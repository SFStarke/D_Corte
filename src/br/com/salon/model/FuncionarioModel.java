package br.com.salon.model;

public class FuncionarioModel {

    private int id;
    private String nome;
    private String cpf;
    private String cnpj;
    private String telefone;
    private String ocupacao;
    private String complemento;

    public FuncionarioModel() {
    }

    public FuncionarioModel(int id, String nome, String cpf, String cnpj,
            String telefone, String ocupacao, String complemento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.ocupacao = ocupacao;
        this.complemento = complemento;
    }

    public FuncionarioModel(String nome, String cpf, String cnpj,
            String telefone, String ocupacao, String complemento) {
        this.nome = nome;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.ocupacao = ocupacao;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(String ocupacao) {
        this.ocupacao = ocupacao;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public String toString() {
        return nome + ". ";
    }
    
    

}
