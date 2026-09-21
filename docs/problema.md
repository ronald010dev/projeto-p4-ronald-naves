# [P4-ETAPA-01] Proposta e especificação do problema

**Autor:** Ronald Naves Bonfim  
**Tema:** Decisão de empréstimo de exemplares de uma biblioteca.

## 1. Descrição do problema

Uma biblioteca precisa informar se um leitor pode retirar um exemplar físico de um livro naquele momento. A decisão depende da situação do leitor, da quantidade de empréstimos ativos, da existência de atrasos, da disponibilidade do exemplar e de eventual reserva feita por outro leitor. A mesma solicitação sempre deverá produzir o mesmo resultado, segundo as regras abaixo.

## 2. Objetivo

Receber uma solicitação e informar **APROVADO** ou **NEGADO**, com um motivo padronizado. Quando aprovado, informar a data prevista para devolução. Quando a entrada não atende ao formato definido, informar **ENTRADA_INVALIDA**, sem decidir o empréstimo.

## 3. Entradas

Cada solicitação contém os campos:

| Campo | Valores permitidos | Significado |
| --- | --- | --- |
| data_solicitacao | data válida no formato AAAA-MM-DD | Dia em que o empréstimo é solicitado. |
| leitor_ativo | verdadeiro ou falso | Leitor com cadastro ativo. |
| emprestimos_ativos | inteiro de 0 a 3 | Empréstimos ainda não encerrados do leitor. |
| possui_atraso | verdadeiro ou falso | Existe ao menos um empréstimo vencido do leitor. |
| exemplares_disponiveis | inteiro de 0 a 999 | Quantidade disponível do título. |
| reserva | SEM_RESERVA, DO_PROPRIO_LEITOR ou DE_OUTRO_LEITOR | Situação da primeira reserva pendente do título. |

Todos os seis campos são obrigatórios; não há valores implícitos. A reserva indica apenas quem tem prioridade naquele momento, independentemente da quantidade disponível. O sistema recebe um pedido por avaliação, sem histórico ou alteração automática dos dados recebidos.

## 4. Saídas

A resposta contém **status**, **motivo** e **data_devolucao**. Em aprovação: `status=APROVADO`, `motivo=EMPRESTIMO_PERMITIDO` e `data_devolucao` com uma data AAAA-MM-DD. Em negação: `status=NEGADO`, um dos motivos da seção 5 e `data_devolucao=—`. Em entrada inválida: `status=ENTRADA_INVALIDA`, `motivo=DADOS_INVALIDOS` e `data_devolucao=—`. O símbolo `—` significa ausência de data, não uma data literal.

## 5. Regras do problema

1. Validar todos os campos antes de aplicar as regras de empréstimo. Campo ausente, valor fora do intervalo ou tipo/formato incorreto gera `ENTRADA_INVALIDA / DADOS_INVALIDOS`. A data deve existir no calendário gregoriano, inclusive respeitando anos bissextos.
2. Havendo entrada válida, verificar as condições **na ordem abaixo**. A primeira condição verdadeira determina o único motivo da resposta:

| Prioridade | Condição | Motivo |
| --- | --- | --- |
| 1 | leitor_ativo = falso | LEITOR_INATIVO |
| 2 | possui_atraso = verdadeiro | EMPRESTIMO_ATRASADO |
| 3 | emprestimos_ativos = 3 | LIMITE_ATINGIDO |
| 4 | exemplares_disponiveis = 0 | SEM_EXEMPLAR |
| 5 | reserva = DE_OUTRO_LEITOR | RESERVADO_PARA_OUTRO |
| 6 | Nenhuma das anteriores | EMPRESTIMO_PERMITIDO |

