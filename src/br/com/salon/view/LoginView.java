package br.com.salon.view;

import br.com.salon.controller.ConnectionController;
import br.com.salon.controller.LoginController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginView extends javax.swing.JFrame {

    String login = "";
    String password = "";
    int setSwitch = 0;

    ConnectionController connController = new ConnectionController();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    LoginController loginController = new LoginController();
    GestorAdministrativoView gestorAdmin = new GestorAdministrativoView();

    // CONSTRUCTOR
    public LoginView() {
        initComponents();
        conn = connController.connToView();

        if (conn != null) {
            lblStatus.setText(
                    "<html><strong><font color= blue>"
                    + "Informe Login, Senha"
                    + "</html>"
            );
        } else {
            lblStatus.setText(
                    "<html><strong><font color= red>"
                    + "Conexão Indisponível"
                    + "</html>"
            );
            txtLogin.setEnabled(false);
            pwPassword.setEnabled(false);
        }
    }

    public void limpar() {
        txtLogin.setText("");
        pwPassword.setText("");
        login = "";
        password = "";
        setSwitch = 0;
    }

// Considerações Futuras =>  L I M I T E  D E  C A R A C T E R E S  E  T E N T A T I V A S
    /*   Verefica se "login & Senha" são verdadeiros para então abrir janela de
     * gerenciamento de Funcionário, Serviço e Seguramça*/
    public void checkEnablePassword() {

        try {
            ps = conn.prepareStatement(
                    "select* from seguranca where login= ? and senha= ?;"
            );
            ps.setString(1, login);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {

                if (setSwitch == 1) {
                    gestorAdmin.setVisible(true);
                    limpar();
                    this.dispose(); //Torna Frame do "LoginView" indisponível após condição verdadeiro.
                } else if (setSwitch == 2) {
                    AgendaView.jcbDeleteGroup.setEnabled(true);
                    limpar();
                    this.dispose(); //Torna Frame do "LoginView" indisponível após condição verdadeiro.
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }
    /* Conexão não pode ser encerrada, pois impediria novos tentativas
     *    para acessar ao gestor administrativo e exclusão de grupo */
//                try { //Encerrar conexão com database.
//                    conn.close();
//                } catch (SQLException ex) {
//                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
//                }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        pwPassword = new javax.swing.JPasswordField();
        txtLogin = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();

        jLabel4.setText("jLabel4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login_Salon");
        setResizable(false);

        pwPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pwPasswordKeyReleased(evt);
            }
        });

        txtLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLoginKeyReleased(evt);
            }
        });

        jLabel2.setText("Login:");

        jLabel3.setText("Senha:");

        lblStatus.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Informa condição da conexão com banco de dados");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pwPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pwPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(27, 27, 27))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtLoginKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLoginKeyReleased
        /* LIBERA BOTÃO DE COMFIRMAR QUANDO "checkEnablePassword();" VERDADEIRO
         * bfMouse em txtNomeCliente / Eventos / Key / KeyReleased */

        login = txtLogin.getText();
        checkEnablePassword();

    }//GEN-LAST:event_txtLoginKeyReleased

    private void pwPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwPasswordKeyReleased
        /* LIBERA BOTÃO DE COMFIRMAR QUANDO "checkEnablePassword();" VERDADEIRO
         * bfMouse em txtNomeCliente / Eventos / Key / KeyReleased */

        password = new String(pwPassword.getPassword());
        checkEnablePassword();

    }//GEN-LAST:event_pwPasswordKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPasswordField pwPassword;
    private javax.swing.JTextField txtLogin;
    // End of variables declaration//GEN-END:variables
}
