package biblioteca.funcional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

// [P4-ETAPA-06] Implementacao funcional
// Autor: Ronald Naves Bonfim
public class Main {

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("uuuu-MM-dd")
                    .withResolverStyle(ResolverStyle.STRICT);

    // Records com componentes imutaveis: nao existem setters.
    record Solicitacao(
            LocalDate data,
            boolean leitorAtivo,
            int emprestimosAtivos,
            boolean possuiAtraso,
            int exemplaresDisponiveis,
            String reserva) {
    }

    record Resultado(String status, String motivo, Optional<LocalDate> data) {
    }

    record Caso(String id, String entrada, String esperado) {
    }

    // Funcao de alta ordem: recebe um predicado e retorna uma funcao.
    // A funcao retornada captura a condicao e o motivo (closure).
    private static Function<Solicitacao, Optional<String>> criarRegra(
            Predicate<Solicitacao> condicao, String motivo) {

        return solicitacao -> condicao.test(solicitacao)
                ? Optional.of(motivo)
                : Optional.empty();
    }

    // Funcoes tratadas como valores e armazenadas em uma lista imutavel.
    // A ordem corresponde a prioridade do contrato das Etapas 01 e 02.
    private static final List<Function<Solicitacao, Optional<String>>> REGRAS =
            List.of(
                    criarRegra(s -> !s.leitorAtivo(), "LEITOR_INATIVO"),
                    criarRegra(Solicitacao::possuiAtraso, "EMPRESTIMO_ATRASADO"),
                    criarRegra(s -> s.emprestimosAtivos() == 3, "LIMITE_ATINGIDO"),
                    criarRegra(s -> s.exemplaresDisponiveis() == 0, "SEM_EXEMPLAR"),
                    criarRegra(s -> s.reserva().equals("DE_OUTRO_LEITOR"),
                            "RESERVADO_PARA_OUTRO")
            );

    // Funcao pura: o resultado depende somente do texto recebido.
    private static boolean lerBooleano(String texto) {
        return switch (texto) {
            case "V" -> true;
            case "F" -> false;
            default -> throw new IllegalArgumentException("Booleano invalido");
        };
    }

    // Funcao pura: converte e valida os seis campos.
    // Entradas invalidas sao representadas por Optional.empty().
    private static Optional<Solicitacao> converter(String entrada) {
        if (entrada == null) {
            return Optional.empty();
        }

        final List<String> campos = List.of(entrada.split(";", -1))
                .stream()
                .map(String::trim)
                .toList();

        if (campos.size() != 6) {
            return Optional.empty();
        }

        try {
            if (!campos.get(0).matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")
                    || !campos.get(2).matches("[0-9]+")
                    || !campos.get(4).matches("[0-9]+")) {
                return Optional.empty();
            }

            final LocalDate data = LocalDate.parse(campos.get(0), FORMATO);
            final boolean ativo = lerBooleano(campos.get(1));
            final int emprestimos = Integer.parseInt(campos.get(2));
            final boolean atraso = lerBooleano(campos.get(3));
            final int exemplares = Integer.parseInt(campos.get(4));
            final String reserva = campos.get(5);

            if (data.getYear() < 1
                    || emprestimos > 3
                    || exemplares > 999
                    || !List.of("SEM_RESERVA", "DO_PROPRIO_LEITOR",
                            "DE_OUTRO_LEITOR").contains(reserva)) {
                return Optional.empty();
            }

            return Optional.of(new Solicitacao(
                    data, ativo, emprestimos, atraso, exemplares, reserva));

        } catch (IllegalArgumentException
                | java.time.format.DateTimeParseException e) {
            return Optional.empty();
        }
    }

    // Funcao pura com pipeline funcional.
    private static Resultado avaliar(Solicitacao solicitacao) {
        return REGRAS.stream()
                .map(regra -> regra.apply(solicitacao))
                .flatMap(Optional::stream)
                .findFirst()
                .map(motivo -> new Resultado(
                        "NEGADO", motivo, Optional.empty()))
                .orElseGet(() -> new Resultado(
                        "APROVADO",
                        "EMPRESTIMO_PERMITIDO",
                        Optional.of(solicitacao.data().plusDays(14))));
    }

    // Funcao pura: nenhuma impressao acontece aqui.
    private static String formatar(Resultado resultado) {
        return resultado.status() + " / "
                + resultado.motivo() + " / "
                + resultado.data()
                        .map(LocalDate::toString)
                        .orElse("—");
    }

    private static final Function<String, Optional<Solicitacao>> CONVERTER =
            Main::converter;

    private static final Function<Optional<Solicitacao>, Resultado> DECIDIR =
            solicitacao -> solicitacao
                    .map(Main::avaliar)
                    .orElseGet(() -> new Resultado(
                            "ENTRADA_INVALIDA",
                            "DADOS_INVALIDOS",
                            Optional.empty()));

    // Composicao funcional explicita usando andThen.
    private static final Function<String, String> PROCESSAR =
            CONVERTER
                    .andThen(DECIDIR)
                    .andThen(Main::formatar);

