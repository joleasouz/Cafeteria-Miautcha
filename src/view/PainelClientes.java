package view;

import data.dao.ClienteDAO;
import data.dao.PedidoDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.Cliente;

public class PainelClientes extends JPanel implements Interface {

    private JPanel painelListaClientes;
    private Cliente clienteSelecionado;
    private JTextField txtPesquisaCliente;
    private JButton btnPesquisarCliente;
    private JTextField txtNome, txtCpf, txtTelefone, txtEmail;
    private JTable tabelaHistorico;
    private DefaultTableModel modeloTabelaHistorico;
    private JButton btnCadastrar, btnRecarregar;
    private ClienteDAO clienteDAO;
    private PedidoDAO pedidoDAO;

    public PainelClientes() {
        clienteDAO = new ClienteDAO();
        pedidoDAO = new PedidoDAO();

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

        JPanel painelPesquisaCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        painelPesquisaCliente.setBackground(COR_FUNDO_PAINEL_ESCURO);
        txtPesquisaCliente = criarCampoCliente();
        txtPesquisaCliente = Interface.comTamanho(Interface.campoDados(" "), 125, 30);
        btnPesquisarCliente = Interface.botaoArredondado("Buscar", COR_BOTAO_PRIMARIO);
        painelPesquisaCliente.add(txtPesquisaCliente);
        painelPesquisaCliente.add(btnPesquisarCliente);
        painelTituloEsquerda.add(painelPesquisaCliente, BorderLayout.SOUTH);

        painelEsquerda.add(painelTituloEsquerda, BorderLayout.NORTH);

        painelListaClientes = new JPanel();
        painelListaClientes.setLayout(new BoxLayout(painelListaClientes, BoxLayout.Y_AXIS));
        painelListaClientes.setBackground(COR_FUNDO_PAINEL_ESCURO);
        painelListaClientes.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JScrollPane scrollLista = new JScrollPane(painelListaClientes);
        scrollLista.getVerticalScrollBar().setUnitIncrement(16);
        scrollLista.setBorder(BorderFactory.createEmptyBorder());
        painelEsquerda.add(scrollLista, BorderLayout.CENTER);

        add(painelEsquerda, BorderLayout.WEST);

        JPanel painelCentral = new JPanel(new BorderLayout(0, 15));
        painelCentral.setBackground(COR_FUNDO_PAINEL);

        // Painel do formulário com título e botão de recarregar alinhado à direita
        JPanel painelFormularioContainer = new JPanel(new BorderLayout());
        painelFormularioContainer.setBackground(COR_FUNDO_PAINEL);

        JPanel painelTituloFormulario = new JPanel(new BorderLayout());
        painelTituloFormulario.setBackground(COR_FUNDO_PAINEL);

        JLabel tituloFormulario = new JLabel("Cadastrar Cliente");
        tituloFormulario.setFont(new Font("SansSerif", Font.BOLD, 14));
        tituloFormulario.setForeground(COR_TITULO);
        tituloFormulario.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelTituloFormulario.add(tituloFormulario, BorderLayout.CENTER);

        // Botão de recarregar colocado no canto superior direito do formulário
        btnRecarregar = Interface.botaoRecarregar(this::recarregarTela);
        painelTituloFormulario.add(btnRecarregar, BorderLayout.EAST);

        painelFormularioContainer.add(painelTituloFormulario, BorderLayout.NORTH);

        JPanel painelFormulario = Interface.paineis(new GridBagLayout(), COR_FUNDO_PAINEL_ESCURO);
        painelFormulario.setPreferredSize(new Dimension(0, 100));
        painelFormularioContainer.add(painelFormulario, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = Interface.comTamanho(Interface.campoDados("Nome"), 125, 30);
        txtCpf = Interface.comTamanho(Interface.campoDados("CPF"), 125, 30);
        txtTelefone = Interface.comTamanho(Interface.campoDados("Telefone"), 125, 30);
        txtEmail = Interface.comTamanho(Interface.campoDados("Email"), 125, 30);
        btnCadastrar = Interface.botaoArredondado("Cadastrar", COR_BOTAO_PRIMARIO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        painelFormulario.add(txtNome, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        painelFormulario.add(txtCpf, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        painelFormulario.add(txtTelefone, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        painelFormulario.add(txtEmail, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.weightx = 0;
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

        String[] colunas = { "ID Pedido", "Nome Cliente", "CPF", "Data", "Itens", "Total (R$)" };
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

        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });

        btnPesquisarCliente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarClientes();
            }
        });

        carregarListaClientes();

        setVisible(true);
    }

    public void recarregarTela() {
        txtPesquisaCliente.setText("");
        limparFormulario();
        carregarListaClientes();
        modeloTabelaHistorico.setRowCount(0);
        clienteSelecionado = null;
    }

    private void estilizarTabela() {
        Interface.estilizarCabecalhoTabela(tabelaHistorico);
        tabelaHistorico.setRowHeight(24);
        tabelaHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void cadastrarCliente() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios (Nome e CPF).", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);

        boolean sucesso = clienteDAO.cadastrar(cliente);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
            carregarListaClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente no banco de dados.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel criarLinhaCliente(Cliente cliente) {
        JPanel linha = Interface.paineis(new GridLayout(3, 1), COR_FUNDO_PAINEL);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JLabel lblNome = new JLabel(cliente.getNome());
        lblNome.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblNome.setForeground(COR_TEXTO);
        linha.add(lblNome);

        JLabel lblCpf = new JLabel("CPF: " + cliente.getCpf());
        lblCpf.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCpf.setForeground(COR_TEXTO);
        linha.add(lblCpf);

        JLabel lblId = new JLabel("ID: " + cliente.getId());
        lblId.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblId.setForeground(COR_TEXTO);
        linha.add(lblId);

        linha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clienteSelecionado = cliente;
            }
        });

