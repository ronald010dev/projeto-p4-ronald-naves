package biblioteca;

public class RegraAtraso implements RegraEmprestimo {

    @Override
    public MotivoEmprestimo verificar(SolicitacaoEmprestimo solicitacao) {
        if (solicitacao.isPossuiAtraso()) {
            return MotivoEmprestimo.EMPRESTIMO_ATRASADO;
        }

        return null;
    }
}