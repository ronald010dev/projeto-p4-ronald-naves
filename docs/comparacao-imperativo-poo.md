# [P4-ETAPA-05] Comparação entre as implementações imperativa e orientada a objetos

**Autor:** Ronald Naves Bonfim  
**Implementação imperativa:** C  
**Implementação orientada a objetos:** Java

## 1. Introdução

O mesmo problema de decisão de empréstimos de uma biblioteca foi implementado em dois paradigmas. Na versão imperativa, desenvolvida em C, a solução foi organizada como uma sequência de comandos e funções que recebem dados, verificam condições e produzem um resultado. Na versão orientada a objetos, desenvolvida em Java, o problema foi remodelado por meio de classes, objetos, interfaces e responsabilidades separadas.

As duas implementações recebem os mesmos dados, seguem a mesma ordem de prioridade das regras e foram verificadas com os mesmos 19 casos de teste definidos na Etapa 02. Portanto, o comportamento esperado não mudou. A principal diferença está na forma como o código representa e organiza esse comportamento.

## 2. Representação do estado

Na implementação imperativa, o estado é representado principalmente por variáveis que armazenam a data da solicitação, situação do leitor, empréstimos ativos, atraso, quantidade de exemplares, reserva e resultado da avaliação. Essas informações são passadas para funções e consultadas durante o fluxo de decisão.

Na implementação orientada a objetos, o estado da entrada ficou concentrado em um objeto da classe `SolicitacaoEmprestimo`. O resultado passou a ser representado por um objeto da classe `ResultadoEmprestimo`. Também foram criados os enums `SituacaoReserva`, `StatusEmprestimo` e `MotivoEmprestimo`, evitando a utilização de números ou textos sem significado explícito.

A versão orientada a objetos tornou mais claro quais dados pertencem à solicitação e quais pertencem ao resultado.

## 3. Mutabilidade

Na versão em C, as variáveis podem receber novos valores durante a execução. Os dados são lidos, armazenados e modificados conforme as funções executam as validações, calculam datas e registram os resultados dos testes. Essa alteração direta de variáveis é uma característica comum do paradigma imperativo.

Na versão Java, os atributos de `SolicitacaoEmprestimo` e `ResultadoEmprestimo` foram declarados como privados e finais. Seus valores são definidos na criação dos objetos e não são modificados posteriormente. Com isso, o estado principal ficou mais protegido.

Ainda existe mutabilidade em pontos específicos, como nos contadores de testes executados e aprovados da classe `TesteSistema`, mas ela ficou limitada à classe responsável pelos testes.

## 4. Fluxo de controle

No código em C, o fluxo é explícito. O programa utiliza comandos como `if`, `else`, `switch`, `for` e chamadas de funções para determinar a ordem das operações. A decisão do empréstimo segue diretamente a sequência das verificações.

Na versão Java também existem estruturas de controle, mas parte da decisão foi distribuída entre objetos. A classe `AvaliadorEmprestimo` percorre uma lista de objetos que implementam a interface `RegraEmprestimo`. Cada objeto verifica uma condição e pode retornar o motivo da negação.

A ordem das regras permanece a mesma nas duas versões:

1. validade dos dados;
2. situação do leitor;
3. existência de atraso;
4. limite de empréstimos;
5. disponibilidade do exemplar;
6. situação da reserva.

## 5. Decomposição do problema

Na implementação imperativa, o problema foi decomposto em funções. Existem operações para leitura, validação, avaliação, cálculo da data, apresentação dos resultados e execução dos testes. A organização está concentrada nas ações realizadas pelo programa.

Na implementação orientada a objetos, o problema foi decomposto por responsabilidades. `SolicitacaoEmprestimo` representa a entrada, `ResultadoEmprestimo` representa a saída, `ValidadorSolicitacao` verifica os dados e `AvaliadorEmprestimo` coordena a decisão.

As condições foram separadas nas classes:

- `RegraLeitorAtivo`;
- `RegraAtraso`;
- `RegraLimiteEmprestimos`;
- `RegraDisponibilidade`;
- `RegraReserva`.

Essa foi uma das principais mudanças entre os dois modelos.

## 6. Reutilização

