package br.com.salon.view;

import br.com.salon.controller.AgendaController;
import br.com.salon.controller.ClienteController;
import br.com.salon.controller.ConnectionController;
import br.com.salon.controller.FuncionarioController;
import br.com.salon.controller.ServicoController;
import br.com.salon.model.AgendaModel;
import br.com.salon.model.ClienteModel;
import br.com.salon.model.FuncionarioModel;
import br.com.salon.model.ServicoModel;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class AgendaView extends javax.swing.JFrame {

    ConnectionController connController = new ConnectionController();
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    String servicoCliente = ""; // Agregando serviços para salvar em DB
    double valorTotal = 0.0f; // Agregando valor total para salvar em DB
    String category = ""; // Para busca específica de funcionário e ou cliente em serviços agendados
    String txtSubject = ""; // Para busca específica de funcionário e ou cliente em serviços agendados
//    int switchSecurity = 0; // 
    //  public static boolean jcb = false;

    DefaultTableModel defaultTableCliente = new DefaultTableModel();
    ClienteController clienteController = new ClienteController();
    ClienteModel clienteModel = new ClienteModel();
    ArrayList<ClienteModel> arrayListCliente = new ArrayList();

    DefaultTableModel defaultTableServicoAgendar = new DefaultTableModel();
    ServicoController servicoController = new ServicoController();
    ServicoModel servicoModel = new ServicoModel();
    ArrayList<ServicoModel> arrayListServico = new ArrayList();

    DefaultTableModel defaultTableFuncionarioAgendar = new DefaultTableModel();
    FuncionarioController FuncionarioController = new FuncionarioController();
    FuncionarioModel funcionarioModel = new FuncionarioModel();
    ArrayList<FuncionarioModel> arrayListFuncionarioAgendar = new ArrayList();

    DefaultTableModel defaultTableAgendado = new DefaultTableModel();
    AgendaController agendaController = new AgendaController();
    AgendaModel agendaModel = new AgendaModel();
    ArrayList<AgendaModel> arrayListAgendado = new ArrayList();

    LoginView logView = new LoginView();

    //CONSTRUCTOR
    public AgendaView() {
        initComponents();
        this.setExtendedState(MAXIMIZED_BOTH);

        conn = connController.connToView();
        defaultTableCliente = (DefaultTableModel) jtCliente.getModel();
        defaultTableServicoAgendar = (DefaultTableModel) jtAgendarServico.getModel();
        defaultTableFuncionarioAgendar = (DefaultTableModel) jtAgendarFuncionario.getModel();
        defaultTableAgendado = (DefaultTableModel) jtAgendado.getModel();
// Limita largura "MaxWidth" dos campos "ID"
        jtAgendarServico.getColumnModel().getColumn(0).setMaxWidth(25);
        jtCliente.getColumnModel().getColumn(0).setMaxWidth(50);
        jtAgendarFuncionario.getColumnModel().getColumn(0).setMaxWidth(25);
        jtAgendarFuncionario.getColumnModel().getColumn(2).setMaxWidth(70);
        jtAgendado.getColumnModel().getColumn(0).setMaxWidth(80);
        jtAgendado.getColumnModel().getColumn(1).setMaxWidth(60);
        jtAgendado.getColumnModel().getColumn(5).setMaxWidth(50);

        selectAll();
        selectAllServico();
        selectFuncionario();
        selectAllAgendado();
        btnUpdateCliente.setEnabled(false);
        btnDeleteCliente.setEnabled(false);

        btnDeleteOneAgendado.setEnabled(false);
        btnUpdateAgendar.setEnabled(false);

        setDate();

        desableGroup();

    }

// Limpa campos: Cliente
    private void cleanFieldCliente() {
        txtIdCliente.setText("ID Cliente");
        txtNomeCliente.setText("");
        txtTelefoneCliente.setText("");
        taComplementoCliente.setText("");
        txtClienteAgendar.setText("");

        btnInsertCliente.setEnabled(true);
        btnUpdateCliente.setEnabled(false);
        btnDeleteCliente.setEnabled(false);

    }

    // Limpa campos: Agendar
    private void cleanFieldAgendar() {
        servicoCliente = "";
        valorTotal = 0.00f;

        txtClienteAgendar.setText("");
        txtCabeleireiroAgendar.setText("");
        txtManicureAgendar.setText("");
        jtpServicoAgendar.setText(servicoCliente);
        setDate();
        txtValorTotalServico.setText(Double.toString(valorTotal));
        lblIdAgendado.setText("");

    }

// Lista todos clientes do BD para JTable "Table"
    private void selectAll() {

        try {
            ps = conn.prepareStatement(
                    "select id, nome, telefone from clientes"
            );
            rs = ps.executeQuery();

            jtCliente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            ((DefaultTableModel) jtCliente.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableCliente.insertRow(defaultTableCliente.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3)
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR CLIENTES...\n" + e);
        }
    }

// Busca específica de cliente para jTeble.
    private void searchCiente() {
        try {

            ps = conn.prepareStatement(
                    "select c.id, c.nome, c.telefone from clientes c where c.nome like ?"
            );
            ps.setString(1, txtNomeCliente.getText() + "%");
            rs = ps.executeQuery();

            ((DefaultTableModel) jtCliente.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableCliente.insertRow(defaultTableCliente.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3)
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO NA BUSCA ESPECÍFICA DE CLIENTE...\n" + ex);
        }
    }

// Preenchimento dos campos txt e ta de Cliente pela seleção na jTable
    private void setTxtFields() {
        int spot = jtCliente.getSelectedRow();//Estabelece posição"valor" do cliente selecionado.

        txtIdCliente.setText(jtCliente.getModel().getValueAt(spot, 0).toString());
        txtNomeCliente.setText(jtCliente.getModel().getValueAt(spot, 1).toString());
        txtClienteAgendar.setText(jtCliente.getModel().getValueAt(spot, 1).toString());
        txtTelefoneCliente.setText(jtCliente.getModel().getValueAt(spot, 2).toString());

        try {
            ps = conn.prepareStatement(
                    "select * from clientes where id = ?"
            );
            ps.setString(1, txtIdCliente.getText());
            rs = ps.executeQuery();
            while (rs.next()) {
                taComplementoCliente.setText(rs.getString(4));

                category = "cliente"; // Para busca específica de clientee em serviços agendados 
                txtSubject = txtNomeCliente.getText(); // Para busca específica de clientee em serviços agendados
                searchAgendado();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM LISTAR COMPLEMENTO DE CLIENTE...\n" + ex);
        }

    }

// Lista serviços do BD para JTable "Table"
    private void selectAllServico() {

        try {
            ps = conn.prepareStatement(
                    "select id, servico from servicos"
            );
            rs = ps.executeQuery();

            ((DefaultTableModel) jtAgendarServico.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableServicoAgendar.insertRow(defaultTableServicoAgendar.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2)
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR SERVIÇOS...\n" + e);
        }
    }

// Preenchimento do campo jtp de Serviços e txt de Valor pela seleção na jTable
    private void setServicoAgendar() {
        int spot = jtAgendarServico.getSelectedRow();//Estabelece posição"valor" do serviço selecionado.
        String id = jtAgendarServico.getModel().getValueAt(spot, 0).toString();

        try {
            ps = conn.prepareStatement(
                    "select * from servicos where id = ?"
            );
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                servicoCliente += rs.getString(2) + ". ";
                valorTotal += rs.getDouble(3);
                jtpServicoAgendar.setText(servicoCliente);
                txtValorTotalServico.setText(Double.toString(valorTotal));

                /*servicoCliente += rs.getString(2) + ". <br>";
                valorTotal += rs.getDouble(3);
                lblTeste.setText("<html>" + servicoCliente + " </html>");
                lblValorTotal.setText(Double.toString(valorTotal));*/
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO EM LISTAR SERVIÇOS...\n" + ex);
        }
    }

// Preenche campos txtData e Horario 
    private void setDate() {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm");

        String dataFormatada = formatDate.format(this.jCalendar.getDate());
        String dataHour = formatHour.format(this.jCalendar.getDate());

        txtDataAgendar.setText(dataFormatada);
        txtHorarioAgendar.setText(dataHour);

    }

// Lista Funcionários do BD para JTable "Table"
    private void selectFuncionario() {
        try {
            ps = conn.prepareStatement(
                    "select * from funcionarios"
            );
            rs = ps.executeQuery();

            ((DefaultTableModel) jtAgendarFuncionario.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableFuncionarioAgendar.insertRow(defaultTableFuncionarioAgendar.getRowCount(), new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(6)
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR FUNCIONÁRIOS...\n" + e);
        }
    }

// Preenchimento do campo txt de Cabeleireiro e ou txt Manicure pela seleção na jTable
    private void setFuncionarioAgendar() {
        int spot = jtAgendarFuncionario.getSelectedRow();//Estabelece posição"valor" do serviço selecionado.
        String id = jtAgendarFuncionario.getModel().getValueAt(spot, 0).toString();

        try {
            ps = conn.prepareStatement(
                    "select * from funcionarios where id = ?"
            );
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (txtCabeleireiroAgendar.getText().equals(rs.getString(2))) {
                    txtCabeleireiroAgendar.setText("");

                    category = "cabeleireiro";
                    txtSubject = txtCabeleireiroAgendar.getText();
                    searchAgendado();

                } else if (rs.getString(6).equals("Cabeleireiro(a)")) {
                    txtCabeleireiroAgendar.setText(rs.getString(2));

                    category = "cabeleireiro";
                    txtSubject = txtCabeleireiroAgendar.getText();
                    searchAgendado();

                }

                if (txtManicureAgendar.getText().equals(rs.getString(2))) {
                    txtManicureAgendar.setText("");

                    category = "manicure";
                    txtSubject = txtManicureAgendar.getText();
                    searchAgendado();

                } else if (rs.getString(6).equals("Manicure")) {
                    txtManicureAgendar.setText(rs.getString(2));

                    category = "manicure";
                    txtSubject = txtManicureAgendar.getText();
                    searchAgendado();

                }
            }
        } catch (SQLException e) {
            /* 
 * java.sql.SQLException: After end of result set
 * java.sql.SQLException: Illegal operation on empty result set.
             */

            //  JOptionPane.showMessageDialog(null, "ERRO EM LISTAR FUNCIONÁRIO...\n" + e);
        }
    }

// Lista todos os agendamentos de serviços para jTeble.
    private void selectAllAgendado() {

        try {

            ps = conn.prepareStatement(
                    "select * from agendas order by data desc, horario"
            );
            rs = ps.executeQuery();

            ((DefaultTableModel) jtAgendado.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableAgendado.insertRow(defaultTableAgendado.getRowCount(), new Object[]{
                    rs.getString(7), rs.getString(8), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(1)
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO AO LISTAR AGENDAMENTO...\n" + ex);
        }
    }

// Busca específica do Serviços Agendados: Cabeleireiro(a), Manicure ou Cliente para jTeble.
    private void searchAgendado() {

        try {
            if (!category.equals("Data")) {
                ps = conn.prepareStatement(
                        "select * from agendas where " + category + " like ? order by data desc, horario"
                );
                ps.setString(1, txtSubject);
                rs = ps.executeQuery();
            } else if (category.equals("Data")){
                ps = conn.prepareStatement(
                        "select * from agendas where " + category + " between ? and ? order by data desc, horario"
                );
                ps.setString(1, txtDataDeleteGroupInicio.getText());
                ps.setString(2, txtDataDeleteGroupFim.getText());
                rs = ps.executeQuery();
            }

            ((DefaultTableModel) jtAgendado.getModel()).setRowCount(0);//Limpa jTable
            while (rs.next()) {
                defaultTableAgendado.insertRow(defaultTableAgendado.getRowCount(), new Object[]{
                    rs.getString(7), rs.getString(8), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(1)
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERRO NA BUSCA ESPECÍFICA EM AGENDAMENTO...\n" + ex);
        }
    }

// Preenchimento do campo lblIdAgendado e txtClienteAgendar pela seleção na jTable
    private void setTxtFieldIdAgendado() {
        btnInsertAgedar.setEnabled(false);
        btnDeleteOneAgendado.setEnabled(true);
        btnUpdateAgendar.setEnabled(true);
        int spot = jtAgendado.getSelectedRow();//Estabelece posição"valor" do cliente selecionado.

        txtDataAgendar.setText(jtAgendado.getModel().getValueAt(spot, 0).toString());
        txtHorarioAgendar.setText(jtAgendado.getModel().getValueAt(spot, 1).toString());
        txtCabeleireiroAgendar.setText(jtAgendado.getModel().getValueAt(spot, 2).toString());
        txtManicureAgendar.setText(jtAgendado.getModel().getValueAt(spot, 3).toString());
        txtClienteAgendar.setText(jtAgendado.getModel().getValueAt(spot, 4).toString());
        lblIdAgendado.setText(jtAgendado.getModel().getValueAt(spot, 5).toString());

        //  jtpServicoAgendar.setText("");
        //  txtValorTotalServico.setText("0.00");
        try { // Preenche campos Serviço e valor total
            ps = conn.prepareStatement(
                    "select * from agendas where id = ?"
            );
            ps.setString(1, lblIdAgendado.getText());
            rs = ps.executeQuery();
            while (rs.next()) {
                jtpServicoAgendar.setText(rs.getString(5));
                txtValorTotalServico.setText(rs.getString(6));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "ERRO EM LISTAR SERVIÇO E VALOR TOTAL DA AGENDA ID [" + lblIdAgendado.getText() + "]\n" + ex
            );
        }
    }

// Inabelita campos de texto, lable e botão no Grupo de Exclusão.
    private void desableGroup() {
        jcbDeleteGroup.setSelectedIndex(0);
        jcbDeleteGroup.setEnabled(false);
        btnDeleteGroup.setEnabled(false);
        lblNomeDeleteGroup.setEnabled(false);
        txtNomeDeleteGroup.setEnabled(false);
        txtNomeDeleteGroup.setText("");
        lblDataDeleteGroupInicio.setEnabled(false);
        txtDataDeleteGroupInicio.setEnabled(false);
        txtDataDeleteGroupInicio.setText("0000-00-00");
        lblDataDeleteGroupFim.setEnabled(false);
        txtDataDeleteGroupFim.setEnabled(false);
        txtDataDeleteGroupFim.setText("0000-00-00");
    }

//Variação de sintaxes "sql" para excluir grupos em DB de agendas
    private void sqlGroup(String sql) {
        agendaController.deleteSubject(sql);
        desableGroup();
        selectAllAgendado();
    }

    private void switchSecurity() {
        logView.setSwitch = 2;
        logView.setVisible(true);
        //  jcbDeleteGroup.setEnabled(jcb);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        txtIdCliente = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtCliente = new javax.swing.JTable();
        txtNomeCliente = new javax.swing.JTextField();
        txtTelefoneCliente = new javax.swing.JTextField();
        btnInsertCliente = new javax.swing.JButton();
        btnUpdateCliente = new javax.swing.JButton();
        btnDeleteCliente = new javax.swing.JButton();
        btnCleanAllFields = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        taComplementoCliente = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jCalendar = new com.toedter.calendar.JCalendar();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtAgendarFuncionario = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jtAgendarServico = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtClienteAgendar = new javax.swing.JTextField();
        txtCabeleireiroAgendar = new javax.swing.JTextField();
        txtManicureAgendar = new javax.swing.JTextField();
        txtValorTotalServico = new javax.swing.JTextField();
        txtDataAgendar = new javax.swing.JTextField();
        txtHorarioAgendar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtpServicoAgendar = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblClienteId = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnInsertAgedar = new javax.swing.JButton();
        btnLimparServicoData = new javax.swing.JButton();
        lblIdAgendado = new javax.swing.JLabel();
        btnResetAgendar = new javax.swing.JButton();
        btnUpdateAgendar = new javax.swing.JButton();
        btnDeleteOneAgendado = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtAgendado = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jcbDeleteGroup = new javax.swing.JComboBox<>();
        btnDeleteGroup = new javax.swing.JButton();
        lblNomeDeleteGroup = new javax.swing.JLabel();
        lblDataDeleteGroupInicio = new javax.swing.JLabel();
        txtDataDeleteGroupInicio = new javax.swing.JTextField();
        lblDataDeleteGroupFim = new javax.swing.JLabel();
        txtDataDeleteGroupFim = new javax.swing.JTextField();
        txtNomeDeleteGroup = new javax.swing.JTextField();
        btnPermission = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        mnMainAdmin = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Salão D'Cortez");

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtIdCliente.setFont(new java.awt.Font("sansserif", 0, 8)); // NOI18N
        txtIdCliente.setText("ID Cliente");
        txtIdCliente.setToolTipText("");
        txtIdCliente.setEnabled(false);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Nome:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Telefone:");

        jLabel4.setText("Complemento");

        jtCliente = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jtCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
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
                "ID", "Nome", "Telefone"
            }
        ));
        jtCliente.getTableHeader().setReorderingAllowed(false);
        jtCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtClienteMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtCliente);

        txtNomeCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeClienteKeyReleased(evt);
            }
        });

        btnInsertCliente.setText("Salvar");
        btnInsertCliente.setPreferredSize(new java.awt.Dimension(64, 28));
        btnInsertCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertClienteActionPerformed(evt);
            }
        });

        btnUpdateCliente.setText("Editar");
        btnUpdateCliente.setPreferredSize(new java.awt.Dimension(64, 28));
        btnUpdateCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateClienteActionPerformed(evt);
            }
        });

        btnDeleteCliente.setText("Excluir");
        btnDeleteCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteClienteActionPerformed(evt);
            }
        });

        btnCleanAllFields.setText("Limpar Campos");
        btnCleanAllFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanAllFieldsActionPerformed(evt);
            }
        });

        taComplementoCliente.setColumns(20);
        taComplementoCliente.setRows(5);
        jScrollPane2.setViewportView(taComplementoCliente);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(233, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(197, 197, 197))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnUpdateCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDeleteCliente)
                                    .addComponent(btnInsertCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtTelefoneCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(57, 57, 57))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnCleanAllFields)))))))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCleanAllFields)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefoneCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnInsertCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnUpdateCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteCliente)
                                .addGap(6, 6, 6))
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jCalendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jCalendarMouseEntered(evt);
            }
        });

        jtAgendarFuncionario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Funcionário", "Ocupação"
            }
        ));
        jtAgendarFuncionario.getTableHeader().setReorderingAllowed(false);
        jtAgendarFuncionario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtAgendarFuncionarioMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jtAgendarFuncionario);

        jtAgendarServico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Serviços"
            }
        ));
        jtAgendarServico.getTableHeader().setReorderingAllowed(false);
        jtAgendarServico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtAgendarServicoMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jtAgendarServico);

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtClienteAgendar.setEditable(false);
        txtClienteAgendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtClienteAgendarMouseClicked(evt);
            }
        });

        txtCabeleireiroAgendar.setEditable(false);
        txtCabeleireiroAgendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCabeleireiroAgendarMouseClicked(evt);
            }
        });

        txtManicureAgendar.setEditable(false);
        txtManicureAgendar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtManicureAgendarMouseClicked(evt);
            }
        });

        txtValorTotalServico.setEditable(false);
        txtValorTotalServico.setText("0.00");

        jtpServicoAgendar.setEditable(false);
        jScrollPane3.setViewportView(jtpServicoAgendar);

        jLabel2.setText("R$");

        jLabel5.setText("Hr");

        jLabel6.setText("Data");

        lblClienteId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClienteId.setText("Cliente");
        lblClienteId.setPreferredSize(new java.awt.Dimension(83, 16));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Cabeleireiro(a)");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Manicure");
        jLabel9.setPreferredSize(new java.awt.Dimension(83, 16));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Serviço");
        jLabel10.setPreferredSize(new java.awt.Dimension(83, 16));

        btnInsertAgedar.setText("Confirmar Agendamento");
        btnInsertAgedar.setPreferredSize(new java.awt.Dimension(70, 28));
        btnInsertAgedar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertAgedarActionPerformed(evt);
            }
        });

        btnLimparServicoData.setFont(new java.awt.Font("sansserif", 0, 10)); // NOI18N
        btnLimparServicoData.setText("Limpar Serviço");
        btnLimparServicoData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparServicoDataActionPerformed(evt);
            }
        });

        lblIdAgendado.setFont(new java.awt.Font("sansserif", 0, 8)); // NOI18N
        lblIdAgendado.setText("Id Agenda Selecionada");
        lblIdAgendado.setToolTipText("");
        lblIdAgendado.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtManicureAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCabeleireiroAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblClienteId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtClienteAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDataAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHorarioAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtValorTotalServico, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblIdAgendado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnInsertAgedar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimparServicoData))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtClienteAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblClienteId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCabeleireiroAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtManicureAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLimparServicoData)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtValorTotalServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtHorarioAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(txtDataAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnInsertAgedar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblIdAgendado, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnResetAgendar.setText("<html><strong>Restaura</strong> Campos de Serviço</html>");
        btnResetAgendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetAgendarActionPerformed(evt);
            }
        });

        btnUpdateAgendar.setText("<html><strong>Editar</strong> Agenda Selecionado</html>");
        btnUpdateAgendar.setPreferredSize(new java.awt.Dimension(70, 28));
        btnUpdateAgendar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateAgendarActionPerformed(evt);
            }
        });

        btnDeleteOneAgendado.setText("<html><strong>Excluir</strong> Agenda Selecionado</html>");
        btnDeleteOneAgendado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteOneAgendadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnUpdateAgendar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteOneAgendado, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnResetAgendar, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUpdateAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteOneAgendado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnResetAgendar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCalendar, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jtAgendado = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        jtAgendado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Data", "Hora", "Cabeleireiro(a)", "Manicure", "Cliente", "ID"
            }
        ));
        jtAgendado.getTableHeader().setReorderingAllowed(false);
        jtAgendado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtAgendadoMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jtAgendado);

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel7.setText("Grupos de Exclusão da Lista de Aqendamento.");

        jcbDeleteGroup.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione Grupo", "Cabeleireiro", "Manicure", "Cliente", "Data", "Todos" }));
        jcbDeleteGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbDeleteGroupActionPerformed(evt);
            }
        });

        btnDeleteGroup.setText("Excluir Grupo");
        btnDeleteGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteGroupActionPerformed(evt);
            }
        });

        lblNomeDeleteGroup.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNomeDeleteGroup.setText("Nome:");

        lblDataDeleteGroupInicio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDataDeleteGroupInicio.setText("Data Início:");

        txtDataDeleteGroupInicio.setText("0000-00-00");
        txtDataDeleteGroupInicio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDataDeleteGroupInicioKeyReleased(evt);
            }
        });

        lblDataDeleteGroupFim.setText("Data Fim:");

        txtDataDeleteGroupFim.setText("0000-00-00");
        txtDataDeleteGroupFim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDataDeleteGroupFimKeyReleased(evt);
            }
        });

        txtNomeDeleteGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeDeleteGroupActionPerformed(evt);
            }
        });
        txtNomeDeleteGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNomeDeleteGroupKeyReleased(evt);
            }
        });

        btnPermission.setText("Admin");
        btnPermission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPermissionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(btnPermission)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDeleteGroup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbDeleteGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(68, 68, 68))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblDataDeleteGroupInicio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblNomeDeleteGroup, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtDataDeleteGroupInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDataDeleteGroupFim)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDataDeleteGroupFim, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNomeDeleteGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteGroup)
                    .addComponent(jcbDeleteGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPermission))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNomeDeleteGroup)
                    .addComponent(txtNomeDeleteGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDataDeleteGroupInicio)
                    .addComponent(txtDataDeleteGroupInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDataDeleteGroupFim)
                    .addComponent(txtDataDeleteGroupFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel11.setText("Cadastro de Cliente e Serviço");

        jLabel13.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        jLabel13.setText("Serviços Agendados");
        jLabel13.setToolTipText("");

        mnMainAdmin.setText("Administrador");
        mnMainAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnMainAdminMouseClicked(evt);
            }
        });
        jMenuBar2.add(mnMainAdmin);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(242, 242, 242)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 533, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(184, 184, 184))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeClienteKeyReleased
        // B U S C A   E S P E C Í F I C A   D E   C L I E N T E 
        searchCiente(); // bfMouse em txtNomeCliente / Eventos / Key / KeyReleased

    }//GEN-LAST:event_txtNomeClienteKeyReleased

    private void jtClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtClienteMouseClicked
        // P R E E N C H E   C A M P O S   D E   C L I E N T E
        setTxtFields();
        btnInsertCliente.setEnabled(false);
        btnUpdateCliente.setEnabled(true);
        btnDeleteCliente.setEnabled(true);

    }//GEN-LAST:event_jtClienteMouseClicked

    private void btnInsertClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertClienteActionPerformed
        // S A L V A   C L I E N T E   E M   D.B
        try {
            if ((txtNomeCliente.getText().equals("")) || (txtTelefoneCliente.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS:\nNome e Telefone\n");
            } else {
                clienteModel.setNome(txtNomeCliente.getText());
                clienteModel.setTelefone(txtTelefoneCliente.getText());
                clienteModel.setComplemento(taComplementoCliente.getText());

                clienteController.insert(clienteModel);

                cleanFieldCliente();
                selectAll();
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR CLIENTE...\n" + e);
        }

    }//GEN-LAST:event_btnInsertClienteActionPerformed

    private void btnCleanAllFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanAllFieldsActionPerformed
        // L I M P A   T O D O S   O S   C A M P O S
        cleanFieldCliente();
        selectAll();
        btnInsertCliente.setEnabled(true);
        btnUpdateCliente.setEnabled(false);
        btnDeleteCliente.setEnabled(false);
    }//GEN-LAST:event_btnCleanAllFieldsActionPerformed

    private void btnUpdateClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateClienteActionPerformed
        // A T U A L I Z A   D A D O S   D E   C L I E N T E
        int id = Integer.parseInt(txtIdCliente.getText());
        String nome = txtNomeCliente.getText();
        String telefone = txtTelefoneCliente.getText();
        String complemento = taComplementoCliente.getText();

        try {
            if ((txtNomeCliente.getText().equals("")) || (txtTelefoneCliente.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "PREENCHA OS CAMPOS:\nNome e Telefone\n");
            } else {
                clienteModel.setNome(nome);
                clienteModel.setTelefone(telefone);
                clienteModel.setComplemento(complemento);

                clienteController.update(clienteModel, id);
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR CLIENTE...\n" + e);
        }

        cleanFieldCliente();
        selectAll();

    }//GEN-LAST:event_btnUpdateClienteActionPerformed

    private void btnDeleteClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteClienteActionPerformed
        // E X C L U I   C L I E N T E   E S P E C Í F I C O
        int atencao = JOptionPane.showConfirmDialog(null,
                "Tem certeza que deseja excluir permanentemente "
                + "[ " + txtNomeCliente.getText() + " ] ?", "ATENÇÃO",
                JOptionPane.YES_NO_OPTION
        );

        if (atencao == JOptionPane.YES_OPTION) {
            clienteController.deleteOne(Integer.parseInt(txtIdCliente.getText()));
        }

        cleanFieldCliente();
        selectAll();

    }//GEN-LAST:event_btnDeleteClienteActionPerformed

    private void mnMainAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnMainAdminMouseClicked

        logView.setSwitch = 1;
        logView.setVisible(true);


    }//GEN-LAST:event_mnMainAdminMouseClicked

    private void jtAgendarServicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtAgendarServicoMouseClicked

        setServicoAgendar();

    }//GEN-LAST:event_jtAgendarServicoMouseClicked

    private void btnResetAgendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetAgendarActionPerformed
        btnInsertAgedar.setEnabled(true);
        btnDeleteOneAgendado.setEnabled(false);
        btnUpdateAgendar.setEnabled(false);
        cleanFieldAgendar();
        desableGroup();
        selectAllServico();
        selectFuncionario();
        selectAllAgendado();

    }//GEN-LAST:event_btnResetAgendarActionPerformed

    private void jCalendarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCalendarMouseEntered

        setDate();

    }//GEN-LAST:event_jCalendarMouseEntered

    private void jtAgendarFuncionarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtAgendarFuncionarioMouseClicked

        setFuncionarioAgendar();

    }//GEN-LAST:event_jtAgendarFuncionarioMouseClicked

    private void btnInsertAgedarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertAgedarActionPerformed
        category = "cliente";
        txtSubject = txtClienteAgendar.getText();

        try {

            if ((txtClienteAgendar.getText().equals("")) && (txtCabeleireiroAgendar.getText().equals(""))
                    && (txtManicureAgendar.getText().equals("")) && (jtpServicoAgendar.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "TODOS OS CAMPOS ESTÃO VASIOS:\nCadastro de Serviço Irrelevante...");
            } else {
                agendaModel.setCliente(txtClienteAgendar.getText());
                agendaModel.setCabeleireiro(txtCabeleireiroAgendar.getText());
                agendaModel.setManicure(txtManicureAgendar.getText());
                agendaModel.setServico(jtpServicoAgendar.getText());
                agendaModel.setData(txtDataAgendar.getText());
                agendaModel.setData(txtDataAgendar.getText());
                agendaModel.setHorario(txtHorarioAgendar.getText());
                agendaModel.setValor_total(txtValorTotalServico.getText());

                agendaController.insert(agendaModel);

                searchAgendado();
                cleanFieldAgendar();
            }

            //  selectAllAgendado();
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM SALVAR AGENDAMENTO DE SERVIÇO...\n" + e);
        }
    }//GEN-LAST:event_btnInsertAgedarActionPerformed

    private void jtAgendadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtAgendadoMouseClicked

        setTxtFieldIdAgendado();

    }//GEN-LAST:event_jtAgendadoMouseClicked

    private void txtCabeleireiroAgendarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCabeleireiroAgendarMouseClicked

        category = "cabeleireiro";
        txtSubject = txtCabeleireiroAgendar.getText();
        searchAgendado();

        // searchCabeleireiroAgendado();
    }//GEN-LAST:event_txtCabeleireiroAgendarMouseClicked

    private void txtManicureAgendarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtManicureAgendarMouseClicked

        category = "manicure";
        txtSubject = txtManicureAgendar.getText();
        searchAgendado();

        // searchManicureAgendado();

    }//GEN-LAST:event_txtManicureAgendarMouseClicked

    private void btnDeleteOneAgendadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteOneAgendadoActionPerformed

        // E X C L U I   A G E N D A M E N T O   E S P E C Í F I C O
        if (lblIdAgendado.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Selecione um item específico da tabela...");
        } else {
            int atencao = JOptionPane.showConfirmDialog(null,
                    "Tem certeza que deseja excluir permanentemente item selecionado?:\n"
                    + " ID:[ " + lblIdAgendado.getText() + " ] Cliente:[ " + txtClienteAgendar.getText() + " ] ", "ATENÇÃO",
                    JOptionPane.YES_NO_OPTION
            );

            if (atencao == JOptionPane.YES_OPTION) {
                agendaController.deleteOne(Integer.parseInt(lblIdAgendado.getText()));
            }
            cleanFieldAgendar();
            selectAllAgendado();

            btnInsertAgedar.setEnabled(true);
            btnDeleteOneAgendado.setEnabled(false);
            btnUpdateAgendar.setEnabled(false);
        }


    }//GEN-LAST:event_btnDeleteOneAgendadoActionPerformed

    private void txtClienteAgendarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtClienteAgendarMouseClicked

        category = "cliente";
        txtSubject = txtNomeCliente.getText();
        searchAgendado();

    }//GEN-LAST:event_txtClienteAgendarMouseClicked

    private void btnUpdateAgendarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateAgendarActionPerformed
        // A T U A L I Z A   D A D O S   D E   A G E N D A M E N T O
        int id = Integer.parseInt(lblIdAgendado.getText());
        /*  && (txtClienteAgendar.getText().equals(""))
        && (txtCabeleireiroAgendar.getText().equals("")) && (txtManicureAgendar.getText().equals(""))
        && (jtpServicoAgendar.getText().equals(""))
         */

        category = "cliente";
        txtSubject = txtClienteAgendar.getText();

        try {
            if ((txtClienteAgendar.getText().equals("")) && (txtCabeleireiroAgendar.getText().equals(""))
                    && (txtManicureAgendar.getText().equals("")) && (jtpServicoAgendar.getText().equals(""))) {
                JOptionPane.showMessageDialog(null, "Selecione um item específico da tabela...");
            } else {
                agendaModel.setCliente(txtClienteAgendar.getText());
                agendaModel.setCabeleireiro(txtCabeleireiroAgendar.getText());
                agendaModel.setManicure(txtManicureAgendar.getText());
                agendaModel.setServico(jtpServicoAgendar.getText());
                agendaModel.setData(txtDataAgendar.getText());
                agendaModel.setData(txtDataAgendar.getText());
                agendaModel.setHorario(txtHorarioAgendar.getText());
                agendaModel.setValor_total(txtValorTotalServico.getText());

                agendaController.update(agendaModel, id);

                searchAgendado();
                cleanFieldAgendar();

                btnInsertAgedar.setEnabled(true);
                btnDeleteOneAgendado.setEnabled(false);
                btnUpdateAgendar.setEnabled(false);
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "ERRO EM ATUALIZAR AGENDAMENTO...\n" + e);
        }


    }//GEN-LAST:event_btnUpdateAgendarActionPerformed

    private void btnLimparServicoDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparServicoDataActionPerformed

        servicoCliente = "";
        valorTotal = 0.00f;
        jtpServicoAgendar.setText("");
        txtValorTotalServico.setText("0.00");

    }//GEN-LAST:event_btnLimparServicoDataActionPerformed

    private void btnDeleteGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteGroupActionPerformed
        // E X C L U I   G R U P O S   E S P E C Í F I C O S   A G E N D A D O S   D O   " D B "
        String categoria = category.toLowerCase();
        String txtNome = txtSubject;
        String dataInicio = txtDataDeleteGroupInicio.getText();
        String dataFim = txtDataDeleteGroupFim.getText();
        String sqlSubjectDelete = "delete from agendas where " + categoria + " = '" + txtNome + "'";
        String sqlDataDelete = "delete from agendas where " + categoria + " between '" + dataInicio + "' and '" + dataFim + "'";
        String sqlTruncate = "truncate table agendas";

        switch (category) {
            case "Cabeleireiro":
            case "Manicure":
            case "Cliente":
                sqlGroup(sqlSubjectDelete);
                break;
            case "Data":
                sqlGroup(sqlDataDelete);
                break;
            case "Todos":
                sqlGroup(sqlTruncate);
                break;
            default:
                break;
        }

    }//GEN-LAST:event_btnDeleteGroupActionPerformed

    private void jcbDeleteGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbDeleteGroupActionPerformed
