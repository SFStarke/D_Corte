package br.com.salon.controller;

import br.com.salon.model.AgendaModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class AgendaController {

    private final Connection conn;
    private PreparedStatement ps;

    public AgendaController() {
        conn = ConnectionController.connection();
    }

    public boolean insert(AgendaModel item) {
        try {
            ps = conn.prepareStatement(
                    "insert into agendas values(?,?,?,?,?,?,?,?)"
            );
            ps.setInt(1, item.getId());
            ps.setString(2, item.getCabeleireiro());
            ps.setString(3, item.getManicure());
            ps.setString(4, item.getCliente());
            ps.setString(5, item.getServico());
            ps.setString(6, item.getValor_total());
            ps.setString(7, item.getData());
            ps.setString(8, item.getHorario());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR AGENDAMENTO\n" + ex);
            return false;
        }
    }

    public boolean update(AgendaModel modelAgenda, int id) {

        try {
            ps = conn.prepareStatement(
            "update agendas set cabeleireiro = ?, manicure= ?, cliente= ?,"
          + "servico= ?, valor_total= ?, data= ?, horario= ? where id = ?"
            );
            ps.setString(1, modelAgenda.getCabeleireiro());
            ps.setString(2, modelAgenda.getManicure());
            ps.setString(3, modelAgenda.getCliente());
            ps.setString(4, modelAgenda.getServico());
            ps.setString(5, modelAgenda.getValor_total());
            ps.setString(6, modelAgenda.getData());
            ps.setString(7, modelAgenda.getHorario());
            ps.setInt(8, id);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR AGENDA\n" + ex);
            return false;
        }
    }

    public boolean deleteOne(int id) {

        try {
            ps = conn.prepareStatement(
                    "delete from agendas where id = ?"
            );
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM EXCLUIR AGENDAMENTO:\n" + e);
            return false;
        }
    }

    public boolean deleteSubject(String sqlSubject) { 
        try {
            ps = conn.prepareStatement(sqlSubject);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM EXCLUIR GRUPO DE AGENDAMENTO:\n" + e);
            return false;
        }
    }
    
}
