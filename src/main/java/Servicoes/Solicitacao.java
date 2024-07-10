package Servicoes;

import java.sql.Timestamp;
import java.util.Date;

public class Solicitacao {
    private int id;
    private String fornecedor;
    private String descricao;
    private Timestamp dataCriacao;
    private Date dataPagamento;
    private String formaPagamento;
//    private int parcelas;
//    private double valorParcelas;
    private double valorTotal;
    private int idUsuario;

    public Solicitacao(int id, String fornecedor, String descricao, Timestamp dataCriacao, Date dataPagamento, String formaPagamento, double valorTotal, int idUsuario) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
//        this.parcelas = parcelas;
//        this.valorParcelas = valorParcelas;
        this.valorTotal = valorTotal;
        this.idUsuario = idUsuario;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public Timestamp getDataCriacao() {
        return dataCriacao;
    }

    public java.sql.Date getDataPagamento() {
        return (java.sql.Date) dataPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

//    public int getParcelas() {
//        return parcelas;
//    }
//
//    public double getValorParcelas() {
//        return valorParcelas;
//    }

    public double getValorTotal() {
        return valorTotal;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataCriacao(Timestamp dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

//    public void setParcelas(int parcelas) {
//        this.parcelas = parcelas;
//    }
//
//    public void setValorParcelas(double valorParcelas) {
//        this.valorParcelas = valorParcelas;
//    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
