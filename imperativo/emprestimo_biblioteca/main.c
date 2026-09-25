#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

// Codigos dos resultados
#define ENTRADA_INVALIDA -1
#define EMPRESTIMO_PERMITIDO 0
#define LEITOR_INATIVO 1
#define EMPRESTIMO_ATRASADO 2
#define LIMITE_ATINGIDO 3
#define SEM_EXEMPLAR 4
#define RESERVADO_PARA_OUTRO 5

// Representa um caso de teste
typedef struct
{
    char id[5];
    char data[11];
    int leitorAtivo;
    int emprestimosAtivos;
    int possuiAtraso;
    int exemplaresDisponiveis;
    int reserva;
    int resultadoEsperado;
    char dataEsperada[11];
} CasoTeste;

int anoBissexto(int ano)
{
    if (ano % 400 == 0)
        return 1;

    if (ano % 100 == 0)
        return 0;

    if (ano % 4 == 0)
        return 1;

    return 0;
}

int diasNoMes(int mes, int ano)
{
    if (mes == 2)
    {
        if (anoBissexto(ano))
            return 29;

        return 28;
    }

    if (mes == 4 || mes == 6 || mes == 9 || mes == 11)
        return 30;

    return 31;
}

int dataValida(char data[])
{
    int ano, mes, dia;
    char caractereExtra;

    if (strlen(data) != 10)
        return 0;

    if (data[4] != '-' || data[7] != '-')
        return 0;

    if (sscanf(data, "%d-%d-%d%c",
               &ano, &mes, &dia, &caractereExtra) != 3)
        return 0;

    if (ano < 1)
        return 0;

    if (mes < 1 || mes > 12)
        return 0;

    if (dia < 1 || dia > diasNoMes(mes, ano))
        return 0;

    return 1;
}

int dadosValidos(char data[],
                 int leitorAtivo,
                 int emprestimosAtivos,
                 int possuiAtraso,
                 int exemplaresDisponiveis,
                 int reserva)
{
    if (!dataValida(data))
        return 0;

    if (leitorAtivo != 0 && leitorAtivo != 1)
        return 0;

    if (emprestimosAtivos < 0 || emprestimosAtivos > 3)
        return 0;

    if (possuiAtraso != 0 && possuiAtraso != 1)
        return 0;

    if (exemplaresDisponiveis < 0 ||
        exemplaresDisponiveis > 999)
        return 0;

    if (reserva < 0 || reserva > 2)
        return 0;

    return 1;
}

// Aplica as regras na ordem definida na Etapa 01
int avaliarEmprestimo(int leitorAtivo,
                      int emprestimosAtivos,
                      int possuiAtraso,
                      int exemplaresDisponiveis,
                      int reserva)
{
    if (leitorAtivo == 0)
        return LEITOR_INATIVO;

    if (possuiAtraso == 1)
        return EMPRESTIMO_ATRASADO;

    if (emprestimosAtivos == 3)
        return LIMITE_ATINGIDO;

    if (exemplaresDisponiveis == 0)
        return SEM_EXEMPLAR;

    if (reserva == 2)
        return RESERVADO_PARA_OUTRO;

    return EMPRESTIMO_PERMITIDO;
}

// Soma os 14 dias corridos
void calcularDataDevolucao(char dataSolicitacao[],
                           char dataDevolucao[])
{
    int ano, mes, dia;
    int diasAdicionados = 0;

    sscanf(dataSolicitacao, "%d-%d-%d", &ano, &mes, &dia);

    while (diasAdicionados < 14)
    {
        dia++;
        diasAdicionados++;

        if (dia > diasNoMes(mes, ano))
        {
            dia = 1;
            mes++;

            if (mes > 12)
            {
                mes = 1;
                ano++;
            }
        }
    }

    sprintf(dataDevolucao, "%04d-%02d-%02d", ano, mes, dia);
}

// Valida e processa uma solicitacao
int processarSolicitacao(char data[],
                         int leitorAtivo,
                         int emprestimosAtivos,
                         int possuiAtraso,
                         int exemplaresDisponiveis,
                         int reserva,
                         char dataDevolucao[])
{
    int resultado;

    if (!dadosValidos(data,
                      leitorAtivo,
                      emprestimosAtivos,
                      possuiAtraso,
                      exemplaresDisponiveis,
                      reserva))
    {
        strcpy(dataDevolucao, "-");
        return ENTRADA_INVALIDA;
    }

    resultado = avaliarEmprestimo(
        leitorAtivo,
        emprestimosAtivos,
        possuiAtraso,
        exemplaresDisponiveis,
        reserva
    );

    if (resultado == EMPRESTIMO_PERMITIDO)
        calcularDataDevolucao(data, dataDevolucao);
    else
        strcpy(dataDevolucao, "-");

    return resultado;
}

