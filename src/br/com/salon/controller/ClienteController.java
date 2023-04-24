package br.com.salon.controller;

import br.com.salon.model.ClienteModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ClienteController {

    private final Connection conn;
    private PreparedStatement ps;

    public ClienteController() {
        conn = ConnectionController.connection();
    }

    public boolean insert(ClienteModel item) {
        try {
            ps = conn.prepareStatement(
                    "insert into clientes values(?,?,?,?)"
            );
            ps.setInt(1, item.getId());
            ps.setString(2, item.getNome());
            ps.setString(3, item.getTelefone());
            ps.setString(4, item.getComplemento());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR CLIENTE\n" + ex);
            return false;
        }
    }

    public boolean update(ClienteModel modelCliente, int id) {

        try {
            ps = conn.prepareStatement(
                    "update clientes set nome = ?, telefone= ?, complemento= ? where id = ?"
            );
            ps.setString(1, modelCliente.getNome());
            ps.setString(2, modelCliente.getTelefone());
            ps.setString(3, modelCliente.getComplemento());
            ps.setInt(4, id);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR CLIENTE\n" + ex);
            return false;
        }
    }

    public boolean deleteOne(int id) {

        try {
            ps = conn.prepareStatement(
                    "delete from clientes where id = ?"
            );
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM DELETAR CLIENTE\n" + e);
            return false;
        }
    }

}