// L I B E R A   E   R E S T R I N G E   C A M P O S:   E X C L U S Ã O   D E   G R U P O S   E M   A G E N D A
        category = (String) jcbDeleteGroup.getSelectedItem();
        txtSubject = txtNomeDeleteGroup.getText();

        switch (category) {
            case "Selecione Grupo":
                desableGroup();
                break;
            case "Cabeleireiro":
            case "Manicure":
            case "Cliente":
                btnDeleteGroup.setEnabled(true);
                lblNomeDeleteGroup.setEnabled(true);
                txtNomeDeleteGroup.setEnabled(true);
                lblDataDeleteGroupInicio.setEnabled(false);
                txtDataDeleteGroupInicio.setEnabled(false);
                lblDataDeleteGroupFim.setEnabled(false);
                txtDataDeleteGroupFim.setEnabled(false);
                break;
            case "Data":
                btnDeleteGroup.setEnabled(true);
                lblNomeDeleteGroup.setEnabled(false);
                txtNomeDeleteGroup.setEnabled(false);
                lblDataDeleteGroupInicio.setEnabled(true);
                txtDataDeleteGroupInicio.setEnabled(true);
                lblDataDeleteGroupFim.setEnabled(true);
                txtDataDeleteGroupFim.setEnabled(true);
                break;
            default:
                btnDeleteGroup.setEnabled(true);
                lblNomeDeleteGroup.setEnabled(false);
                txtNomeDeleteGroup.setEnabled(false);
                lblDataDeleteGroupInicio.setEnabled(false);
                txtDataDeleteGroupInicio.setEnabled(false);
                lblDataDeleteGroupFim.setEnabled(false);
                txtDataDeleteGroupFim.setEnabled(false);
                break;
        }

    }//GEN-LAST:event_jcbDeleteGroupActionPerformed

    private void txtNomeDeleteGroupKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomeDeleteGroupKeyReleased
