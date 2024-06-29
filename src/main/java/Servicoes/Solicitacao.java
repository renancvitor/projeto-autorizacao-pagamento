package Servicoes;

import java.util.Date;

public class Solicitacao {
    private int id;
    private String fornecedor;
    private String descricao;
    private Date dataCriacao;
    private Date dataPagamento;
    private String formaPagamento;
    private int parcelas;
    private double valorParcela;
    private double valorTotal;
    private int idUsuario;
    private StatusSolicitacao status; // Campo para armazenar o status da solicitação

    public Solicitacao(int id, String fornecedor, String descricao, Date dataCriacao, Date dataPagamento,
                       String formaPagamento, int parcelas, double valorParcela, double valorTotal, int idUsuario) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.parcelas = parcelas;
        this.valorParcela = valorParcela;
        this.valorTotal = valorTotal;
        this.idUsuario = idUsuario;
        this.status = StatusSolicitacao.PENDENTE; // Inicializa como pendente por padrão
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public int getParcelas() {
        return parcelas;
    }

    public double getValorParcela() {
        return valorParcela;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public void setValorParcela(double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    // Métodos para verificar e manipular o status
    public void setStatusByString(String statusStr) {
        // Converte a string para o enum StatusSolicitacao
        if ("PENDENTE".equalsIgnoreCase(statusStr)) {
            this.status = StatusSolicitacao.PENDENTE;
        } else if ("APROVADA".equalsIgnoreCase(statusStr)) {
            this.status = StatusSolicitacao.APROVADA;
        } else if ("REJEITADA".equalsIgnoreCase(statusStr)) {
            this.status = StatusSolicitacao.REJEITADA;
        } else {
            throw new IllegalArgumentException("Status desconhecido: " + statusStr);
        }
    }

    public void aprovarSolicitacao() {
        this.status = StatusSolicitacao.APROVADA;
    }

    public void rejeitarSolicitacao() {
        this.status = StatusSolicitacao.REJEITADA;
    }

    public boolean estaPendente() {
        return this.status == StatusSolicitacao.PENDENTE;
    }
}