char *nomeResultado(int resultado)
{
    if (resultado == ENTRADA_INVALIDA)
        return "ENTRADA_INVALIDA / DADOS_INVALIDOS";

    if (resultado == EMPRESTIMO_PERMITIDO)
        return "APROVADO / EMPRESTIMO_PERMITIDO";

    if (resultado == LEITOR_INATIVO)
        return "NEGADO / LEITOR_INATIVO";

    if (resultado == EMPRESTIMO_ATRASADO)
        return "NEGADO / EMPRESTIMO_ATRASADO";

    if (resultado == LIMITE_ATINGIDO)
        return "NEGADO / LIMITE_ATINGIDO";

    if (resultado == SEM_EXEMPLAR)
        return "NEGADO / SEM_EXEMPLAR";

    return "NEGADO / RESERVADO_PARA_OUTRO";
}

void mostrarResultado(int resultado, char dataDevolucao[])
{
    printf("\n=== RESULTADO ===\n");

    if (resultado == ENTRADA_INVALIDA)
    {
        printf("Status: ENTRADA_INVALIDA\n");
        printf("Motivo: DADOS_INVALIDOS\n");
    }
    else if (resultado == EMPRESTIMO_PERMITIDO)
    {
        printf("Status: APROVADO\n");
        printf("Motivo: EMPRESTIMO_PERMITIDO\n");
    }
    else
    {
        printf("Status: NEGADO\n");

        if (resultado == LEITOR_INATIVO)
            printf("Motivo: LEITOR_INATIVO\n");
        else if (resultado == EMPRESTIMO_ATRASADO)
            printf("Motivo: EMPRESTIMO_ATRASADO\n");
        else if (resultado == LIMITE_ATINGIDO)
            printf("Motivo: LIMITE_ATINGIDO\n");
        else if (resultado == SEM_EXEMPLAR)
            printf("Motivo: SEM_EXEMPLAR\n");
        else
            printf("Motivo: RESERVADO_PARA_OUTRO\n");
    }

    printf("Data de devolucao: %s\n", dataDevolucao);
}

// Remove o Enter lido pelo fgets
void removerQuebraLinha(char texto[])
{
    texto[strcspn(texto, "\r\n")] = '\0';
}

// Le um numero inteiro e rejeita letras
int lerInteiro(char mensagem[], int *valor)
{
    char linha[100];
    char *fim;
    long numero;

    printf("%s", mensagem);

    if (fgets(linha, sizeof(linha), stdin) == NULL)
        return 0;

    numero = strtol(linha, &fim, 10);

    if (fim == linha)
        return 0;

    while (*fim == ' ' || *fim == '\t' ||
           *fim == '\n' || *fim == '\r')
    {
        fim++;
    }

    if (*fim != '\0')
        return 0;

    if (numero < INT_MIN || numero > INT_MAX)
        return 0;

    *valor = (int) numero;
    return 1;
}

void executarModoManual()
{
    char dataSolicitacao[100];
    char dataDevolucao[11];
    int leitorAtivo = 0;
    int emprestimosAtivos = 0;
    int possuiAtraso = 0;
    int exemplaresDisponiveis = 0;
    int reserva = 0;
    int leiturasValidas = 1;
    int resultado;

    printf("\nData da solicitacao (AAAA-MM-DD): ");

    if (fgets(dataSolicitacao,
              sizeof(dataSolicitacao), stdin) == NULL)
    {
        leiturasValidas = 0;
    }

    removerQuebraLinha(dataSolicitacao);

    if (!lerInteiro(
            "Leitor ativo? (1 = Sim / 0 = Nao): ",
            &leitorAtivo))
    {
        leiturasValidas = 0;
    }

    if (!lerInteiro(
            "Quantidade de emprestimos ativos: ",
            &emprestimosAtivos))
    {
        leiturasValidas = 0;
    }

    if (!lerInteiro(
            "Possui atraso? (1 = Sim / 0 = Nao): ",
            &possuiAtraso))
    {
        leiturasValidas = 0;
    }

    if (!lerInteiro(
            "Quantidade de exemplares disponiveis: ",
            &exemplaresDisponiveis))
    {
        leiturasValidas = 0;
    }

    printf("\nSituacao da reserva:\n");
    printf("0 - Sem reserva\n");
    printf("1 - Reserva do proprio leitor\n");
    printf("2 - Reserva de outro leitor\n");

    if (!lerInteiro("Escolha: ", &reserva))
        leiturasValidas = 0;

    if (!leiturasValidas)
    {
        resultado = ENTRADA_INVALIDA;
        strcpy(dataDevolucao, "-");
    }
    else
    {
        resultado = processarSolicitacao(
            dataSolicitacao,
            leitorAtivo,
            emprestimosAtivos,
            possuiAtraso,
            exemplaresDisponiveis,
            reserva,
            dataDevolucao
        );
    }

    mostrarResultado(resultado, dataDevolucao);
}

