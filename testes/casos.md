# [P4-ETAPA-02] Contrato semântico e casos de teste

**Autor:** Ronald Naves Bonfim  
**Especificação vinculada:** [../docs/problema.md](../docs/problema.md)

## Convenções do contrato

Cada avaliação recebe os seis campos obrigatórios descritos na Etapa 01, sem valores implícitos. Nas tabelas, a entrada é **data_solicitacao; leitor_ativo; emprestimos_ativos; possui_atraso; exemplares_disponiveis; reserva**. `V` representa verdadeiro e `F` representa falso. A saída é **status / motivo / data_devolucao**; `—` significa que não há data. Os códigos e a ordem de prioridade da Etapa 01 são parte do contrato e valem igualmente nos quatro paradigmas. Cada caso é independente; executar um teste não modifica os dados de outro.

## Casos normais (10)

| ID | Entrada | Saída esperada | Descrição |
| --- | --- | --- | --- |
| N01 | 2026-09-21; V; 0; F; 2; SEM_RESERVA | APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05 | Leitor apto, sem empréstimos ou reserva. |
| N02 | 2026-09-21; V; 1; F; 4; SEM_RESERVA | APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05 | Um empréstimo ativo não bloqueia outro. |
| N03 | 2026-09-21; V; 0; F; 1; DO_PROPRIO_LEITOR | APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05 | Reserva do solicitante permite retirada. |
| N04 | 2026-09-21; F; 0; F; 2; SEM_RESERVA | NEGADO / LEITOR_INATIVO / — | Cadastro inativo bloqueia. |
| N05 | 2026-09-21; V; 1; V; 2; SEM_RESERVA | NEGADO / EMPRESTIMO_ATRASADO / — | Atraso bloqueia. |
| N06 | 2026-09-21; V; 3; F; 2; SEM_RESERVA | NEGADO / LIMITE_ATINGIDO / — | Limite de três empréstimos atingido. |
| N07 | 2026-09-21; V; 0; F; 0; SEM_RESERVA | NEGADO / SEM_EXEMPLAR / — | Título indisponível. |
| N08 | 2026-09-21; V; 0; F; 1; DE_OUTRO_LEITOR | NEGADO / RESERVADO_PARA_OUTRO / — | Prioridade da reserva de outro leitor. |
| N09 | 2026-09-21; F; 3; V; 0; DE_OUTRO_LEITOR | NEGADO / LEITOR_INATIVO / — | Impedimentos simultâneos: prevalece a prioridade 1. |
| N10 | 2026-09-21; V; 0; V; 0; DE_OUTRO_LEITOR | NEGADO / EMPRESTIMO_ATRASADO / — | Atraso prevalece sobre estoque e reserva. |

## Casos-limite (5)

| ID | Entrada | Saída esperada | Descrição |
| --- | --- | --- | --- |
| L01 | 2026-09-21; V; 2; F; 1; SEM_RESERVA | APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05 | Maior quantidade de empréstimos ainda permitida. |
| L02 | 2026-09-21; V; 0; F; 0; DO_PROPRIO_LEITOR | NEGADO / SEM_EXEMPLAR / — | Reserva própria não cria disponibilidade. |
| L03 | 2026-12-25; V; 0; F; 1; SEM_RESERVA | APROVADO / EMPRESTIMO_PERMITIDO / 2027-01-08 | Soma de dias atravessando o ano. |
| L04 | 2028-02-29; V; 0; F; 1; SEM_RESERVA | APROVADO / EMPRESTIMO_PERMITIDO / 2028-03-14 | Dia bissexto válido. |
| L05 | 2026-09-21; V; 0; F; 999; SEM_RESERVA | APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05 | Maior estoque admitido. |

## Entradas inválidas (4)

| ID | Entrada | Saída esperada | Descrição |
| --- | --- | --- | --- |
| I01 | 2026-02-29; V; 0; F; 1; SEM_RESERVA | ENTRADA_INVALIDA / DADOS_INVALIDOS / — | Data inexistente em ano não bissexto. |
| I02 | 2026-09-21; V; -1; F; 1; SEM_RESERVA | ENTRADA_INVALIDA / DADOS_INVALIDOS / — | Quantidade negativa de empréstimos. |
| I03 | 2026-09-21; V; 0; F; 1; RESERVA_DESCONHECIDA | ENTRADA_INVALIDA / DADOS_INVALIDOS / — | Valor de reserva fora das opções. |
| I04 | 2026-09-21; F; 0; F; 1000; SEM_RESERVA | ENTRADA_INVALIDA / DADOS_INVALIDOS / — | Validação antecede a regra que negaria leitor inativo. |

**Total:** 19 casos independentes: 10 normais, 5 casos-limite e 4 inválidos.
