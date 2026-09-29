package biblioteca;

import java.util.List;

public class AvaliadorEmprestimo {

    private final ValidadorSolicitacao validador;
    private final List<RegraEmprestimo> regras;

    public AvaliadorEmprestimo() {
        this.validador = new ValidadorSolicitacao();

        this.regras = List.of(
                new RegraLeitorAtivo(),
                new RegraAtraso(),
                new RegraLimiteEmprestimos(),
                new RegraDisponibilidade(),
                new RegraReserva()
        );
    }

    public ResultadoEmprestimo avaliar(
            SolicitacaoEmprestimo solicitacao) {

        if (validador.possuiDadosInvalidos(solicitacao)) {
            return ResultadoEmprestimo.entradaInvalida();
        }

        for (RegraEmprestimo regra : regras) {
            MotivoEmprestimo motivo = regra.verificar(solicitacao);

            if (motivo != null) {
                return ResultadoEmprestimo.negado(motivo);
            }
        }

        return ResultadoEmprestimo.aprovado(
                solicitacao.getDataSolicitacao().plusDays(14));
    }
}