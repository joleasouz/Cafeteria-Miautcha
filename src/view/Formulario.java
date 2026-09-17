package view;

import data.dao.ClienteDAO;
import data.dao.PedidoDAO;
import data.dao.ProdutoDAO;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Cliente;
import model.ItemPedido;
import model.Pedido;
import model.Produto;
import model.StatusPedido;

public class Formulario extends JPanel implements Interface {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final List<Produto> catalogo = produtoDAO.listar();
    private final List<ItemPedido> itensPedido = new ArrayList<>();

    private DefaultTableModel modeloTabela;
    private JTable tabelaItens;
    private JLabel lblTotal;
    private JTextField txtCpf;
    private JTextField txtMesa;
    private JLabel lblClienteEncontrado;
    private Cliente clienteSelecionado;

    private final Map<Produto, JLabel> labelsEstoque = new HashMap<>();
    private final Map<Produto, JButton> botoesAdicionar = new HashMap<>();

    private final Consumer<String> aoNavegar;

    public Formulario() {
        this(destino -> {
            /* sem nada */ });
    }

    public Formulario(Consumer<String> aoNavegar) {
        this.aoNavegar = aoNavegar;

        setLayout(new BorderLayout());
        setBackground(COR_FUNDO_PAINEL);
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
    }

    private JComponent criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(COR_CABECALHO);
        cabecalho.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Carrega a imagem da pasta src/assets/
        ImageIcon iconOriginal = new ImageIcon(getClass().getResource("/assets/miautcha_cabecalho.png"));
        
        // Redimensiona a imagem para uma altura adequada de cabeçalho (ex: 50px mantendo proporção)
        Image img = iconOriginal.getImage();
        int larguraProporcional = (int) ((double) img.getWidth(null) / img.getHeight(null) * 50);
        ImageIcon iconRedimensionado = new ImageIcon(img.getScaledInstance(larguraProporcional, 50, Image.SCALE_SMOOTH));

        // Label contendo apenas a foto no centro do cabeçalho
        JLabel lblImagemCabecalho = new JLabel(iconRedimensionado, SwingConstants.CENTER);

        JButton btnRecarregar = Interface.botaoRecarregar(this::recarregarTela);

        cabecalho.add(lblImagemCabecalho, BorderLayout.CENTER);
        cabecalho.add(btnRecarregar, BorderLayout.EAST);

