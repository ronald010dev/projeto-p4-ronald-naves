# [P4-ETAPA-03] Decisões da implementação imperativa

## Linguagem escolhida

A linguagem C foi escolhida porque permite representar diretamente características do paradigma imperativo, como variáveis, atribuições, estruturas de controle, funções e mudanças de estado. A solução não utiliza classes ou objetos.

## Estados mantidos

Durante uma avaliação, o programa mantém os seguintes dados:

- data da solicitação;
- situação do cadastro do leitor;
- quantidade de empréstimos ativos;
- existência de atraso;
- quantidade de exemplares disponíveis;
- situação da reserva;
- validade da entrada;
- resultado da avaliação;
- data prevista para devolução.

Esses valores são armazenados em variáveis e podem receber novas atribuições durante a execução.

## Operações que modificam o estado

A leitura dos dados altera as variáveis da solicitação. A variável que representa o resultado também é modificada após a validação e a aplicação das regras.

No cálculo da devolução, as variáveis de dia, mês e ano são alteradas dentro de uma repetição até completar os 14 dias corridos. Quando o último dia de um mês ou ano é ultrapassado, os valores são ajustados.

Durante os testes automáticos, o contador de testes aprovados é incrementado sempre que o resultado obtido corresponde ao resultado esperado.

## Efeitos colaterais

As operações de entrada e saída são efeitos colaterais, pois `fgets` recebe informações do usuário e `printf` apresenta informações no console.

A função `calcularDataDevolucao` também modifica o vetor recebido pelo parâmetro `dataDevolucao`. Dessa forma, o resultado calculado fica disponível para o restante do programa.

## Estruturas de controle

Foram utilizadas estruturas `if` e `else` para validar os dados, aplicar as regras na ordem de prioridade e escolher o resultado apresentado.

O laço `while` foi utilizado para acrescentar os 14 dias corridos à data da solicitação. O laço `for` foi utilizado para percorrer os casos de teste da Etapa 02.

A ordem das condições é importante. Se mais de um impedimento estiver presente, o primeiro motivo previsto na especificação determina a resposta.

## Organização dos subprogramas

A solução foi dividida em funções com responsabilidades específicas:

- `anoBissexto`: verifica se um ano é bissexto;
- `diasNoMes`: informa a quantidade de dias de um mês;
- `dataValida`: verifica o formato e a existência da data;
- `dadosValidos`: valida todos os valores de entrada;
- `avaliarEmprestimo`: aplica as regras de aprovação e negação;
- `calcularDataDevolucao`: soma os 14 dias corridos;
- `processarSolicitacao`: coordena a validação e a decisão;
- `mostrarResultado`: apresenta a resposta;
- `lerInteiro`: lê números e rejeita entradas não numéricas;
- `executarModoManual`: recebe e processa uma solicitação;
- `executarTestes`: executa os casos definidos na Etapa 02.

Os dados necessários são enviados às funções por parâmetros. Isso evita o uso de variáveis globais.

## Validação com os casos da Etapa 02

Foram executados 19 casos de teste:

- 10 casos normais;
- 5 casos-limite;
- 4 casos de entrada inválida.

O programa compara automaticamente o resultado e a data obtidos com os valores esperados. Os 19 casos foram aprovados.

## Por que a solução é imperativa

A solução é predominantemente imperativa porque descreve uma sequência de comandos que modifica o estado do programa. Os dados são armazenados em variáveis, recebem atribuições e são avaliados por estruturas condicionais e de repetição.

A execução segue uma ordem definida: leitura, validação, aplicação das regras, cálculo da data quando necessário e apresentação do resultado. Não foram utilizadas classes ou abstrações orientadas a objetos.