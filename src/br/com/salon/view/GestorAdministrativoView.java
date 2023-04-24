package br.com.salon.view;

import br.com.salon.controller.ConnectionController;
import br.com.salon.controller.FuncionarioController;
import br.com.salon.controller.LoginController;
import br.com.salon.controller.ServicoController;
import br.com.salon.model.FuncionarioModel;
import br.com.salon.model.LoginModel;
import br.com.salon.model.ServicoModel;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GestorAdministrativoView extends javax.swing.JFrame {

    ConnectionController connController = new ConnectionController();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    DefaultTableModel defaultTableFuncionario = new DefaultTableModel();
    FuncionarioController funcionarioController = new FuncionarioController();
    FuncionarioModel funcionarioModel = new FuncionarioModel();
    ArrayList<FuncionarioModel> arrayListFuncionario = new ArrayList();

    DefaultTableModel defaultTableServico = new DefaultTableModel();
    ServicoController servicoController = new ServicoController();
    ServicoModel servicoModel = new ServicoModel();
    ArrayList<ServicoModel> arrayListServico = new ArrayList();

    DefaultTableModel defaultTableLog = new DefaultTableModel();
    LoginController logController = new LoginController();
    LoginModel logModel = new LoginModel();
    ArrayList<LoginModel> arrayListLog = new ArrayList();

    //CONSTRUCTOR
    public GestorAdministrativoView() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);
        conn = connController.connToView();

        defaultTableFuncionario = (DefaultTableModel) jtFuncionario.getModel();
        selectAllFuncionario();
        btnUpdateFuncionario.setEnabled(false);
        btnDeleteFuncionario.setEnabled(false);

        defaultTableServico = (DefaultTableModel) jtServico.getModel();
        selectAllServico();
        btnUpdateServico.setEnabled(false);
        btnDeleteServico.setEnabled(false);

        defaultTableLog = (DefaultTableModel) jtLog.getModel();
        selectAllLog();
        btnUpdateLog.setEnabled(false);
        btnDeleteLog.setEnabled(false);
