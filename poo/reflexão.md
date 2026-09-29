# [P4-ETAPA-04] Reflexão sobre a implementação orientada a objetos

**Autor:** Ronald Naves Bonfim  
**Linguagem utilizada:** Java

## Como meu modelo mudou

Na implementação imperativa, o problema foi organizado principalmente em funções e variáveis. A função principal recebia os dados, verificava as condições em uma ordem definida e apresentava o resultado. Esse modelo resolveu o problema, mas a maior parte das decisões ficou concentrada no mesmo fluxo.

Na implementação orientada a objetos, o problema passou a ser dividido de acordo com as responsabilidades de cada elemento. A solicitação, o resultado, a validação e as regras do empréstimo passaram a ser representados por objetos diferentes. Com isso, o programa deixou de ser apenas uma sequência de verificações e passou a ser uma colaboração entre objetos.

## Estado e encapsulamento

A classe `SolicitacaoEmprestimo` concentra os dados necessários para avaliar um empréstimo, como a data da solicitação, a situação do leitor, a quantidade de empréstimos ativos, a existência de atraso, a disponibilidade de exemplares e a situação da reserva.

Esses atributos foram declarados como privados e são acessados por métodos públicos. Dessa forma, os dados ficam encapsulados e não podem ser alterados diretamente por outras partes do programa.

A classe `ResultadoEmprestimo` representa a resposta da avaliação. Ela guarda o status, o motivo e, quando existe aprovação, a data prevista para devolução. A criação dos resultados é feita por métodos específicos para aprovação, negação e entrada inválida.

## Responsabilidades e relacionamentos

Cada classe possui uma responsabilidade definida. O `ValidadorSolicitacao` verifica se os dados recebidos são válidos. O `AvaliadorEmprestimo` coordena o processo de decisão e aplica as regras na ordem de prioridade definida nas etapas anteriores.

As regras de leitor ativo, atraso, limite de empréstimos, disponibilidade e reserva foram separadas em classes próprias. Todas implementam a interface `RegraEmprestimo`.

O avaliador possui uma lista de regras, caracterizando uma relação de composição. Ele percorre essa lista e solicita que cada objeto verifique a solicitação. Se alguma regra encontrar um impedimento, o resultado é produzido com o motivo correspondente.

## Polimorfismo, reutilização e herança

O polimorfismo aparece no uso da interface `RegraEmprestimo`. Mesmo sendo objetos de classes diferentes, todas as regras são utilizadas por meio do mesmo método `verificar`. Assim, o avaliador não precisa conhecer os detalhes internos de cada regra.

Essa organização também facilita a reutilização e a extensão do programa. Caso seja criada uma nova regra de empréstimo, basta implementar a mesma interface e adicionar o novo objeto à lista utilizada pelo avaliador.

Não foi criada uma hierarquia de herança entre classes porque não foi encontrada uma relação natural de especialização no problema. Criar subclasses somente para demonstrar herança aumentaria a complexidade sem melhorar o modelo. Por isso, foram priorizadas interfaces e composição.

## Comparação com a versão imperativa

A versão imperativa é menor e possui um fluxo mais direto, sendo adequada para uma implementação simples. Porém, conforme o número de regras aumenta, a função responsável pela decisão também tende a crescer.

Na versão orientada a objetos existem mais arquivos, mas as responsabilidades estão separadas. Cada regra pode ser compreendida e modificada isoladamente. O estado está encapsulado e o processo de avaliação pode ser ampliado sem alterar todas as classes existentes.

Os mesmos 19 casos de teste definidos na Etapa 02 foram executados na implementação orientada a objetos. Todos passaram, demonstrando que a mudança do modelo manteve o comportamento definido anteriormente.