Na versão imperativa, as funções podem ser chamadas em diferentes partes do programa. Isso permite reutilizar operações como a avaliação de uma solicitação e o cálculo da data de devolução.

Na versão orientada a objetos, a reutilização ocorre por meio das classes e da interface `RegraEmprestimo`. O mesmo objeto `AvaliadorEmprestimo` pode avaliar diferentes solicitações. As regras também podem ser utilizadas por qualquer avaliador que trabalhe com o mesmo contrato.

A interface permite que todas as regras sejam tratadas da mesma forma, mesmo sendo objetos de classes diferentes. Isso representa o uso de polimorfismo.

## 7. Manutenção

Para um problema pequeno, a implementação em C é mais direta e pode ser compreendida em um único arquivo. Porém, conforme novas regras são adicionadas, a função de avaliação tende a crescer e concentrar muitas condições.

Na versão Java existem mais arquivos, mas cada regra está isolada. Se a regra do limite de empréstimos mudar, por exemplo, a alteração pode ser feita principalmente em `RegraLimiteEmprestimos`.

A separação facilita localizar a responsabilidade de cada trecho. Por outro lado, exige compreender a relação entre várias classes antes de entender o fluxo completo.

## 8. Facilidade de extensão

Na versão imperativa, acrescentar uma regra exige criar uma nova função ou condição e alterar diretamente o fluxo de avaliação.

Na versão orientada a objetos, uma nova regra pode ser criada como outra classe que implementa `RegraEmprestimo`. Depois, o objeto dessa classe pode ser acrescentado à lista mantida pelo `AvaliadorEmprestimo`.

A versão orientada a objetos apresentou vantagem principalmente nesse ponto, porque permite adicionar comportamentos mantendo as regras existentes separadas.

## 9. Tratamento de erros

Na versão em C, o tratamento de entradas inválidas é feito por verificações explícitas. O programa confere os valores recebidos e produz `ENTRADA_INVALIDA` com o motivo `DADOS_INVALIDOS` quando alguma informação não pertence ao domínio permitido.

Na versão Java, a classe `ValidadorSolicitacao` possui a responsabilidade de verificar a validade do objeto recebido. A entrada manual também é protegida por um bloco `try/catch`, que trata problemas como data em formato incorreto e valor não numérico.

As duas versões produzem o mesmo resultado semântico para dados inválidos, embora utilizem mecanismos diferentes.

## 10. Efeitos colaterais

Na implementação imperativa, funções que leem valores, escrevem no console ou modificam variáveis produzem efeitos colaterais. Os contadores utilizados durante os testes também são alterados durante a execução.

Na implementação orientada a objetos, também existem efeitos colaterais na leitura pelo `Scanner`, na impressão com `System.out` e na atualização dos contadores da classe `TesteSistema`.

Entretanto, a decisão principal foi separada da entrada e da exibição. O método de avaliação recebe uma solicitação e devolve um objeto de resultado, sem imprimir diretamente no console. Isso reduz os efeitos colaterais na lógica principal.

## 11. Facilidade para testar

As duas versões utilizam os mesmos 19 casos da Etapa 02, divididos em 10 casos normais, 5 casos-limite e 4 entradas inválidas.

Na versão em C, os testes executam as funções responsáveis pela decisão e comparam os valores produzidos com os resultados esperados.

Na versão Java, a classe `TesteSistema` cria objetos `SolicitacaoEmprestimo`, chama o método `avaliar` de `AvaliadorEmprestimo` e compara o status, o motivo e a data de devolução.

A separação entre entrada, avaliação e apresentação facilitou o teste da versão orientada a objetos, pois não é necessário usar o menu manual para testar a regra principal.

## 12. Organização do código

A implementação imperativa ficou concentrada principalmente no arquivo `main.c`. As diferentes tarefas foram separadas em funções, mas continuam reunidas no mesmo módulo.

A implementação orientada a objetos foi distribuída em várias classes no pacote `biblioteca`. Essa organização aumenta a quantidade de arquivos, mas permite identificar a função de cada parte por seu próprio nome.

O código em C oferece uma visão mais rápida do fluxo completo. O código Java oferece uma visão mais clara das responsabilidades.

## 13. Complexidade