// Limita largura "MaxWidth" dos campos "ID" e "Valor de Serviço"
        jtServico.getColumnModel().getColumn(0).setMaxWidth(30);
        jtServico.getColumnModel().getColumn(2).setMaxWidth(80);
        jtLog.getColumnModel().getColumn(0).setMaxWidth(30);
        jtFuncionario.getColumnModel().getColumn(0).setMaxWidth(30);

    }

    // Limpa campos: funcionario
    private void cleanFieldFuncionario() {
        txtIdFuncionario.setText("");
        txtNomeFuncionario.setText("");
        txtCpfFuncionario.setText("");
        txtCnpjFuncionario.setText("");
        txtTelefoneFuncionario.setText("");
        jcbOcupacaoFuncionario.setSelectedIndex(0);
        taComplementoFuncionario.setText("");

        btnInsertFuncionario.setEnabled(true);
        btnUpdateFuncionario.setEnabled(false);
        btnDeleteFuncionario.setEnabled(false);
    }

    //Busca específica de funcionario para jTeble.
    private void searchFuncionario() {
        try {

            ps = conn.prepareStatement(
                    "select f.id, f.nome from funcionarios f where f.nome like ?"
            );
            ps.setString(1, txtNomeFuncionario.getText() + "%");
            rs = ps.executeQuery();

            ((DefaultTableModel) jtFuncionario.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableFuncionario.insertRow(defaultTableFuncionario.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2)
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO PELA BUSCA ESPECÍFICA DE FUNCIONÁRIO...\n" + ex);
        }
    }

    // Lista todos funcionario do BD para JTable "jtFuncionario"
    private void selectAllFuncionario() {

        try {
            ps = conn.prepareStatement(
                    "select id, nome from funcionarios"
            );
            rs = ps.executeQuery();

            ((DefaultTableModel) jtFuncionario.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableFuncionario.insertRow(defaultTableFuncionario.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2)
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR FUNCIONÁRIOS...\n" + e);
        }
    }

    //Preenchimento dos campos txt e ta de Funcionario pela seleção na jTable
    private void setTxtFieldsFuncionario() {
        int spot = jtFuncionario.getSelectedRow();//Estabelece posição"valor" do funcionário selecionado.

        txtIdFuncionario.setText(jtFuncionario.getModel().getValueAt(spot, 0).toString());
        //   txtNomeFuncionario.setText(jtFuncionario.getModel().getValueAt(spot, 1).toString());

        try {
            ps = conn.prepareStatement(
                    "select * from funcionarios where id = ?"
            );
            ps.setString(1, txtIdFuncionario.getText());
            rs = ps.executeQuery();
            while (rs.next()) {
                txtNomeFuncionario.setText(rs.getString(2));
                txtCpfFuncionario.setText(rs.getString(3));
                txtCnpjFuncionario.setText(rs.getString(4));
                txtTelefoneFuncionario.setText(rs.getString(5));
                jcbOcupacaoFuncionario.setSelectedItem(rs.getString("ocupacao"));
                taComplementoFuncionario.setText(rs.getString(7));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM LISTAR CAMPOS DE FUNCIONÁRIO...\n" + ex);
        }
    }

    // Limpa campos: serviço
    private void cleanFieldServico() {
        txtIdServico.setText("");
        txtServico.setText("");
        txtValorServico.setText("");

        btnInsertServico.setEnabled(true);
        btnUpdateServico.setEnabled(false);
        btnDeleteServico.setEnabled(false);
    }

    // Lista todos serviços do BD para JTable "jtServico"
    private void selectAllServico() {

        try {
            ps = conn.prepareStatement(
                    "select id, servico, valor from servicos"
            );
            rs = ps.executeQuery();

            ((DefaultTableModel) jtServico.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableServico.insertRow(defaultTableServico.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3)
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR SERVIÇOS...\n" + e);
        }
    }

    //Busca específica de serviço para jTeble.
    private void searchServico() {
        try {

            ps = conn.prepareStatement(
                    "select s.id, s.servico, s.valor from servicos s where s.servico like ?"
            );
            ps.setString(1, txtServico.getText() + "%");
            rs = ps.executeQuery();

            ((DefaultTableModel) jtServico.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableServico.insertRow(defaultTableServico.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3)
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO PELA BUSCA ESPECÍFICA DE SERVIÇO...\n" + ex);
        }
    }

    //Preenchimento dos campos txt de Serviço pela seleção na jTable
    private void setTxtFieldsServico() {
        int spot = jtServico.getSelectedRow();//Estabelece posição"valor" do serviço selecionado.

        txtIdServico.setText(jtServico.getModel().getValueAt(spot, 0).toString());
        txtServico.setText(jtServico.getModel().getValueAt(spot, 1).toString());
        txtValorServico.setText(jtServico.getModel().getValueAt(spot, 2).toString());

    }

    // Limpa campos: usuário, login e senha
    private void cleanFieldLog() {
        txtIdLog.setText("");
        txtUsuario.setText("");
        txtLogin.setText("");
        txtSenha.setText("");

        btnInsertLog.setEnabled(true);
        btnUpdateLog.setEnabled(false);
        btnDeleteLog.setEnabled(false);
    }

    // Lista todos usuários do BD para JTable "jtLog"
    private void selectAllLog() {

        try {
            ps = conn.prepareStatement(
                    "select id, usuario, login, senha from seguranca"
            );
            rs = ps.executeQuery();

            ((DefaultTableModel) jtLog.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableLog.insertRow(defaultTableLog.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR USUÁRIO...\n" + e);
        }
    }

    //Busca específica de usuário para jTeble.
    private void searchLog() {
        try {

            ps = conn.prepareStatement(
                    "select s.id, s.usuario, s.login, s.senha from seguranca s where s.usuario like ?"
            );
            ps.setString(1, txtUsuario.getText() + "%");
            rs = ps.executeQuery();

            ((DefaultTableModel) jtLog.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableLog.insertRow(defaultTableLog.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO PELA BUSCA ESPECÍFICA DE USUÁRIO...\n" + ex);
        }
    }

    //Preenchimento dos campos txt de Usuário pela seleção na jTable
    private void setTxtFieldsLog() {
        int spot = jtLog.getSelectedRow();//Estabelece posição"valor" do serviço selecionado.

        txtIdLog.setText(jtLog.getModel().getValueAt(spot, 0).toString());
        txtUsuario.setText(jtLog.getModel().getValueAt(spot, 1).toString());
        txtLogin.setText(jtLog.getModel().getValueAt(spot, 2).toString());
        txtSenha.setText(jtLog.getModel().getValueAt(spot, 3).toString());

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtIdFuncionario = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNomeFuncionario = new javax.swing.JTextField();
        txtTelefoneFuncionario = new javax.swing.JTextField();
        txtCpfFuncionario = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        taComplementoFuncionario = new javax.swing.JTextArea();
        txtCnpjFuncionario = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtFuncionario = new javax.swing.JTable();
        btnInsertFuncionario = new javax.swing.JButton();
        btnUpdateFuncionario = new javax.swing.JButton();
        btnDeleteFuncionario = new javax.swing.JButton();
        btnCleanAllFieldsFuncionario = new javax.swing.JButton();
        jcbOcupacaoFuncionario = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        txtIdServico = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtServico = new javax.swing.JTextField();
        txtValorServico = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtServico = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        btnInsertServico = new javax.swing.JButton();
        btnUpdateServico = new javax.swing.JButton();
        btnDeleteServico = new javax.swing.JButton();
        btnCleanAllFieldsServico = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtLog = new javax.swing.JTable();
        txtIdLog = new javax.swing.JLabel();
        btnDeleteLog = new javax.swing.JButton();
        btnUpdateLog = new javax.swing.JButton();
        btnInsertLog = new javax.swing.JButton();
        btnCleanAllFieldsLog = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtLogin = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtSenha = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestor Administrativo");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Funcionário");

        txtIdFuncionario.setText("ID");
        txtIdFuncionario.setEnabled(false);

        jLabel3.setText("* Nome");

        jLabel4.setText("* CPF");

        jLabel5.setText("* CNPJ");

        jLabel6.setText("* Telefone");

        jLabel8.setText("Complemento");

        txtNomeFuncionario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeFuncionarioKeyReleased(evt);
            }
        });

        taComplementoFuncionario.setColumns(20);
        taComplementoFuncionario.setRows(5);
        jScrollPane1.setViewportView(taComplementoFuncionario);

        jtFuncionario = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jtFuncionario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Nome"
            }
        ));
        jtFuncionario.getTableHeader().setReorderingAllowed(false);
        jtFuncionario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtFuncionarioMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jtFuncionario);

        btnInsertFuncionario.setText("Salvar");
        btnInsertFuncionario.setPreferredSize(new java.awt.Dimension(64, 28));
        btnInsertFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertFuncionarioActionPerformed(evt);
            }
        });

        btnUpdateFuncionario.setText("Editar");
        btnUpdateFuncionario.setPreferredSize(new java.awt.Dimension(64, 28));
        btnUpdateFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateFuncionarioActionPerformed(evt);
            }
        });

        btnDeleteFuncionario.setText("Excluir");
        btnDeleteFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFuncionarioActionPerformed(evt);
            }
        });

        btnCleanAllFieldsFuncionario.setText("Limpar Campos");
        btnCleanAllFieldsFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanAllFieldsFuncionarioActionPerformed(evt);
            }
        });

        jcbOcupacaoFuncionario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "* Selecione Ocupação", "Cabeleireiro(a)", "Manicure", " " }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(288, 288, 288)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtIdFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel3))
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNomeFuncionario, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                            .addComponent(txtCpfFuncionario)
                            .addComponent(txtTelefoneFuncionario)
                            .addComponent(txtCnpjFuncionario)))
                    .addComponent(jcbOcupacaoFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnCleanAllFieldsFuncionario))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnInsertFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnUpdateFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDeleteFuncionario))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addGap(171, 171, 171))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNomeFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdFuncionario)
                            .addComponent(btnCleanAllFieldsFuncionario))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnInsertFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnUpdateFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDeleteFuncionario))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCpfFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtCnpjFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTelefoneFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jcbOcupacaoFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtIdServico.setText("ID");
        txtIdServico.setEnabled(false);

        jLabel10.setText("* Serviço:");

        jLabel11.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel11.setText("* R$");

        txtServico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtServicoKeyReleased(evt);
            }
        });

        jtServico = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jtServico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Serviço", "R$"
            }
        ));
        jtServico.getTableHeader().setReorderingAllowed(false);
        jtServico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtServicoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jtServico);

        jLabel12.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Serviço e Valor");

        btnInsertServico.setText("Salvar");
        btnInsertServico.setPreferredSize(new java.awt.Dimension(64, 28));
        btnInsertServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertServicoActionPerformed(evt);
            }
        });

        btnUpdateServico.setText("Editar");
        btnUpdateServico.setPreferredSize(new java.awt.Dimension(64, 28));
        btnUpdateServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateServicoActionPerformed(evt);
            }
        });

        btnDeleteServico.setText("Excluir");
        btnDeleteServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteServicoActionPerformed(evt);
            }
        });

        btnCleanAllFieldsServico.setText("Limpar Campos");
        btnCleanAllFieldsServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanAllFieldsServicoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnCleanAllFieldsServico)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnInsertServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdateServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteServico))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtIdServico, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtServico, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtValorServico, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(196, 196, 196))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdServico)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(txtServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtValorServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsertServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdateServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteServico)
                    .addComponent(btnCleanAllFieldsServico))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jtLog = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jtLog.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Usuário", "Login", "Senha"
            }
        ));
        jtLog.getTableHeader().setReorderingAllowed(false);
        jtLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtLogMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jtLog);

        txtIdLog.setText("ID");
        txtIdLog.setEnabled(false);

        btnDeleteLog.setText("Excluir");
        btnDeleteLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteLogActionPerformed(evt);
            }
        });

        btnUpdateLog.setText("Editar");
        btnUpdateLog.setPreferredSize(new java.awt.Dimension(64, 28));
        btnUpdateLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateLogActionPerformed(evt);
            }
        });

        btnInsertLog.setText("Salvar");
        btnInsertLog.setPreferredSize(new java.awt.Dimension(64, 28));
        btnInsertLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertLogActionPerformed(evt);
            }
        });

        btnCleanAllFieldsLog.setText("Limpar Campos");
        btnCleanAllFieldsLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanAllFieldsLogActionPerformed(evt);
            }
        });

        jLabel9.setText("* Usuário:");

        jLabel13.setText("* Login:");

        jLabel14.setText("* Senha:");

        jLabel15.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Login e Senha");

        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtIdLog, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 578, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addComponent(jLabel14))
                                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtSenha)
                                            .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(138, 138, 138))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnCleanAllFieldsLog)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(btnInsertLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnUpdateLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnDeleteLog))))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(233, 233, 233)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdLog, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(btnCleanAllFieldsLog))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnInsertLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUpdateLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDeleteLog))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 762, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeFuncionarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeFuncionarioKeyReleased
        // B U S C A   E S P E C Í F I C A   D E   F U N C I O N Á R I O 
        searchFuncionario(); // bfMouse em txtNomeFuncionario / Eventos / Key / KeyReleased

    }//GEN-LAST:event_txtNomeFuncionarioKeyReleased

    private void jtFuncionarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtFuncionarioMouseClicked
        // P R E E N C H E   C A M P O S   D E   F U N C I O N Á R I O
        setTxtFieldsFuncionario();
        btnInsertFuncionario.setEnabled(false);
        btnUpdateFuncionario.setEnabled(true);
        btnDeleteFuncionario.setEnabled(true);

    }//GEN-LAST:event_jtFuncionarioMouseClicked

    private void btnInsertFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertFuncionarioActionPerformed
        // S A L V A   F U N C I O N Á R I O   E M   D.B
        try {
            if ((txtNomeFuncionario.getText().equals("")) || (txtCpfFuncionario.getText().equals(""))
                    || (txtCnpjFuncionario.getText().equals("")) || (txtTelefoneFuncionario.getText().equals(""))
                    || (jcbOcupacaoFuncionario.getSelectedItem().equals("* Selecione Opção"))) { 
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS OBRIGATÓRIOS ( * )");
            } else {
                funcionarioModel.setNome(txtNomeFuncionario.getText());
                funcionarioModel.setCpf(txtCpfFuncionario.getText());
                funcionarioModel.setCnpj(txtCnpjFuncionario.getText());
                funcionarioModel.setTelefone(txtTelefoneFuncionario.getText());
                funcionarioModel.setOcupacao(jcbOcupacaoFuncionario.getSelectedItem().toString());
                funcionarioModel.setComplemento(taComplementoFuncionario.getText());

                funcionarioController.insert(funcionarioModel);

                cleanFieldFuncionario();
                selectAllFuncionario();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR FUNCIONÁRIO...\n" + e);
        }
    }//GEN-LAST:event_btnInsertFuncionarioActionPerformed

    private void btnCleanAllFieldsFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanAllFieldsFuncionarioActionPerformed
        // L I M P A   T O D O S   O S   C A M P O S   D E   F U N C I O N Á R I O
        cleanFieldFuncionario();
        selectAllFuncionario();
        btnInsertFuncionario.setEnabled(true);
        btnUpdateFuncionario.setEnabled(false);
        btnDeleteFuncionario.setEnabled(false);
    }//GEN-LAST:event_btnCleanAllFieldsFuncionarioActionPerformed

    private void btnUpdateFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateFuncionarioActionPerformed
        // A T U A L I Z A   D A D O S   D E   F U N C I O N Á R I O
        int id = Integer.parseInt(txtIdFuncionario.getText());

        try {
            if ((txtNomeFuncionario.getText().equals("")) || (txtCpfFuncionario.getText().equals(""))
                    || (txtCnpjFuncionario.getText().equals("")) || (txtTelefoneFuncionario.getText().equals(""))
                    || (jcbOcupacaoFuncionario.getSelectedItem().equals("* Selecione Opção"))) { 
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS OBRIGATÓRIOS ( * )");
            } else {
                funcionarioModel.setNome(txtNomeFuncionario.getText());
                funcionarioModel.setCpf(txtCpfFuncionario.getText());
                funcionarioModel.setCnpj(txtCnpjFuncionario.getText());
                funcionarioModel.setTelefone(txtTelefoneFuncionario.getText());
                funcionarioModel.setOcupacao(jcbOcupacaoFuncionario.getSelectedItem().toString());
                funcionarioModel.setComplemento(taComplementoFuncionario.getText());

                funcionarioController.update(funcionarioModel, id);

                cleanFieldFuncionario();
                selectAllFuncionario();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR FUNCIONÁRIO...\n" + e);
        }

    }//GEN-LAST:event_btnUpdateFuncionarioActionPerformed

    private void btnDeleteFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFuncionarioActionPerformed
        // E X C L U I   F U N C I O N Á R I O   E S P E C Í F I C O
        int atencao = JOptionPane.showConfirmDialog(null,
                "Tem certeza que deseja excluir permanentemente "
                + "[ " + txtNomeFuncionario.getText() + " ] ?", "ATENÇÃO",
                JOptionPane.YES_NO_OPTION
        );

        if (atencao == JOptionPane.YES_OPTION) {
            funcionarioController.deleteOne(Integer.parseInt(txtIdFuncionario.getText()));
            cleanFieldFuncionario();
            selectAllFuncionario();
        }


    }//GEN-LAST:event_btnDeleteFuncionarioActionPerformed

    private void txtServicoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServicoKeyReleased
        // B U S C A   E S P E C Í F I C A   D E   S E R V I Ç O
        searchServico(); // bfMouse em txtServico / Eventos / Key / KeyReleased

    }//GEN-LAST:event_txtServicoKeyReleased

    private void jtServicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtServicoMouseClicked
        // P R E E N C H E   C A M P O S   D E   S E R V I Ç O
        setTxtFieldsServico();
        btnInsertServico.setEnabled(false);
        btnUpdateServico.setEnabled(true);
        btnDeleteServico.setEnabled(true);

    }//GEN-LAST:event_jtServicoMouseClicked

    private void btnInsertServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertServicoActionPerformed
        // S A L V A   S E R V I Ç O   E M   D.B
        try {
            if ((txtServico.getText().equals("")) || (txtValorServico.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS OBRIGATÓRIOS ( * )");
            } else {
                servicoModel.setServico(txtServico.getText());
                servicoModel.setValor(Double.parseDouble(txtValorServico.getText().replace(",", ".")));

                servicoController.insert(servicoModel);

                cleanFieldServico();
                selectAllServico();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR SERVIÇO...\n" + e);
        }

    }//GEN-LAST:event_btnInsertServicoActionPerformed

    private void btnCleanAllFieldsServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanAllFieldsServicoActionPerformed
        // L I M P A   T O D O S   O S   C A M P O S   D E   S E R V I Ç O
        cleanFieldServico();
        selectAllServico();
        btnInsertServico.setEnabled(true);
        btnUpdateServico.setEnabled(false);
        btnDeleteServico.setEnabled(false);
    }//GEN-LAST:event_btnCleanAllFieldsServicoActionPerformed

    private void btnUpdateServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateServicoActionPerformed
        // A T U A L I Z A   D A D O S   D E   S E R V I Ç O
        int id = Integer.parseInt(txtIdServico.getText());

        try {
            if ((txtServico.getText().equals("")) || (txtValorServico.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS OBRIGATÓRIOS ( * )");
            } else {
                servicoModel.setServico(txtServico.getText());
                servicoModel.setValor(Double.parseDouble(txtValorServico.getText().replace(",", ".")));

                servicoController.update(servicoModel, id);

                cleanFieldServico();
                selectAllServico();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR SERVIÇO...\n" + e);
        }

    }//GEN-LAST:event_btnUpdateServicoActionPerformed

    private void btnDeleteServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteServicoActionPerformed
        // E X C L U I   S E R V I Ç O
        int atencao = JOptionPane.showConfirmDialog(null,
                "Tem certeza que deseja excluir permanentemente serviço de "
                + "[ " + txtServico.getText() + " ] ?", "ATENÇÃO",
                JOptionPane.YES_NO_OPTION
        );

        if (atencao == JOptionPane.YES_OPTION) {
            servicoController.deleteOne(Integer.parseInt(txtIdServico.getText()));
            cleanFieldServico();
            selectAllServico();
        }

    }//GEN-LAST:event_btnDeleteServicoActionPerformed

    private void txtUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyReleased
        // B U S C A   E S P E C Í F I C A   D E   U S U Á R I O
        searchLog(); // bfMouse em txtUsuario / Eventos / Key / KeyReleased

    }//GEN-LAST:event_txtUsuarioKeyReleased

    private void jtLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtLogMouseClicked
        // P R E E N C H E   C A M P O S   D E   U S U Á R I O
        setTxtFieldsLog();
        btnInsertLog.setEnabled(false);
        btnUpdateLog.setEnabled(true);
        btnDeleteLog.setEnabled(true);

    }//GEN-LAST:event_jtLogMouseClicked

    private void btnInsertLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertLogActionPerformed
        // S A L V A   U S U Á R I O   E M   D.B

        // com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'b' for key 'login'
        try {
            if ((txtUsuario.getText().equals("")) || (txtLogin.getText().equals(""))
                    || (txtSenha.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS OBRIGATÓRIOS ( * )");
            } else {
                logModel.setUsuario(txtUsuario.getText());
                logModel.setLogin(txtLogin.getText());
                logModel.setSenha(txtSenha.getText());

                logController.insert(logModel);

                cleanFieldLog();
                selectAllLog();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR USUÁRIO da view...\n" + e);
        }

    }//GEN-LAST:event_btnInsertLogActionPerformed

    private void btnCleanAllFieldsLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanAllFieldsLogActionPerformed
        // L I M P A   T O D O S   O S   C A M P O S   D E   U S U Á R I O
        cleanFieldLog();
        selectAllLog();
        btnInsertLog.setEnabled(true);
        btnUpdateLog.setEnabled(false);
        btnDeleteLog.setEnabled(false);

    }//GEN-LAST:event_btnCleanAllFieldsLogActionPerformed

    private void btnUpdateLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateLogActionPerformed
        // A T U A L I Z A   D A D O S   D E   U S U Á R I O
        int id = Integer.parseInt(txtIdLog.getText());

        try {
            if ((txtUsuario.getText().equals("")) || (txtLogin.getText().equals(""))
                    || (txtSenha.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS OBRIGATÓRIOS ( * )");
            } else {
                logModel.setUsuario(txtUsuario.getText());
                logModel.setLogin(txtLogin.getText());
                logModel.setSenha(txtSenha.getText());

                logController.update(logModel, id);

                cleanFieldLog();
                selectAllLog();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR USUÁRIO...\n" + e);
        }

    }//GEN-LAST:event_btnUpdateLogActionPerformed

    private void btnDeleteLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteLogActionPerformed
        // E X C L U I   U S U Á R I O
        int atencao = JOptionPane.showConfirmDialog(null,
                "Tem certeza que deseja excluir permanentemente o Usuário "
                + "[ " + txtUsuario.getText() + " ] ?", "ATENÇÃO",
                JOptionPane.YES_NO_OPTION
        );

        if (atencao == JOptionPane.YES_OPTION) {
            logController.deleteOne(Integer.parseInt(txtIdLog.getText()));
            cleanFieldLog();
            selectAllLog();
        }

    }//GEN-LAST:event_btnDeleteLogActionPerformed

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
            java.util.logging.Logger.getLogger(GestorAdministrativoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestorAdministrativoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestorAdministrativoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestorAdministrativoView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestorAdministrativoView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCleanAllFieldsFuncionario;
    private javax.swing.JButton btnCleanAllFieldsLog;
    private javax.swing.JButton btnCleanAllFieldsServico;
    private javax.swing.JButton btnDeleteFuncionario;
    private javax.swing.JButton btnDeleteLog;
    private javax.swing.JButton btnDeleteServico;
    private javax.swing.JButton btnInsertFuncionario;
    private javax.swing.JButton btnInsertLog;
    private javax.swing.JButton btnInsertServico;
    private javax.swing.JButton btnUpdateFuncionario;
    private javax.swing.JButton btnUpdateLog;
    private javax.swing.JButton btnUpdateServico;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JComboBox<String> jcbOcupacaoFuncionario;
    private javax.swing.JTable jtFuncionario;
    private javax.swing.JTable jtLog;
    private javax.swing.JTable jtServico;
    private javax.swing.JTextArea taComplementoFuncionario;
    private javax.swing.JTextField txtCnpjFuncionario;
    private javax.swing.JTextField txtCpfFuncionario;
    private javax.swing.JLabel txtIdFuncionario;
    private javax.swing.JLabel txtIdLog;
    private javax.swing.JLabel txtIdServico;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtNomeFuncionario;
    private javax.swing.JTextField txtSenha;
    private javax.swing.JTextField txtServico;
    private javax.swing.JTextField txtTelefoneFuncionario;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JTextField txtValorServico;
    // End of variables declaration//GEN-END:variables
}
