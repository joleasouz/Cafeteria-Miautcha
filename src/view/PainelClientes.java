package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PainelClientes extends JPanel implements Interface {

    private JList<String> listaClientesEsquerda;
    private DefaultListModel<String> modeloListaClientes;
    private JTextField txtNome, txtCpf, txtTelefone, txtEmail;
    private JTable tabelaHistorico;
    private DefaultTableModel modeloTabelaHistorico;
    private JButton btnCadastrar;

    public PainelClientes() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO_PAINEL);
        setBorder(new EmptyBorder(15, 15, 15, 15));


        JPanel painelEsquerda = new JPanel(new BorderLayout());
        painelEsquerda.setBackground(COR_FUNDO_PAINEL);
        painelEsquerda.setPreferredSize(new Dimension(220, 0));
        painelEsquerda.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_CABECALHO, 1, true),
                " Clientes Cadastrados "
        ));

        modeloListaClientes = new DefaultListModel<>();
        listaClientesEsquerda = new JList<>(modeloListaClientes);
        listaClientesEsquerda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollLista = new JScrollPane(listaClientesEsquerda);
        scrollLista.setBorder(BorderFactory.createEmptyBorder());
        painelEsquerda.add(scrollLista, BorderLayout.CENTER);

        add(painelEsquerda, BorderLayout.WEST);


        JPanel painelCentral = new JPanel(new BorderLayout(0, 15));
        painelCentral.setBackground(COR_FUNDO_PAINEL);

        
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBackground(COR_FUNDO_PAINEL);
        painelFormulario.setPreferredSize(new Dimension(0, 100));
        painelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_CABECALHO, 1, true),
                " Cadastrar Novo Cliente "
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField();
        txtCpf = new JTextField();
        txtTelefone = new JTextField();
        txtEmail = new JTextField();
        btnCadastrar = new JButton("Cadastrar");

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        painelFormulario.add(criarRotulo("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        painelFormulario.add(txtNome, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        painelFormulario.add(criarRotulo("CPF:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        painelFormulario.add(txtCpf, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painelFormulario.add(criarRotulo("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        painelFormulario.add(txtTelefone, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0;
        painelFormulario.add(criarRotulo("E-mail:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        painelFormulario.add(txtEmail, gbc);

        gbc.gridx = 4; gbc.gridy = 1; gbc.weightx = 0;
        painelFormulario.add(btnCadastrar, gbc);

        painelCentral.add(painelFormulario, BorderLayout.NORTH);
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(COR_FUNDO_PAINEL);
        painelTabela.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_CABECALHO, 1, true),
                " Histórico de Pedidos "
        ));

        String[] colunas = {"ID Pedido", "Nome Cliente", "CPF", "Data", "Itens", "Total (R$)"};
        modeloTabelaHistorico = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaHistorico = new JTable(modeloTabelaHistorico);
        estilizarTabela();

        JScrollPane scrollTabela = new JScrollPane(tabelaHistorico);
        scrollTabela.getViewport().setBackground(Color.WHITE);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder());

        painelTabela.add(scrollTabela, BorderLayout.CENTER);
        painelCentral.add(painelTabela, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        setVisible(true);
    }

    private JLabel criarRotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(COR_CABECALHO);
        return label;
    }

    private void estilizarTabela() {
        JTableHeader header = tabelaHistorico.getTableHeader();
        header.setBackground(COR_CABECALHO);
        header.setForeground(Color.WHITE);

        tabelaHistorico.setRowHeight(24);
        tabelaHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}