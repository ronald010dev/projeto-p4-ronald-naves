package biblioteca;

import java.time.LocalDate;

public class SolicitacaoEmprestimo {

    private final LocalDate dataSolicitacao;
    private final boolean leitorAtivo;
    private final int emprestimosAtivos;
    private final boolean possuiAtraso;
    private final int exemplaresDisponiveis;
    private final SituacaoReserva situacaoReserva;

    public SolicitacaoEmprestimo(
            LocalDate dataSolicitacao,
            boolean leitorAtivo,
            int emprestimosAtivos,
            boolean possuiAtraso,
            int exemplaresDisponiveis,
            SituacaoReserva situacaoReserva) {

        this.dataSolicitacao = dataSolicitacao;
        this.leitorAtivo = leitorAtivo;
        this.emprestimosAtivos = emprestimosAtivos;
        this.possuiAtraso = possuiAtraso;
        this.exemplaresDisponiveis = exemplaresDisponiveis;
        this.situacaoReserva = situacaoReserva;
    }

    public LocalDate getDataSolicitacao() {
        return dataSolicitacao;
    }

    public boolean isLeitorAtivo() {
        return leitorAtivo;
    }

    public int getEmprestimosAtivos() {
        return emprestimosAtivos;
    }

    public boolean isPossuiAtraso() {
        return possuiAtraso;
    }

    public int getExemplaresDisponiveis() {
        return exemplaresDisponiveis;
    }

    public SituacaoReserva getSituacaoReserva() {
        return situacaoReserva;
    }
}