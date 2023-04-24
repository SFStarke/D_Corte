package br.com.salon.model;

public class ClienteModel {

    private int id;
    private String nome;
    private String telefone;
    private String complemento;
    
    public ClienteModel(){}
    
    public ClienteModel(String nome, String telefone){
        this.nome = nome;
        this.telefone = telefone;
    }
    
    public ClienteModel(String nome, String telefone, String complemento){
        this.nome = nome;
        this.telefone = telefone;
        this.complemento = complemento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

}