3. As prioridades eliminam ambiguidades quando várias condições impedem o empréstimo: por exemplo, leitor inativo e título indisponível gera `LEITOR_INATIVO`.
4. `SEM_RESERVA` e `DO_PROPRIO_LEITOR` permitem o empréstimo quando as demais condições permitem. Mesmo uma reserva do próprio leitor não substitui a exigência de exemplar disponível.
5. A data de devolução de um pedido aprovado é **14 dias corridos após** `data_solicitacao`, contando a partir do dia seguinte. Fins de semana e feriados contam; não há extensão automática.
6. A resposta reflete apenas o pedido recebido. Não altera estoque, reservas, cadastro nem empréstimos ativos.

## 6. Casos de exemplo

Na tabela, cada entrada usa a ordem: **data; ativo; ativos; atraso; disponíveis; reserva**. `V` e `F` abreviam verdadeiro e falso. A saída usa **status / motivo / devolução**.

| Exemplo | Entrada | Saída esperada |
| --- | --- | --- |
| 1 | 2026-09-21; V; 0; F; 2; SEM_RESERVA | APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05 |
| 2 | 2026-09-21; F; 0; F; 2; SEM_RESERVA | NEGADO / LEITOR_INATIVO / — |
| 3 | 2026-09-21; V; 1; V; 2; SEM_RESERVA | NEGADO / EMPRESTIMO_ATRASADO / — |
| 4 | 2026-09-21; V; 3; F; 2; SEM_RESERVA | NEGADO / LIMITE_ATINGIDO / — |
| 5 | 2026-09-21; V; 0; F; 1; DE_OUTRO_LEITOR | NEGADO / RESERVADO_PARA_OUTRO / — |
| 6 | 2026-09-21; V; 0; F; 1; DO_PROPRIO_LEITOR | APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05 |

## 7. Casos-limite

1. **Exatamente dois empréstimos ativos:** ainda é possível retirar um exemplar, se as demais condições permitirem; três atingem o limite.
2. **Exatamente um exemplar disponível:** pode ser emprestado se não houver bloqueio; zero impede o empréstimo, inclusive se a reserva for do próprio leitor.
3. **Mudança de ano:** 2026-12-25 resulta em 2027-01-08 após 14 dias.
4. **Ano bissexto:** 2028-02-29 é uma data válida e resulta em 2028-03-14.

## 8. Restrições

Ficam fora do escopo: criação de cadastros, registro efetivo do empréstimo, alteração de estoque, fila completa ou cancelamento de reservas, multas, cálculo de feriados, renovação e políticas diferentes conforme o leitor ou o tipo de livro. Os limites 3 empréstimos e 14 dias são fixos neste projeto.

## 9. Principais conceitos do domínio

**Leitor** é quem solicita o exemplar; **empréstimo ativo** é um empréstimo não encerrado; **atraso** é a existência de ao menos um empréstimo vencido; **exemplar disponível** é uma unidade do título apta à retirada; **reserva** determina a prioridade de retirada; **solicitação** reúne os dados de uma decisão; **data de devolução** é o prazo resultante da aprovação.

## 10. Adequação aos quatro paradigmas

O problema combina verificações ordenadas, dados relacionados, transformação de uma entrada em uma resposta e regras de elegibilidade. No paradigma imperativo, a decisão pode ser expressa por uma sequência de verificações. No orientado a objetos, leitor, disponibilidade e solicitação podem ter responsabilidades distintas. No funcional, a resposta pode ser calculada como uma transformação dos dados de entrada sem alterá-los. No lógico, as condições de aprovação e os motivos de negação podem ser expressos como relações e regras. Em todos os casos, as entradas, prioridades e saídas previstas aqui são as mesmas.

## 11. Linguagens inicialmente consideradas

| Paradigma | Linguagem | Justificativa inicial |
| --- | --- | --- |
| Imperativo | C | Permite explicitar fluxo de controle, estado e verificações. |
| Orientado a objetos | Java | Tem suporte direto a objetos e encapsulamento; já é familiar na disciplina de POO. |
| Funcional | Haskell | Favorece funções puras e composição de transformações. |
| Lógico | Prolog | Permite expressar elegibilidade e impedimentos por fatos e regras. |

As escolhas poderão ser revistas em etapas futuras com justificativa, mantendo este contrato de comportamento.