        return cabecalho;
    }

    private JComponent criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(10, 10));
        corpo.setBackground(COR_FUNDO_PAINEL);
        corpo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        corpo.add(criarPainelCatalogo(), BorderLayout.WEST);
        corpo.add(criarPainelPedido(), BorderLayout.CENTER);

        return corpo;
    }

    private JComponent criarPainelCatalogo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setPreferredSize(new Dimension(320, 0));
        painel.setBackground(COR_FUNDO_PAINEL);

        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBackground(COR_FUNDO_PAINEL);
        painelTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COR_TITULO));

        JLabel titulo = new JLabel("Cardápio");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelTitulo.add(titulo, BorderLayout.CENTER);

        painel.add(painelTitulo, BorderLayout.NORTH);

        JPanel listaProdutos = new JPanel();
        listaProdutos.setName("listaProdutosContainer");
        listaProdutos.setBackground(COR_FUNDO_PAINEL);
        listaProdutos.setLayout(new BoxLayout(listaProdutos, BoxLayout.Y_AXIS));
        listaProdutos.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        for (Produto produto : catalogo) {
            listaProdutos.add(criarLinhaProduto(produto));
            listaProdutos.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        JScrollPane scroll = new JScrollPane(listaProdutos);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarLinhaProduto(Produto produto) {
        JPanel linha = Interface.paineis(new BorderLayout(10, 0), COR_FUNDO_PAINEL_ESCURO);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel textos = new JPanel(new GridLayout(3, 1));
        textos.setBackground(COR_FUNDO_PAINEL_ESCURO);
        textos.setOpaque(false);

        JLabel lblNome = new JLabel(produto.getNome());
        lblNome.setForeground(COR_TEXTO);
        textos.add(lblNome);

        JLabel lblPreco = new JLabel(String.format("R$ %.2f", produto.getValor()));
        lblPreco.setForeground(COR_TEXTO);
        textos.add(lblPreco);

        JLabel lblEstoque = new JLabel();
        textos.add(lblEstoque);
        labelsEstoque.put(produto, lblEstoque);

        JButton btnAdicionar = Interface.botaoArredondado("+", COR_BOTAO_VERDE);
        btnAdicionar.addActionListener(e -> adicionarItemAoPedido(produto));
        botoesAdicionar.put(produto, btnAdicionar);

        atualizarLinhaProduto(produto);

        linha.add(textos, BorderLayout.CENTER);
        linha.add(btnAdicionar, BorderLayout.EAST);

        return linha;
    }

    public void recarregarTela() {
        for (ItemPedido item : itensPedido) {
            devolverEstoque(item);
        }
        itensPedido.clear();
        modeloTabela.setRowCount(0);
        txtCpf.setText("");
        txtMesa.setText("");
        lblClienteEncontrado.setText("Nenhum cliente selecionado");
        clienteSelecionado = null;
        atualizarTotal();

        catalogo.clear();
        catalogo.addAll(produtoDAO.listar());

        labelsEstoque.clear();
        botoesAdicionar.clear();

        for (Component comp : getComponents()) {
            reconstruirCatalogoVisual(this);
        }

        revalidate();
        repaint();
    }

    private void reconstruirCatalogoVisual(Container container) {
        for (Component c : container.getComponents()) {
            if ("listaProdutosContainer".equals(c.getName()) && c instanceof JPanel) {
                JPanel listaProdutos = (JPanel) c;
                listaProdutos.removeAll();
                for (Produto produto : catalogo) {
                    listaProdutos.add(criarLinhaProduto(produto));
                    listaProdutos.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                listaProdutos.revalidate();
                listaProdutos.repaint();
                return;
            }
            if (c instanceof Container) {
                reconstruirCatalogoVisual((Container) c);
            }
        }
    }

    private void atualizarLinhaProduto(Produto produto) {
        JLabel lblEstoque = labelsEstoque.get(produto);
        JButton btnAdicionar = botoesAdicionar.get(produto);

        if (produto.getQntdEstoque() <= 0) {
            lblEstoque.setText("Esgotado");
            lblEstoque.setForeground(COR_BOTAO_VERMELHO);
            btnAdicionar.setEnabled(false);
        } else {
            lblEstoque.setText("Estoque: " + produto.getQntdEstoque());
            lblEstoque.setForeground(COR_TEXTO);
            btnAdicionar.setEnabled(true);
        }
    }

    private JComponent criarPainelPedido() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(COR_FUNDO_PAINEL);

        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBackground(COR_FUNDO_PAINEL);
        painelTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COR_TITULO));

        JLabel titulo = new JLabel("Novo Pedido");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelTitulo.add(titulo, BorderLayout.CENTER);

        painel.add(painelTitulo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel(new BorderLayout(10, 10));
        conteudo.setBackground(COR_FUNDO_PAINEL);
        conteudo.add(criarPainelCliente(), BorderLayout.NORTH);
        conteudo.add(criarPainelTabelaItens(), BorderLayout.CENTER);
        conteudo.add(criarPainelRodape(), BorderLayout.SOUTH);

        painel.add(conteudo, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelCliente() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.setBackground(COR_FUNDO_PAINEL);

        txtCpf = Interface.comTamanho(Interface.campoDados("CPF do cliente"), 200, 35);
        painel.add(txtCpf);

        JButton btnBuscar = Interface.botaoArredondado("Buscar cliente", COR_BOTAO_PRIMARIO);
        btnBuscar.addActionListener(e -> buscarCliente());
        painel.add(btnBuscar);

        lblClienteEncontrado = new JLabel("Nenhum cliente selecionado");
        lblClienteEncontrado.setForeground(COR_TEXTO);
        painel.add(lblClienteEncontrado);

        txtMesa = Interface.comTamanho(Interface.campoDados("Mesa"), 60, 35);
        painel.add(txtMesa);

        return painel;
    }

    private JComponent criarPainelTabelaItens() {
        String[] colunas = { "Produto", "Qtd", "Preço unit.", "Subtotal" };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabelaItens = new JTable(modeloTabela);
        tabelaItens.setSelectionBackground(COR_SELECAO_TABELA);
        tabelaItens.setSelectionForeground(COR_TEXTO);
        tabelaItens.setShowVerticalLines(false);

        Interface.estilizarCabecalhoTabela(tabelaItens);
        return new JScrollPane(tabelaItens);
    }

    private JPanel criarPainelRodape() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO_PAINEL);

        JButton btnRemover = Interface.botaoArredondado("Remover item selecionado", COR_BOTAO_VERMELHO);
        btnRemover.addActionListener(e -> removerItemSelecionado());

        JButton btnCancelar = Interface.botaoArredondado("Cancelar pedido", COR_BOTAO_VERMELHO);
        btnCancelar.addActionListener(e -> cancelarPedido());

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 16f));
        lblTotal.setForeground(COR_TITULO);

        JButton btnFinalizar = Interface.botaoArredondado("Finalizar Pedido", COR_BOTAO_VERDE);
        btnFinalizar.addActionListener(e -> finalizarPedido());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.setBackground(COR_FUNDO_PAINEL);
        botoes.add(btnRemover);
        botoes.add(btnCancelar);
        botoes.add(btnFinalizar);

        painel.add(lblTotal, BorderLayout.WEST);
        painel.add(botoes, BorderLayout.EAST);

        return painel;
    }

    private void adicionarItemAoPedido(Produto produto) {
        String qtdStr = JOptionPane.showInputDialog(this,
                "Quantidade de " + produto.getNome() + " (estoque: " + produto.getQntdEstoque() + "):", "1");
        if (qtdStr == null)
            return;

        int quantidade;
        try {
            quantidade = Integer.parseInt(qtdStr.trim());
            if (quantidade <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (quantidade > produto.getQntdEstoque()) {
            JOptionPane.showMessageDialog(this,
                    "Estoque insuficiente. Disponível: " + produto.getQntdEstoque(),
                    "Estoque insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        produto.setQntdEstoque(produto.getQntdEstoque() - quantidade);
        atualizarLinhaProduto(produto);

        ItemPedido item = new ItemPedido(0, produto, quantidade, produto.getValor());
        itensPedido.add(item);

        modeloTabela.addRow(new Object[] {
                produto.getNome(),
                quantidade,
                String.format("R$ %.2f", produto.getValor()),
                String.format("R$ %.2f", item.calcularSubtotal())
        });

        atualizarTotal();
    }

    private void removerItemSelecionado() {
        int linha = tabelaItens.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemPedido item = itensPedido.get(linha);
        devolverEstoque(item);
        itensPedido.remove(linha);
        modeloTabela.removeRow(linha);
        atualizarTotal();
    }

    private void cancelarPedido() {
        for (ItemPedido item : itensPedido) {
            devolverEstoque(item);
        }
        itensPedido.clear();
        modeloTabela.setRowCount(0);
        txtCpf.setText("");
        txtMesa.setText("");
        lblClienteEncontrado.setText("Nenhum cliente selecionado");
        clienteSelecionado = null;
        atualizarTotal();
    }

    private void devolverEstoque(ItemPedido item) {
        Produto produto = item.getProduto();
        produto.setQntdEstoque(produto.getQntdEstoque() + item.getQuantidade());
        atualizarLinhaProduto(produto);
    }

    private void atualizarTotal() {
        double total = 0;
        for (ItemPedido item : itensPedido) {
            total += item.calcularSubtotal();
        }
        lblTotal.setText(String.format("Total: R$ %.2f", total));
    }

    private void buscarCliente() {
        String cpf = txtCpf.getText().trim();
        if (cpf.isEmpty())
            return;

        clienteSelecionado = null;
        for (Cliente c : clienteDAO.listar()) {
            if (c.getCpf() != null && c.getCpf().contains(cpf)) {
                clienteSelecionado = c;
                break;
            }
        }

        lblClienteEncontrado.setText(clienteSelecionado != null
                ? "Cliente: " + clienteSelecionado.getNome() + " (" + clienteSelecionado.getCpf() + ")"
                : "Cliente não encontrado");
    }

    private void finalizarPedido() {
        if (itensPedido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos um item antes de finalizar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (clienteSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Busque e selecione um cliente antes de finalizar.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int numeroMesa;
        try {
            numeroMesa = Integer.parseInt(txtMesa.getText().trim());
        } catch (NumberFormatException ex) {
            numeroMesa = 0;
        }

        Pedido pedido = new Pedido(0, clienteSelecionado, LocalDateTime.now(), StatusPedido.pendente(), 0, numeroMesa);
        for (ItemPedido item : itensPedido) {
            pedido.adicionarItem(item);
        }

        boolean sucesso = pedidoDAO.salvar(pedido);
        if (!sucesso) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o pedido no banco de dados.", "Erro MySQL",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (ItemPedido item : itensPedido) {
            produtoDAO.ajustarEstoque(item.getProduto().getId(), item.getQuantidade(), false);
        }

        StringBuilder resumo = new StringBuilder("Pedido registrado com sucesso!\n");
        for (ItemPedido item : itensPedido) {
            resumo.append(String.format("- %dx %s = R$ %.2f%n",
                    item.getQuantidade(), item.getProduto().getNome(), item.calcularSubtotal()));
        }
        resumo.append(String.format("Total: R$ %.2f", pedido.getValorTotal()));
        JOptionPane.showMessageDialog(this, resumo.toString());

        itensPedido.clear();
        modeloTabela.setRowCount(0);
        txtCpf.setText("");
        txtMesa.setText("");
        lblClienteEncontrado.setText("Nenhum cliente selecionado");
        clienteSelecionado = null;
        atualizarTotal();
    }
}