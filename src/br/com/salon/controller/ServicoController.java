package br.com.salon.controller;

import br.com.salon.model.ServicoModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ServicoController {

    private final Connection conn;
    private PreparedStatement ps;

    public ServicoController() {
        conn = ConnectionController.connection();
    }

    public boolean insert(ServicoModel modelServico) {
        try {
            ps = conn.prepareStatement(
                    "insert into servicos values(?,?,?)"
            );
            ps.setInt(1, modelServico.getId());
            ps.setString(2, modelServico.getServico());
            ps.setDouble(3, modelServico.getValor());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR SERVIÇO" + ex);
            return false;
        }
    }

    public boolean update(ServicoModel modelServico, int id) {

        try {
            ps = conn.prepareStatement(
                    "update servicos set servico = ?, valor= ? where id = ?"
            );
            ps.setString(1, modelServico.getServico());
            ps.setDouble(2, modelServico.getValor());
            ps.setInt(3, id);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR SERVIÇO" + ex);
            return false;
        }
    }

    public boolean deleteOne(int id) {

        try {
            ps = conn.prepareStatement(
                    "delete from servicos where id = ?"
            );
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM EXCLUIR SERVIÇO" + e);
            return false;
        }
    }

}