void executarTestes()
{
    CasoTeste testes[] =
    {
        {"N01", "2026-09-21", 1, 0, 0, 2, 0,
         EMPRESTIMO_PERMITIDO, "2026-10-05"},

        {"N02", "2026-09-21", 1, 1, 0, 4, 0,
         EMPRESTIMO_PERMITIDO, "2026-10-05"},

        {"N03", "2026-09-21", 1, 0, 0, 1, 1,
         EMPRESTIMO_PERMITIDO, "2026-10-05"},

        {"N04", "2026-09-21", 0, 0, 0, 2, 0,
         LEITOR_INATIVO, "-"},

        {"N05", "2026-09-21", 1, 1, 1, 2, 0,
         EMPRESTIMO_ATRASADO, "-"},

        {"N06", "2026-09-21", 1, 3, 0, 2, 0,
         LIMITE_ATINGIDO, "-"},

        {"N07", "2026-09-21", 1, 0, 0, 0, 0,
         SEM_EXEMPLAR, "-"},

        {"N08", "2026-09-21", 1, 0, 0, 1, 2,
         RESERVADO_PARA_OUTRO, "-"},

        {"N09", "2026-09-21", 0, 3, 1, 0, 2,
         LEITOR_INATIVO, "-"},

        {"N10", "2026-09-21", 1, 0, 1, 0, 2,
         EMPRESTIMO_ATRASADO, "-"},

        {"L01", "2026-09-21", 1, 2, 0, 1, 0,
         EMPRESTIMO_PERMITIDO, "2026-10-05"},

        {"L02", "2026-09-21", 1, 0, 0, 0, 1,
         SEM_EXEMPLAR, "-"},

        {"L03", "2026-12-25", 1, 0, 0, 1, 0,
         EMPRESTIMO_PERMITIDO, "2027-01-08"},

        {"L04", "2028-02-29", 1, 0, 0, 1, 0,
         EMPRESTIMO_PERMITIDO, "2028-03-14"},

        {"L05", "2026-09-21", 1, 0, 0, 999, 0,
         EMPRESTIMO_PERMITIDO, "2026-10-05"},

        {"I01", "2026-02-29", 1, 0, 0, 1, 0,
         ENTRADA_INVALIDA, "-"},

        {"I02", "2026-09-21", 1, -1, 0, 1, 0,
         ENTRADA_INVALIDA, "-"},

        {"I03", "2026-09-21", 1, 0, 0, 1, 3,
         ENTRADA_INVALIDA, "-"},

        {"I04", "2026-09-21", 0, 0, 0, 1000, 0,
         ENTRADA_INVALIDA, "-"}
    };

    int quantidadeTestes =
        sizeof(testes) / sizeof(testes[0]);

    int i;
    int resultadoObtido;
    int testesAprovados = 0;
    char dataObtida[11];

    printf("\n=== TESTES AUTOMATICOS DA ETAPA 02 ===\n\n");

    for (i = 0; i < quantidadeTestes; i++)
    {
        resultadoObtido = processarSolicitacao(
            testes[i].data,
            testes[i].leitorAtivo,
            testes[i].emprestimosAtivos,
            testes[i].possuiAtraso,
            testes[i].exemplaresDisponiveis,
            testes[i].reserva,
            dataObtida
        );

        if (resultadoObtido ==
                testes[i].resultadoEsperado &&
            strcmp(dataObtida,
                   testes[i].dataEsperada) == 0)
        {
            printf("[%s] PASSOU - %s\n",
                   testes[i].id,
                   nomeResultado(resultadoObtido));

            testesAprovados++;
        }
        else
        {
            printf("[%s] FALHOU\n", testes[i].id);

            printf("  Esperado: %s / %s\n",
                   nomeResultado(
                       testes[i].resultadoEsperado),
                   testes[i].dataEsperada);

            printf("  Obtido:   %s / %s\n",
                   nomeResultado(resultadoObtido),
                   dataObtida);
        }
    }

    printf("\nResultado final: %d de %d testes passaram.\n",
           testesAprovados, quantidadeTestes);
}

int main()
{
    int opcao;

    printf("=== SISTEMA DE EMPRESTIMO DA BIBLIOTECA ===\n");
    printf("1 - Avaliar uma solicitacao\n");
    printf("2 - Executar os testes da Etapa 02\n");

    if (!lerInteiro("Escolha: ", &opcao))
    {
        printf("\nOpcao invalida.\n");
        return 0;
    }

    if (opcao == 1)
    {
        executarModoManual();
    }
    else if (opcao == 2)
    {
        executarTestes();
    }
    else
    {
        printf("\nOpcao invalida.\n");
    }

    return 0;
}
