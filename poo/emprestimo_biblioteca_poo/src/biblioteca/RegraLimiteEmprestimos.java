package biblioteca;

public class RegraLimiteEmprestimos implements RegraEmprestimo {

    private static final int LIMITE_EMPRESTIMOS = 3;

    @Override
    public MotivoEmprestimo verificar(SolicitacaoEmprestimo solicitacao) {
        if (solicitacao.getEmprestimosAtivos() >= LIMITE_EMPRESTIMOS) {
            return MotivoEmprestimo.LIMITE_ATINGIDO;
        }

        return null;
    }
}