Para o tamanho atual do problema, a implementação imperativa é menor e mais simples de executar. A sequência de regras pode ser representada diretamente por condições.

A versão orientada a objetos acrescentou classes, enums, uma interface, construtores e relacionamentos entre objetos. Essa estrutura aumenta a complexidade inicial do projeto.

Por outro lado, essa complexidade traz vantagens quando o sistema cresce, porque as responsabilidades estão separadas e novas regras podem ser incluídas com menos impacto sobre o código existente.

## 14. Respostas às perguntas propostas

### 14.1 Qual problema ficou mais fácil de expressar de forma imperativa?

O fluxo sequencial da decisão ficou mais fácil de expressar de forma imperativa. O problema consiste em receber os dados e verificar condições em uma ordem determinada. Em C, isso pôde ser representado diretamente com variáveis, funções e estruturas condicionais.

O cálculo da data e a execução dos testes também ficaram visíveis em um fluxo único, sem a necessidade de criar vários tipos e objetos.

### 14.2 Qual problema ficou mais fácil de expressar utilizando orientação a objetos?

A separação das responsabilidades e a representação dos elementos do domínio ficaram mais fáceis de expressar com orientação a objetos.

Uma solicitação é representada por `SolicitacaoEmprestimo`, uma resposta por `ResultadoEmprestimo` e cada impedimento por uma classe de regra. Isso torna mais claro o papel de cada parte do sistema.

### 14.3 Onde a orientação a objetos realmente trouxe vantagem?

A principal vantagem apareceu na organização e na extensão das regras. A interface `RegraEmprestimo` permite que diferentes regras sejam executadas de maneira uniforme pelo `AvaliadorEmprestimo`.

Também houve vantagem no encapsulamento. Os dados da solicitação e do resultado ficam protegidos dentro dos objetos. Além disso, a lógica da decisão ficou separada do menu, da leitura e da impressão.

### 14.4 Em quais situações a utilização de objetos acrescentou complexidade desnecessária?

Para algumas representações simples, a quantidade de classes pode ser considerada maior do que a necessidade imediata do problema. Na versão imperativa, uma condição pode ser escrita diretamente com um `if`. Na versão orientada a objetos, cada condição recebeu sua própria classe.

Os enums e as classes de resultado e solicitação melhoram a clareza, mas também exigem mais arquivos e relacionamentos. Para um programa pequeno e que não receberá novas regras, essa estrutura pode parecer excessiva.

Não foi criada uma hierarquia de herança entre classes porque ela não representaria uma relação natural do problema e acrescentaria ainda mais complexidade sem benefício real.

### 14.5 Que partes do problema praticamente não mudaram entre as duas implementações?

Não mudaram:

- as entradas;
- as saídas;
- as regras de negócio;
- a ordem de prioridade;
- o limite de três empréstimos;
- o prazo de 14 dias;
- os motivos de negação;
- os 19 casos de teste;
- os resultados esperados.

Esses elementos fazem parte do contrato semântico definido nas Etapas 01 e 02 e não dependem do paradigma utilizado.

### 14.6 Que partes precisaram ser completamente remodeladas?

A organização interna da solução precisou ser remodelada. Na versão imperativa, o programa foi estruturado por funções e comandos. Na versão orientada a objetos, passou a ser estruturado por classes, objetos e responsabilidades.

As verificações que faziam parte de um fluxo de condições foram transformadas em objetos que implementam uma interface comum. Os dados deixaram de circular apenas como variáveis e passaram a compor objetos de solicitação e resultado.

## 15. Conclusão

As duas implementações resolvem o mesmo problema e produzem os mesmos resultados, mas expressam a solução de maneiras diferentes.

A implementação imperativa é direta, possui menos estruturas e representa bem a sequência de verificações. A implementação orientada a objetos exige mais arquivos e planejamento, mas separa melhor o estado, as responsabilidades e as regras.

Neste projeto, a versão em C foi mais simples para representar o fluxo atual. A versão Java apresentou maior vantagem para manutenção, testes e inclusão de novas regras. A comparação mostrou que a escolha do paradigma não muda apenas a sintaxe da linguagem, mas também a forma de pensar e organizar a solução.