    // Os mesmos 19 casos definidos em testes/casos.md.
    private static final List<Caso> CASOS = List.of(
            new Caso("N01", "2026-09-21;V;0;F;2;SEM_RESERVA",
                    "APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05"),
            new Caso("N02", "2026-09-21;V;1;F;4;SEM_RESERVA",
                    "APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05"),
            new Caso("N03", "2026-09-21;V;0;F;1;DO_PROPRIO_LEITOR",
                    "APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05"),
            new Caso("N04", "2026-09-21;F;0;F;2;SEM_RESERVA",
                    "NEGADO / LEITOR_INATIVO / —"),
            new Caso("N05", "2026-09-21;V;1;V;2;SEM_RESERVA",
                    "NEGADO / EMPRESTIMO_ATRASADO / —"),
            new Caso("N06", "2026-09-21;V;3;F;2;SEM_RESERVA",
                    "NEGADO / LIMITE_ATINGIDO / —"),
            new Caso("N07", "2026-09-21;V;0;F;0;SEM_RESERVA",
                    "NEGADO / SEM_EXEMPLAR / —"),
            new Caso("N08", "2026-09-21;V;0;F;1;DE_OUTRO_LEITOR",
                    "NEGADO / RESERVADO_PARA_OUTRO / —"),
            new Caso("N09", "2026-09-21;F;3;V;0;DE_OUTRO_LEITOR",
                    "NEGADO / LEITOR_INATIVO / —"),
            new Caso("N10", "2026-09-21;V;0;V;0;DE_OUTRO_LEITOR",
                    "NEGADO / EMPRESTIMO_ATRASADO / —"),
            new Caso("L01", "2026-09-21;V;2;F;1;SEM_RESERVA",
                    "APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05"),
            new Caso("L02", "2026-09-21;V;0;F;0;DO_PROPRIO_LEITOR",
                    "NEGADO / SEM_EXEMPLAR / —"),
            new Caso("L03", "2026-12-25;V;0;F;1;SEM_RESERVA",
                    "APROVADO / EMPRESTIMO_PERMITIDO / 2027-01-08"),
            new Caso("L04", "2028-02-29;V;0;F;1;SEM_RESERVA",
                    "APROVADO / EMPRESTIMO_PERMITIDO / 2028-03-14"),
            new Caso("L05", "2026-09-21;V;0;F;999;SEM_RESERVA",
                    "APROVADO / EMPRESTIMO_PERMITIDO / 2026-10-05"),
            new Caso("I01", "2026-02-29;V;0;F;1;SEM_RESERVA",
                    "ENTRADA_INVALIDA / DADOS_INVALIDOS / —"),
            new Caso("I02", "2026-09-21;V;-1;F;1;SEM_RESERVA",
                    "ENTRADA_INVALIDA / DADOS_INVALIDOS / —"),
            new Caso("I03", "2026-09-21;V;0;F;1;RESERVA_DESCONHECIDA",
                    "ENTRADA_INVALIDA / DADOS_INVALIDOS / —"),
            new Caso("I04", "2026-09-21;F;0;F;1000;SEM_RESERVA",
                    "ENTRADA_INVALIDA / DADOS_INVALIDOS / —")
    );

    // Funcao pura: transforma um caso em um relato de verificacao.
    private static String verificar(Caso caso) {
        final String obtido = PROCESSAR.apply(caso.entrada());

        return "[" + caso.id() + "] "
                + (obtido.equals(caso.esperado()) ? "PASSOU" : "FALHOU")
                + " - " + obtido;
    }

    // Funcao impura: imprime no console.
    private static void executarTestes() {
        CASOS.stream()
                .map(Main::verificar)
                .forEach(System.out::println);

        final long aprovados = CASOS.stream()
                .filter(caso -> PROCESSAR.apply(caso.entrada())
                        .equals(caso.esperado()))
                .count();

        System.out.println("\nResultado: " + aprovados
                + " de " + CASOS.size() + " testes passaram.");
    }

    // Funcao impura: concentra a interacao com o usuario.
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== EMPRESTIMO FUNCIONAL ===");
            System.out.println("1 - Executar os 19 testes");
            System.out.println("2 - Avaliar uma solicitacao");
            System.out.print("Escolha: ");

            if (!scanner.hasNextLine()) {
                return;
            }

            final String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> executarTestes();

                case "2" -> {
                    System.out.println(
                            "Informe os seis campos separados por ponto e virgula:");
                    System.out.println(
                            "data; leitor_ativo; emprestimos_ativos; "
                            + "possui_atraso; exemplares_disponiveis; reserva");
                    System.out.println("Use V ou F nos campos booleanos.");
                    System.out.println(
                            "Exemplo: 2026-09-21;V;0;F;2;SEM_RESERVA");

                    if (scanner.hasNextLine()) {
                        System.out.println(PROCESSAR.apply(scanner.nextLine()));
                    }
                }

                default -> System.out.println("Opcao invalida.");
            }
        }
    }
}