package br.com.salon.model;

public class ServicoModel {

    private int id;
    private String servico;
    private double valor;

    public ServicoModel() {
    }

    public ServicoModel(int id, String servico, double valor) {
        this.id = id;
        this.servico = servico;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
