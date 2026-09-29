package biblioteca;

public class RegraReserva implements RegraEmprestimo {

    @Override
    public MotivoEmprestimo verificar(SolicitacaoEmprestimo solicitacao) {
        if (solicitacao.getSituacaoReserva()
                == SituacaoReserva.DE_OUTRO_LEITOR) {

            return MotivoEmprestimo.RESERVADO_PARA_OUTRO;
        }

        return null;
    }
}