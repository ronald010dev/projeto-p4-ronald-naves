package biblioteca;

public class RegraLeitorAtivo implements RegraEmprestimo {

    @Override
    public MotivoEmprestimo verificar(SolicitacaoEmprestimo solicitacao) {
        if (!solicitacao.isLeitorAtivo()) {
            return MotivoEmprestimo.LEITOR_INATIVO;
        }

        return null;
    }
}