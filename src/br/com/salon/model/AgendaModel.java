package br.com.salon.model;

public class AgendaModel {

    private int id;
    private String cabeleireiro;
    private String manicure;
    private String cliente;
    private String servico;
    private String valor_total;
    private String data;
    private String horario;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCabeleireiro() {
        return cabeleireiro;
    }

    public void setCabeleireiro(String cabeleireiro) {
        this.cabeleireiro = cabeleireiro;
    }

    public String getManicure() {
        return manicure;
    }

    public void setManicure(String manicure) {
        this.manicure = manicure;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getValor_total() {
        return valor_total;
    }

    public void setValor_total(String valor_total) {
        this.valor_total = valor_total;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

}
