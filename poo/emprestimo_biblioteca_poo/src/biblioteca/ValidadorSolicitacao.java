package biblioteca;

public class ValidadorSolicitacao {

    public boolean possuiDadosInvalidos(SolicitacaoEmprestimo solicitacao) {
        if (solicitacao == null) {
            return true;
        }

        if (solicitacao.getDataSolicitacao() == null) {
            return true;
        }

        if (solicitacao.getEmprestimosAtivos() < 0
                || solicitacao.getEmprestimosAtivos() > 3) {
            return true;
        }

        if (solicitacao.getExemplaresDisponiveis() < 0) {
            return true;
        }

        if (solicitacao.getSituacaoReserva() == null) {
            return true;
        }

        return false;
    }
}