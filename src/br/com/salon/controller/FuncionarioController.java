package br.com.salon.controller;

import br.com.salon.model.FuncionarioModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class FuncionarioController {

    private final Connection conn;
    private PreparedStatement ps;

    public FuncionarioController() {
        conn = ConnectionController.connection();
    }

    public boolean insert(FuncionarioModel modelFuncionario) {
        try {
            ps = conn.prepareStatement(
                    "insert into funcionarios values(?,?,?,?,?,?,?)"
            );
            ps.setInt(1, modelFuncionario.getId());
            ps.setString(2, modelFuncionario.getNome());
            ps.setString(3, modelFuncionario.getCpf());
            ps.setString(4, modelFuncionario.getCnpj());
            ps.setString(5, modelFuncionario.getTelefone());
            ps.setString(6, modelFuncionario.getOcupacao());
            ps.setString(7, modelFuncionario.getComplemento());
            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR FUNCIONÁRIO" + ex);
            return false;
        }
    }
// Não implementado. Apenas para referência didático
    public ArrayList<FuncionarioModel> selectAll() {
        ArrayList<FuncionarioModel> list = new ArrayList<>();

        try {
            ps = conn.prepareStatement("select * from funcionarios"
            );
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FuncionarioModel s = new FuncionarioModel( //String item, int caixa, double volume
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7)
                );
                list.add(s);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR FUNCIONÁRIOS...\n" + e);
        }
        return list;
    }

    public boolean update(FuncionarioModel modelFuncionario, int id) {

        try {
            ps = conn.prepareStatement(
                    "update funcionarios set nome = ?, cpf= ?, cnpj= ?, telefone= ?,"
                    + "ocupacao= ?, complemento= ? where id = ?"
            );
            ps.setString(1, modelFuncionario.getNome());
            ps.setString(2, modelFuncionario.getCpf());
            ps.setString(3, modelFuncionario.getCnpj());
            ps.setString(4, modelFuncionario.getTelefone());
            ps.setString(5, modelFuncionario.getOcupacao());
            ps.setString(6, modelFuncionario.getComplemento());
            ps.setInt(7, id);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR FUNCIONÁRIO" + ex);
            return false;
        }
    }

    public boolean deleteOne(int id) {

        try {
            ps = conn.prepareStatement(
                    "delete from funcionarios where id = ?"
            );
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM DELETAR FUNCIONÁRIO" + e);
            return false;
        }
    }

}
