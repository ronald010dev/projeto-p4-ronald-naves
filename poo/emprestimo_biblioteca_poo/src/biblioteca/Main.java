package biblioteca;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(
                "=== SISTEMA DE EMPRESTIMO DA BIBLIOTECA ===");
        System.out.println();
        System.out.println("1 - Avaliar uma solicitacao");
        System.out.println(
                "2 - Executar os testes da Etapa 02");
        System.out.print("Escolha: ");

        String opcao = scanner.nextLine();

        switch (opcao) {
            case "1":
                executarAvaliacaoManual();
                break;

            case "2":
                new TesteSistema().executarTodos();
                break;

            default:
                System.out.println("Opcao invalida.");
        }

        scanner.close();
    }

    private static void executarAvaliacaoManual() {
        try {
            System.out.println();
            System.out.print(
                    "Data da solicitacao (AAAA-MM-DD): ");

            LocalDate data = LocalDate.parse(
                    scanner.nextLine());

            boolean leitorAtivo = lerBooleano(
                    "Leitor ativo? (1 - Sim / 0 - Nao): ");

            int emprestimosAtivos = lerInteiro(
                    "Quantidade de emprestimos ativos: ");

            boolean possuiAtraso = lerBooleano(
                    "Possui emprestimo atrasado? "
                    + "(1 - Sim / 0 - Nao): ");

            int exemplaresDisponiveis = lerInteiro(
                    "Quantidade de exemplares disponiveis: ");

            SituacaoReserva reserva = lerReserva();

            SolicitacaoEmprestimo solicitacao =
                    new SolicitacaoEmprestimo(
                            data,
                            leitorAtivo,
                            emprestimosAtivos,
                            possuiAtraso,
                            exemplaresDisponiveis,
                            reserva);

            AvaliadorEmprestimo avaliador =
                    new AvaliadorEmprestimo();

            ResultadoEmprestimo resultado =
                    avaliador.avaliar(solicitacao);

            exibirResultado(resultado);

        } catch (RuntimeException erro) {
            System.out.println();
            System.out.println(
                    "ENTRADA_INVALIDA / DADOS_INVALIDOS");
        }
    }

    private static boolean lerBooleano(String mensagem) {
        System.out.print(mensagem);
        String valor = scanner.nextLine();

        if (valor.equals("1")) {
            return true;
        }

        if (valor.equals("0")) {
            return false;
        }

        throw new IllegalArgumentException();
    }

    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        return Integer.parseInt(scanner.nextLine());
    }

    private static SituacaoReserva lerReserva() {
        System.out.println();
        System.out.println("Situacao da reserva:");
        System.out.println("0 - Sem reserva");
        System.out.println("1 - Reserva do proprio leitor");
        System.out.println("2 - Reserva de outro leitor");
        System.out.print("Escolha: ");

        String opcao = scanner.nextLine();

        return switch (opcao) {
            case "0" -> SituacaoReserva.SEM_RESERVA;
            case "1" -> SituacaoReserva.DO_PROPRIO_LEITOR;
            case "2" -> SituacaoReserva.DE_OUTRO_LEITOR;
            default -> throw new IllegalArgumentException();
        };
    }

    private static void exibirResultado(
            ResultadoEmprestimo resultado) {

        System.out.println();
        System.out.println("=== RESULTADO ===");
        System.out.println(
                "Status: " + resultado.getStatus());
        System.out.println(
                "Motivo: " + resultado.getMotivo());

        if (resultado.foiAprovado()) {
            System.out.println(
                    "Data prevista para devolucao: "
                    + resultado.getDataDevolucao());
        }
    }
}