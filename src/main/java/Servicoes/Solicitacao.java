package Servicoes;

import Entities.Usuario;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Solicitacao {
    private Usuario usuario;
    private DoubleProperty valor;
    private StringProperty descricao;
    private ObjectProperty<Date> data;
    private StringProperty tipo;
    private StringProperty status;

    public Solicitacao(Usuario usuario, double valor, String descricao, String tipo, String status, Date data) {
        this.usuario = usuario;
        this.valor = new SimpleDoubleProperty(valor);
        this.descricao = new SimpleStringProperty(descricao);
        this.data = new SimpleObjectProperty<>(data);
        this.tipo = new SimpleStringProperty(tipo);
        this.status = new SimpleStringProperty(status);
    }

    public Solicitacao(Usuario usuario, double valor, String descricao, StatusSolicitacao status, Date date, String data, String tipo) {
    }

    public Solicitacao(Usuario usuario, double valor, String descricao, StatusSolicitacao status, java.sql.Date data, String tipo) {
    }

    // Getters e Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getValor() {
        return valor.get();
    }

    public DoubleProperty valorProperty() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor.set(valor);
    }

    public String getDescricao() {
        return descricao.get();
    }

    public StringProperty descricaoProperty() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao.set(descricao);
    }

    public Date getData() {
        return data.get();
    }

    public ObjectProperty<Date> dataProperty() {
        return data;
    }

    public void setData(Date data) {
        this.data.set(data);
    }

    public String getTipo() {
        return tipo.get();
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