// Lista em jTable Serviços Agendados
        txtSubject = txtNomeDeleteGroup.getText();

        searchAgendado();
    }//GEN-LAST:event_txtNomeDeleteGroupKeyReleased

    private void btnPermissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPermissionActionPerformed
        logView.limpar();
        logView.setSwitch = 2;
        logView.setVisible(true);
        //jcbDeleteGroup.setEnabled(jcb);

    }//GEN-LAST:event_btnPermissionActionPerformed

    private void txtDataDeleteGroupInicioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataDeleteGroupInicioKeyReleased
        category = "Data";
        searchAgendado();
    }//GEN-LAST:event_txtDataDeleteGroupInicioKeyReleased

    private void txtDataDeleteGroupFimKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDataDeleteGroupFimKeyReleased
        category = "Data";
        searchAgendado();
    }//GEN-LAST:event_txtDataDeleteGroupFimKeyReleased
// SEM USO
    private void txtNomeDeleteGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeDeleteGroupActionPerformed
    }//GEN-LAST:event_txtNomeDeleteGroupActionPerformed

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
            java.util.logging.Logger.getLogger(AgendaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgendaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgendaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgendaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AgendaView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCleanAllFields;
    private javax.swing.JButton btnDeleteCliente;
    private javax.swing.JButton btnDeleteGroup;
    private javax.swing.JButton btnDeleteOneAgendado;
    private javax.swing.JButton btnInsertAgedar;
    private javax.swing.JButton btnInsertCliente;
    private javax.swing.JButton btnLimparServicoData;
    private javax.swing.JButton btnPermission;
    private javax.swing.JButton btnResetAgendar;
    private javax.swing.JButton btnUpdateAgendar;
    private javax.swing.JButton btnUpdateCliente;
    private com.toedter.calendar.JCalendar jCalendar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    public static javax.swing.JComboBox<String> jcbDeleteGroup;
    private javax.swing.JTable jtAgendado;
    private javax.swing.JTable jtAgendarFuncionario;
    private javax.swing.JTable jtAgendarServico;
    private javax.swing.JTable jtCliente;
    private javax.swing.JTextPane jtpServicoAgendar;
    private javax.swing.JLabel lblClienteId;
    private javax.swing.JLabel lblDataDeleteGroupFim;
    private javax.swing.JLabel lblDataDeleteGroupInicio;
    private javax.swing.JLabel lblIdAgendado;
    private javax.swing.JLabel lblNomeDeleteGroup;
    private javax.swing.JMenu mnMainAdmin;
    private javax.swing.JTextArea taComplementoCliente;
    private javax.swing.JTextField txtCabeleireiroAgendar;
    private javax.swing.JTextField txtClienteAgendar;
    private javax.swing.JTextField txtDataAgendar;
    private javax.swing.JTextField txtDataDeleteGroupFim;
    private javax.swing.JTextField txtDataDeleteGroupInicio;
    private javax.swing.JTextField txtHorarioAgendar;
    private javax.swing.JLabel txtIdCliente;
    private javax.swing.JTextField txtManicureAgendar;
    private javax.swing.JTextField txtNomeCliente;
    private javax.swing.JTextField txtNomeDeleteGroup;
    private javax.swing.JTextField txtTelefoneCliente;
    private javax.swing.JTextField txtValorTotalServico;
    // End of variables declaration//GEN-END:variables
}
