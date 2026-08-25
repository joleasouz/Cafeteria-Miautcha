package view;
//main temporario no final, Jolea do futuo NÂO ESQUECE DE TIRAR e fazer o bglh lá do crud
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Formulario extends JPanel implements Interface {

    private static class ProdutoDemo {
        String nome;
        double preco;
        int estoque;

        ProdutoDemo(String nome, double preco, int estoque) {
            this.nome = nome;
            this.preco = preco;
            this.estoque = estoque;
        }
    }

    private static class ItemCarrinho {
        ProdutoDemo produto;
        int quantidade;
        double precoUnitario;

        ItemCarrinho(ProdutoDemo produto, int quantidade, double precoUnitario) {
            this.produto = produto;
            this.quantidade = quantidade;
            this.precoUnitario = precoUnitario;
        }

        double calcularSubtotal() {
            return quantidade * precoUnitario;
        }
    }

    private final List<ProdutoDemo> catalogo = criarCatalogoDemo();
    private final List<String> clientesDemo = criarClientesDemo();
    private final List<ItemCarrinho> itensPedido = new ArrayList<>();

    private DefaultTableModel modeloTabela;
    private JTable tabelaItens;
    private JLabel lblTotal;
    private JTextField txtCpf;
    private JLabel lblClienteEncontrado;
    private String clienteSelecionado;

    private final java.util.Map<ProdutoDemo, JLabel> labelsEstoque = new java.util.HashMap<>();
    private final java.util.Map<ProdutoDemo, JButton> botoesAdicionar = new java.util.HashMap<>();

    private final Consumer<String> aoNavegar;

    public Formulario() {
        this(destino -> { /* sem nada*/ });
    }

    public Formulario(Consumer<String> aoNavegar) {
        this.aoNavegar = aoNavegar;

        setLayout(new BorderLayout());
        add(criarCabecalho(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
    }

    /*   --- Jolea
                Dados de teste com os arraylist que nem eu comentei no grupo
            
        --- Doda
                fiz o arquivo de produtos com base nessas entradas*/
    private static List<ProdutoDemo> criarCatalogoDemo() {
        List<ProdutoDemo> lista = new ArrayList<>();
        lista.add(new ProdutoDemo("Miautcha", 20.90, 100));
        lista.add(new ProdutoDemo("Nyan Coffee", 16.90, 100));
        lista.add(new ProdutoDemo("Cattuccino", 17.90, 50));
        lista.add(new ProdutoDemo("Neko Latte", 16.90, 30));
        return lista;
    }

    private static List<String> criarClientesDemo() {
        List<String> lista = new ArrayList<>();
        lista.add("Maria Silva - 111.111.111-11");
        return lista;
    }

    private JComponent criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());

        JLabel titulo = new JLabel("MIAUTCHA SYSTEM", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 26f));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        cabecalho.add(titulo, BorderLayout.NORTH);

        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        navBar.setBackground(new Color(184, 191, 96));
        navBar.add(criarBotaoNav("Pedido", "pedido"));
        navBar.add(criarBotaoNav("Clientes", "clientes"));
        navBar.add(criarBotaoNav("Estoque", "estoque"));
        navBar.add(criarBotaoNav("Comandas", "comandas"));
        cabecalho.add(navBar, BorderLayout.SOUTH);

        return cabecalho;
    }

    private JButton criarBotaoNav(String texto, String destino) {
        JButton botao = new JButton(texto);
        botao.addActionListener(e -> aoNavegar.accept(destino));
        return botao;
    }

    // form de pedido
    private JComponent criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(10, 10));
        corpo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        corpo.add(criarPainelCatalogo(), BorderLayout.WEST);
        corpo.add(criarPainelPedido(), BorderLayout.CENTER);

        return corpo;
    }

    // produtos
    private JComponent criarPainelCatalogo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setPreferredSize(new Dimension(320, 0));
        painel.setBorder(BorderFactory.createTitledBorder("Cardápio"));

        JPanel listaProdutos = new JPanel();
        listaProdutos.setLayout(new BoxLayout(listaProdutos, BoxLayout.Y_AXIS));

        for (ProdutoDemo produto : catalogo) {
            listaProdutos.add(criarLinhaProduto(produto));
            listaProdutos.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        JScrollPane scroll = new JScrollPane(listaProdutos);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarLinhaProduto(ProdutoDemo produto) {
        JPanel linha = new JPanel(new BorderLayout(10, 0));
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        linha.setBorder(BorderFactory.createEtchedBorder());

        JPanel textos = new JPanel(new GridLayout(3, 1));
        textos.add(new JLabel(produto.nome));
        textos.add(new JLabel(String.format("R$ %.2f", produto.preco)));

        JLabel lblEstoque = new JLabel();
        textos.add(lblEstoque);
        labelsEstoque.put(produto, lblEstoque);

        JButton btnAdicionar = new JButton("+");
        btnAdicionar.addActionListener(e -> adicionarItemAoPedido(produto));
        botoesAdicionar.put(produto, btnAdicionar);

        atualizarLinhaProduto(produto);

        linha.add(textos, BorderLayout.CENTER);
        linha.add(btnAdicionar, BorderLayout.EAST);

        return linha;
    }

    private void atualizarLinhaProduto(ProdutoDemo produto) {
        JLabel lblEstoque = labelsEstoque.get(produto);
        JButton btnAdicionar = botoesAdicionar.get(produto);

        if (produto.estoque <= 0) {
            lblEstoque.setText("Esgotado");
            lblEstoque.setForeground(COR_TEXTO);
            btnAdicionar.setEnabled(false);
        } else {
            lblEstoque.setText("Estoque: " + produto.estoque);
            lblEstoque.setForeground(COR_TEXTO);
            btnAdicionar.setEnabled(true);
        }
    }

    // painel de pedido
    private JComponent criarPainelPedido() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createTitledBorder("Novo Pedido"));

        painel.add(criarPainelCliente(), BorderLayout.NORTH);
        painel.add(criarPainelTabelaItens(), BorderLayout.CENTER);
        painel.add(criarPainelRodape(), BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelCliente() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.add(new JLabel("CPF do cliente:"));

        txtCpf = new JTextField(15);
        painel.add(txtCpf);

        JButton btnBuscar = new JButton("Buscar cliente");
        btnBuscar.addActionListener(e -> buscarCliente());
        painel.add(btnBuscar);

        lblClienteEncontrado = new JLabel("Nenhum cliente selecionado");
        painel.add(lblClienteEncontrado);

        return painel;
    }

    private JComponent criarPainelTabelaItens() {
        String[] colunas = {"Produto", "Qtd", "Preço unit.", "Subtotal"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tabelaItens = new JTable(modeloTabela);
        return new JScrollPane(tabelaItens);
    }

    private JPanel criarPainelRodape() {
        JPanel painel = new JPanel(new BorderLayout());

        JButton btnRemover = new JButton("Remover item selecionado");
        btnRemover.addActionListener(e -> removerItemSelecionado());

        JButton btnCancelar = new JButton("Cancelar pedido");
        btnCancelar.addActionListener(e -> cancelarPedido());

        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 16f));

        JButton btnFinalizar = new JButton("Finalizar Pedido");
        btnFinalizar.addActionListener(e -> finalizarPedido());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.add(btnRemover);
        botoes.add(btnCancelar);
        botoes.add(btnFinalizar);

        painel.add(lblTotal, BorderLayout.WEST);
        painel.add(botoes, BorderLayout.EAST);

        return painel;
    }



    private void adicionarItemAoPedido(ProdutoDemo produto) {
        String qtdStr = JOptionPane.showInputDialog(this,
                "Quantidade de " + produto.nome + " (estoque: " + produto.estoque + "):", "1");
        if (qtdStr == null) return;

        int quantidade;
        try {
            quantidade = Integer.parseInt(qtdStr.trim());
            if (quantidade <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (quantidade > produto.estoque) {
            JOptionPane.showMessageDialog(this,
                    "Estoque insuficiente. Disponível: " + produto.estoque,
                    "Estoque insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }

        produto.estoque -= quantidade;
        atualizarLinhaProduto(produto);

        ItemCarrinho item = new ItemCarrinho(produto, quantidade, produto.preco);
        itensPedido.add(item);

        modeloTabela.addRow(new Object[]{
                produto.nome,
                quantidade,
                String.format("R$ %.2f", produto.preco),
                String.format("R$ %.2f", item.calcularSubtotal())
        });

        atualizarTotal();
    }

    private void removerItemSelecionado() {
        int linha = tabelaItens.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemCarrinho item = itensPedido.get(linha);
        devolverEstoque(item);
        itensPedido.remove(linha);
        modeloTabela.removeRow(linha);
        atualizarTotal();
    }

    private void cancelarPedido() {
        for (ItemCarrinho item : itensPedido) {
            devolverEstoque(item);
        }
        itensPedido.clear();
        modeloTabela.setRowCount(0);
        txtCpf.setText("");
        lblClienteEncontrado.setText("Nenhum cliente selecionado");
        clienteSelecionado = null;
        atualizarTotal();
    }

    private void devolverEstoque(ItemCarrinho item) {
        item.produto.estoque += item.quantidade;
        atualizarLinhaProduto(item.produto);
    }

    private void atualizarTotal() {
        double total = 0;
        for (ItemCarrinho item : itensPedido) {
            total += item.calcularSubtotal();
        }
        lblTotal.setText(String.format("Total: R$ %.2f", total));
    }

    private void buscarCliente() {
        String cpf = txtCpf.getText().trim();
        if (cpf.isEmpty()) return;

        clienteSelecionado = null;
        for (String c : clientesDemo) {
            if (c.contains(cpf)) {
                clienteSelecionado = c;
                break;
            }
        }

        lblClienteEncontrado.setText(clienteSelecionado != null
                ? "Cliente: " + clienteSelecionado
                : "Cliente não encontrado");
    }

    private void finalizarPedido() {
        if (itensPedido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione ao menos um item antes de finalizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder resumo = new StringBuilder("Pedido registrado (modo de teste):\n");
        for (ItemCarrinho item : itensPedido) {
            resumo.append(String.format("- %dx %s = R$ %.2f%n", item.quantidade, item.produto.nome, item.calcularSubtotal()));
        }
        JOptionPane.showMessageDialog(this, resumo.toString());

        itensPedido.clear();
        modeloTabela.setRowCount(0);
        txtCpf.setText("");
        lblClienteEncontrado.setText("Nenhum cliente selecionado");
        clienteSelecionado = null;
        atualizarTotal();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Miautcha - Teste do Formulario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setLocationRelativeTo(null);
        frame.add(new Formulario());
        frame.setVisible(true);
    }
}