package br.com.salon.controller;

import br.com.salon.model.LoginModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class LoginController {

    private final Connection conn;
    private PreparedStatement ps;

    public LoginController() { //CONSTRUCTOR
        conn = ConnectionController.connection();
    }

    public boolean insert(LoginModel item) {
        try {
            ps = conn.prepareStatement(
                    "insert into seguranca values(?,?,?,?)"
            );
            ps.setInt(1, item.getId());
            ps.setString(2, item.getUsuario());
            ps.setString(3, item.getLogin());
            ps.setString(4, item.getSenha());
            ps.execute();
            return true;
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "LOGIN JÁ USADO, TENTE OUTRO...\n");
            return false;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR USUÁRIO\n" + ex);
            System.out.println(ex);
            return false;
        }
    }

    public boolean update(LoginModel log, int id) {

        try {
            ps = conn.prepareStatement(
                    "update seguranca set usuario = ?, login= ?, senha= ? where id = ?"
            );
            ps.setString(1, log.getUsuario());
            ps.setString(2, log.getLogin());
            ps.setString(3, log.getSenha());
            ps.setInt(4, id);
            ps.execute();
            return true;
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "LOGIN JÁ USADO, TENTE OUTRO...\n");
            return false;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR USUÁRIO\n" + ex);
            return false;
        }
    }

    public boolean deleteOne(int id) {

        try {
            ps = conn.prepareStatement(
                    "delete from seguranca where id = ?"
            );
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM EXCLUIR USUÁRIO\n" + e);
            return false;
        }
    }
}
