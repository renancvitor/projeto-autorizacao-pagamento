package Servicoes;

import Entities.Usuario;

import java.util.Date;

public class Solicitacao {

    private Usuario solicitante;
    private Double valor;
    private String descricao;
    private StatusSolicitacao status;
    private Date dataSolicitacao;

    public Solicitacao(Usuario solicitante, Double valor, String descricao, StatusSolicitacao status, Date dataSolicitacao) {
        this.solicitante = solicitante;
        this.valor = valor;
        this.descricao = descricao;
        this.status = status;
        this.dataSolicitacao = dataSolicitacao;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public Double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public Date getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    public void setDataSolicitacao(Date dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

}
