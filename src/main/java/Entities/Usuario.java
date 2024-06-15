package Entities;

public class Usuario {

    public String name;
    public String setor;
    public String username;
    public String password;

    public Usuario() {
    }

    public Usuario(String name, String setor, String username, String password) {
        this.name = name;
        this.setor = setor;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getSetor() {
        return setor;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
