package biblioteca;

public class RegraDisponibilidade implements RegraEmprestimo {

    @Override
    public MotivoEmprestimo verificar(SolicitacaoEmprestimo solicitacao) {
        if (solicitacao.getExemplaresDisponiveis() == 0) {
            return MotivoEmprestimo.SEM_EXEMPLAR;
        }

        return null;
    }
}