        return linha;
    }

    private void carregarListaClientes() {
        painelListaClientes.removeAll();
        for (Cliente c : clienteDAO.listar()) {
            painelListaClientes.add(criarLinhaCliente(c));
            painelListaClientes.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        painelListaClientes.revalidate();
        painelListaClientes.repaint();
    }

    private void pesquisarClientes() {
        String termo = txtPesquisaCliente.getText().trim();
        List<Cliente> resultado = termo.isEmpty() ? clienteDAO.listar() : clienteDAO.pesquisar(termo);
        painelListaClientes.removeAll();
        for (Cliente c : resultado) {
            painelListaClientes.add(criarLinhaCliente(c));
            painelListaClientes.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        painelListaClientes.revalidate();
        painelListaClientes.repaint();
    }

    private JTextField criarCampoCliente() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return campo;
    }

    private static class ClienteCellRenderer extends JPanel implements ListCellRenderer<Cliente> {
        private final JLabel lblNome = new JLabel();
        private final JLabel lblCpf = new JLabel();
        private final JLabel lblId = new JLabel();

        ClienteCellRenderer() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            lblNome.setFont(new Font("SansSerif", Font.BOLD, 12));
            lblCpf.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblId.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblNome.setForeground(COR_TEXTO);
            lblCpf.setForeground(COR_TEXTO);
            lblId.setForeground(COR_TEXTO);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, COR_FUNDO_PAINEL),
                    BorderFactory.createEmptyBorder(6, 8, 6, 8)));
            add(lblNome);
            add(lblCpf);
            add(lblId);
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Cliente> list, Cliente cliente, int index,
                boolean isSelected, boolean cellHasFocus) {
            lblNome.setText(cliente.getNome());
            lblCpf.setText("CPF: " + cliente.getCpf());
            lblId.setText("ID: " + cliente.getId());
            setBackground(isSelected ? COR_SELECAO_TABELA : Color.WHITE);
            return this;
        }
    }

    private void limparFormulario() {
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtNome.requestFocus();
    }
}