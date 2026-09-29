package biblioteca;

import java.time.LocalDate;

public class ResultadoEmprestimo {

    private final StatusEmprestimo status;
    private final MotivoEmprestimo motivo;
    private final LocalDate dataDevolucao;

    private ResultadoEmprestimo(
            StatusEmprestimo status,
            MotivoEmprestimo motivo,
            LocalDate dataDevolucao) {

        this.status = status;
        this.motivo = motivo;
        this.dataDevolucao = dataDevolucao;
    }

    public static ResultadoEmprestimo aprovado(LocalDate dataDevolucao) {
        return new ResultadoEmprestimo(
                StatusEmprestimo.APROVADO,
                MotivoEmprestimo.EMPRESTIMO_PERMITIDO,
                dataDevolucao);
    }

    public static ResultadoEmprestimo negado(MotivoEmprestimo motivo) {
        return new ResultadoEmprestimo(
                StatusEmprestimo.NEGADO,
                motivo,
                null);
    }

    public static ResultadoEmprestimo entradaInvalida() {
        return new ResultadoEmprestimo(
                StatusEmprestimo.ENTRADA_INVALIDA,
                MotivoEmprestimo.DADOS_INVALIDOS,
                null);
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public MotivoEmprestimo getMotivo() {
        return motivo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public boolean foiAprovado() {
        return status == StatusEmprestimo.APROVADO;
    }
}