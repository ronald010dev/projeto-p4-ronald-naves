package biblioteca;

import java.time.LocalDate;

public class TesteSistema {

    private final AvaliadorEmprestimo avaliador;
    private int testesExecutados;
    private int testesAprovados;

    public TesteSistema() {
        avaliador = new AvaliadorEmprestimo();
    }

    public void executarTodos() {
        System.out.println("=== TESTES AUTOMATICOS DA ETAPA 02 ===");
        System.out.println();

        executarCasosNormais();
        executarCasosLimite();
        executarCasosInvalidos();

        System.out.println();
        System.out.printf(
                "Resultado final: %d de %d testes passaram.%n",
                testesAprovados,
                testesExecutados);
    }

    private void executarCasosNormais() {
        testar(
                "N01",
                criarSolicitacao("2026-09-21", true, 0, false, 2,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                LocalDate.of(2026, 10, 5));

        testar(
                "N02",
                criarSolicitacao("2026-09-21", true, 1, false, 4,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                LocalDate.of(2026, 10, 5));

        testar(
                "N03",
                criarSolicitacao("2026-09-21", true, 0, false, 1,
                        SituacaoReserva.DO_PROPRIO_LEITOR),
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                LocalDate.of(2026, 10, 5));

        testar(
                "N04",
                criarSolicitacao("2026-09-21", false, 0, false, 2,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.NEGADO,
                MotivoEmprestimo.LEITOR_INATIVO,
                null);

        testar(
                "N05",
                criarSolicitacao("2026-09-21", true, 1, true, 2,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.NEGADO,
                MotivoEmprestimo.EMPRESTIMO_ATRASADO,
                null);

        testar(
                "N06",
                criarSolicitacao("2026-09-21", true, 3, false, 2,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.NEGADO,
                MotivoEmprestimo.LIMITE_ATINGIDO,
                null);

        testar(
                "N07",
                criarSolicitacao("2026-09-21", true, 0, false, 0,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.NEGADO,
                MotivoEmprestimo.SEM_EXEMPLAR,
                null);

        testar(
                "N08",
                criarSolicitacao("2026-09-21", true, 0, false, 1,
                        SituacaoReserva.DE_OUTRO_LEITOR),
                StatusEmprestimo.NEGADO,
                MotivoEmprestimo.RESERVADO_PARA_OUTRO,
                null);

        testar(
                "N09",
                criarSolicitacao("2026-09-21", false, 1, true, 2,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.NEGADO,
                MotivoEmprestimo.LEITOR_INATIVO,
                null);

        testar(
                "N10",
                criarSolicitacao("2026-09-21", true, 1, true, 0,
                        SituacaoReserva.DE_OUTRO_LEITOR),
                StatusEmprestimo.NEGADO,
                MotivoEmprestimo.EMPRESTIMO_ATRASADO,
                null);
    }

    private void executarCasosLimite() {
        testar(
                "L01",
                criarSolicitacao("2026-09-21", true, 2, false, 1,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                LocalDate.of(2026, 10, 5));

        testar(
                "L02",
                criarSolicitacao("2026-09-21", true, 0, false, 1,
                        SituacaoReserva.DO_PROPRIO_LEITOR),
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                LocalDate.of(2026, 10, 5));

        testar(
                "L03",
                criarSolicitacao("2026-12-25", true, 0, false, 1,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                LocalDate.of(2027, 1, 8));

        testar(
                "L04",
                criarSolicitacao("2028-02-29", true, 0, false, 1,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                LocalDate.of(2028, 3, 14));

        testar(
                "L05",
                criarSolicitacao("2026-09-21", true, 0, false, 100,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                LocalDate.of(2026, 10, 5));
    }

    private void executarCasosInvalidos() {
        testar(
                "I01",
                new SolicitacaoEmprestimo(
                        null, true, 0, false, 1,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.ENTRADA_INVALIDA,
                MotivoEmprestimo.DADOS_INVALIDOS,
                null);

        testar(
                "I02",
                criarSolicitacao("2026-09-21", true, -1, false, 1,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.ENTRADA_INVALIDA,
                MotivoEmprestimo.DADOS_INVALIDOS,
                null);

        testar(
                "I03",
                criarSolicitacao("2026-09-21", true, 0, false, 1,
                        null),
                StatusEmprestimo.ENTRADA_INVALIDA,
                MotivoEmprestimo.DADOS_INVALIDOS,
                null);

        testar(
                "I04",
                new SolicitacaoEmprestimo(
                        null, false, 0, false, 1,
                        SituacaoReserva.SEM_RESERVA),
                StatusEmprestimo.ENTRADA_INVALIDA,
                MotivoEmprestimo.DADOS_INVALIDOS,
                null);
    }

    private SolicitacaoEmprestimo criarSolicitacao(
            String data,
            boolean leitorAtivo,
            int emprestimosAtivos,
            boolean possuiAtraso,
            int exemplaresDisponiveis,
            SituacaoReserva reserva) {

        return new SolicitacaoEmprestimo(
                LocalDate.parse(data),
                leitorAtivo,
                emprestimosAtivos,
                possuiAtraso,
                exemplaresDisponiveis,
                reserva);
    }

    private void testar(
            String identificador,
            SolicitacaoEmprestimo solicitacao,
            StatusEmprestimo statusEsperado,
            MotivoEmprestimo motivoEsperado,
            LocalDate dataEsperada) {

        testesExecutados++;

        ResultadoEmprestimo resultado =
                avaliador.avaliar(solicitacao);

        boolean passou =
                resultado.getStatus() == statusEsperado
                && resultado.getMotivo() == motivoEsperado
                && datasIguais(
                        resultado.getDataDevolucao(),
                        dataEsperada);

        if (passou) {
            testesAprovados++;
        }

        System.out.printf(
                "[%s] %s - %s / %s",
                identificador,
                passou ? "PASSOU" : "FALHOU",
                resultado.getStatus(),
                resultado.getMotivo());

        if (resultado.getDataDevolucao() != null) {
            System.out.print(
                    " / " + resultado.getDataDevolucao());
        }

        System.out.println();
    }

    private boolean datasIguais(
            LocalDate primeira,
            LocalDate segunda) {

        if (primeira == null && segunda == null) {
            return true;
        }

        if (primeira == null || segunda == null) {
            return false;
        }

        return primeira.equals(segunda);
    }
}