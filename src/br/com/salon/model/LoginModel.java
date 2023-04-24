package br.com.salon.model;

public class LoginModel {

    private int id;
    private String usuario;
    private String login;
    private String senha;

    public LoginModel() {
    }

    public LoginModel(String user, String log, String password) {
        this.usuario = user;
        this.login = log;
        this.senha = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
