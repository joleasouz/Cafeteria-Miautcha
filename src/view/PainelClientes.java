package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

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

        JPanel painelEsquerda = Interface.paineis(new BorderLayout(), COR_FUNDO_PAINEL_ESCURO);
        painelEsquerda.setPreferredSize(new Dimension(220, 0));

        // Título do painel esquerdo
        JPanel painelTituloEsquerda = new JPanel(new BorderLayout());
        painelTituloEsquerda.setBackground(COR_FUNDO_PAINEL_ESCURO);
        
        JLabel tituloEsquerda = new JLabel("Clientes");
        tituloEsquerda.setFont(new Font("SansSerif", Font.BOLD, 14));
        tituloEsquerda.setForeground(COR_TITULO);
        tituloEsquerda.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelTituloEsquerda.add(tituloEsquerda, BorderLayout.CENTER);

        painelEsquerda.add(painelTituloEsquerda, BorderLayout.NORTH);

        modeloListaClientes = new DefaultListModel<>();
        listaClientesEsquerda = new JList<>(modeloListaClientes);
        listaClientesEsquerda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollLista = new JScrollPane(listaClientesEsquerda);
        scrollLista.setBorder(BorderFactory.createEmptyBorder());
        painelEsquerda.add(scrollLista, BorderLayout.CENTER);

        add(painelEsquerda, BorderLayout.WEST);


        JPanel painelCentral = new JPanel(new BorderLayout(0, 15));
        painelCentral.setBackground(COR_FUNDO_PAINEL);

        // Painel do formulário com título
        JPanel painelFormularioContainer = new JPanel(new BorderLayout());
        painelFormularioContainer.setBackground(COR_FUNDO_PAINEL);

        JPanel painelTituloFormulario = new JPanel(new BorderLayout());
        painelTituloFormulario.setBackground(COR_FUNDO_PAINEL);
        
        JLabel tituloFormulario = new JLabel("Cadastrar Cliente");
        tituloFormulario.setFont(new Font("SansSerif", Font.BOLD, 14));
        tituloFormulario.setForeground(COR_TITULO);
        tituloFormulario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelTituloFormulario.add(tituloFormulario, BorderLayout.CENTER);

        painelFormularioContainer.add(painelTituloFormulario, BorderLayout.NORTH);
        
        JPanel painelFormulario = Interface.paineis(new GridBagLayout(), COR_FUNDO_PAINEL_ESCURO);
        painelFormulario.setPreferredSize(new Dimension(0, 100));
        painelFormularioContainer.add(painelFormulario, BorderLayout.CENTER);

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

        painelCentral.add(painelFormularioContainer, BorderLayout.NORTH);
        
        // Painel da tabela com título
        JPanel painelTabelaContainer = new JPanel(new BorderLayout());
        painelTabelaContainer.setBackground(COR_FUNDO_PAINEL);

        JPanel painelTituloTabela = new JPanel(new BorderLayout());
        painelTituloTabela.setBackground(COR_FUNDO_PAINEL);
        
        JLabel tituloTabela = new JLabel("Histórico de Pedidos");
        tituloTabela.setFont(new Font("SansSerif", Font.BOLD, 14));
        tituloTabela.setForeground(COR_TITULO);
        tituloTabela.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelTituloTabela.add(tituloTabela, BorderLayout.CENTER);

        painelTabelaContainer.add(painelTituloTabela, BorderLayout.NORTH);
        
        JPanel painelTabela = Interface.paineis(new BorderLayout(), COR_FUNDO_PAINEL_ESCURO);
        painelTabela.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        painelTabelaContainer.add(painelTabela, BorderLayout.CENTER);
        painelCentral.add(painelTabelaContainer, BorderLayout.CENTER);

        add(painelCentral, BorderLayout.CENTER);

        setVisible(true);
    }

    private JLabel criarRotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(COR_CABECALHO);
        return label;
    }

    private void estilizarTabela() {
        Interface.estilizarCabecalhoTabela(tabelaHistorico);
        tabelaHistorico.setRowHeight(24);
        tabelaHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}