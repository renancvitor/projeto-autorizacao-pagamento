package Servicoes;

import java.sql.Timestamp;

import java.util.Date;

public class Solicitacao {
    private int id;
    private String fornecedor;
    private String descricao;
    private Timestamp dataCriacao; // Alterado para Timestamp
    private Date dataPagamento; // Alterado para Date
    private String formaPagamento;
    private int parcelas;
    private double valorParcelas;
    private double valorTotal;
    private int idUsuario;

    public Solicitacao(int id, String fornecedor, String descricao, Timestamp dataCriacao, Date dataPagamento, String formaPagamento, int parcelas, double valorParcelas, double valorTotal, int idUsuario) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataPagamento = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.parcelas = parcelas;
        this.valorParcelas = valorParcelas;
        this.valorTotal = valorTotal;
        this.idUsuario = idUsuario;
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

    public Timestamp getDataCriacao() {
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

    public double getValorParcelas() {
        return valorParcelas;
    }

    public double getValorTotal() {
        return valorTotal;
    }
}