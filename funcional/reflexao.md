# [P4-ETAPA-06] Reflexão sobre a implementação funcional

**Autor:** Ronald Naves Bonfim
**Linguagem utilizada:** Java 21

## Escolha da linguagem

Na proposta inicial, considerei Haskell para a implementação funcional.
Nesta etapa, escolhi Java por já conhecer o ambiente e por seu suporte
aos recursos apresentados nas aulas: lambdas, interfaces funcionais,
Streams, Optional e dados imutáveis.

A mudança de linguagem mantém as entradas, saídas e regras definidas
nas Etapas 01 e 02.

## Mudança de raciocínio

Na implementação funcional, passei a organizar a solução como uma
transformação dos dados recebidos em um resultado.

O processamento segue esta composição:

texto de entrada → conversão e validação → decisão → formatação da saída.

Em vez de perguntar “Que estado devo modificar?”, passei a perguntar
“Que transformação deve ser aplicada aos dados?”.

A avaliação não altera a solicitação, o estoque, as reservas ou os
empréstimos ativos. Ela apenas calcula e retorna uma resposta.

A versão orientada a objetos já possuía atributos finais. Nesta versão,
a principal mudança está em representar regras como funções e compor
transformações usando interfaces funcionais e Streams.

## Funções puras e transparência referencial

As funções converter, avaliar, formatar e verificar não leem o teclado,
não imprimem resultados e não modificam estado externo.

Para os mesmos argumentos, produzem resultados equivalentes. A avaliação
não depende da data atual, de arquivos ou de variáveis globais mutáveis.

Isso permite substituir uma chamada pura pelo resultado correspondente,
preservando o comportamento observável do programa.

Os erros de conversão são tratados dentro de converter e representados
por Optional.empty(), sem realizar operações de entrada e saída.

## Funções impuras e efeitos colaterais

O método main é impuro porque utiliza Scanner para ler o teclado e
System.out para apresentar mensagens.

O método executarTestes também é impuro porque imprime os resultados
no console.

Esses são os pontos de interação com o mundo externo. A lógica principal
permanece separada dessas operações.

## Dados imutáveis

Solicitacao, Resultado e Caso são records sem métodos de alteração.
Seus componentes utilizam valores imutáveis, como String, LocalDate,
boolean, int e Optional<LocalDate>.

As listas de regras e de casos são criadas com List.of, que impede
alterações na estrutura dessas coleções.

O cálculo da devolução utiliza plusDays(14), que retorna uma nova data
sem modificar a data da solicitação.

## Funções de primeira classe

As regras são representadas por valores do tipo Function e armazenadas
na lista REGRAS.

Elas podem ser passadas como argumentos, armazenadas e retornadas por
outras funções. O programa também utiliza lambdas e referências de métodos.

## Funções de alta ordem

criarRegra recebe um Predicate e retorna uma Function. Portanto, é uma
função de alta ordem.

A função retornada captura a condição e o motivo recebidos, formando
uma closure.

As operações map, filter, flatMap e andThen também recebem funções
para definir os comportamentos aplicados aos dados.

## Composição funcional e transformações

PROCESSAR combina CONVERTER, DECIDIR e formatar por meio de andThen.

Na conversão, map transforma os campos de texto removendo espaços
nas extremidades.

Na avaliação, map aplica cada regra à solicitação, flatMap extrai os
motivos presentes e findFirst seleciona o primeiro impedimento.

Nos testes, map transforma casos em relatos. filter seleciona os
casos cujo resultado corresponde ao esperado, e count calcula o total.

## Avaliação preguiçosa

As operações intermediárias dos Streams são executadas quando uma
operação terminal solicita os resultados.

Na avaliação das regras, findFirst permite encerrar o processamento
ao encontrar o primeiro impedimento. A ordem de prioridade definida
no contrato é preservada.

## Recursão

Não utilizei recursão explícita porque o problema exige avaliar uma
quantidade fixa de regras, tarefa expressa pelas operações de Streams.

O enunciado solicita recursão quando apropriada. Neste caso, acrescentar
chamadas recursivas não tornaria a solução mais clara.

A implementação não depende de otimização de chamadas de cauda.

## Verificação do comportamento

Executei os 19 casos originais da Etapa 02, comparando status, motivo
e data de devolução. Todos passaram.

Também executei uma solicitação manual válida e obtive a aprovação
com a data de devolução esperada.

Essas verificações confirmam os resultados dos casos executados,
incluindo prioridades, limites e entradas inválidas previstos na tabela.