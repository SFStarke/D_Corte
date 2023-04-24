package br.com.salon.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConnectionController {

    
    public static Connection connection() {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/salon", "root", ""
            );
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO NA CONEXÃO COM BANCO DE DADOS...\n" + e);
            return null;
        }
    }
   // Esta método estabelece conecção ao DB para a classe view. 
    public Connection connToView() {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/salon", "root", ""
            );
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
           JOptionPane.showMessageDialog(null, "ERRO NA CONEXÃO COM BANCO DE DADOS...\n" + e);
            return null;
        }

    }
}
