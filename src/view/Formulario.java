package view;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import data.dao.ProdutoDAO;
import model.Produto;

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

    private final List<Produto> catalogo = new ProdutoDAO().listar();
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
        setBackground(COR_FUNDO_PAINEL);
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
        cabecalho.setBackground(COR_CABECALHO);

        JLabel titulo = new JLabel("MIAUTCHA SYSTEM", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 26f));
        titulo.setForeground(COR_TEXTO_BOTAO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        cabecalho.add(titulo, BorderLayout.NORTH);

        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        navBar.setBackground(COR_CABECALHO);
        cabecalho.add(navBar, BorderLayout.SOUTH);

        return cabecalho;
    }

    // form de pedido
    private JComponent criarCorpo() {
        JPanel corpo = new JPanel(new BorderLayout(10, 10));
        corpo.setBackground(COR_FUNDO_PAINEL);
        corpo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        corpo.add(criarPainelCatalogo(), BorderLayout.WEST);
        corpo.add(criarPainelPedido(), BorderLayout.CENTER);

        return corpo;
    }

    // produtos
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
        listaProdutos.setBackground(COR_FUNDO_PAINEL);
        listaProdutos.setLayout(new BoxLayout(listaProdutos, BoxLayout.Y_AXIS));
        listaProdutos.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        for (ProdutoDemo produto : catalogo) {
            listaProdutos.add(criarLinhaProduto(produto));
            listaProdutos.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        JScrollPane scroll = new JScrollPane(listaProdutos);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarLinhaProduto(ProdutoDemo produto) {
        JPanel linha = Interface.paineis(new BorderLayout(10, 0), COR_FUNDO_PAINEL_ESCURO);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel textos = new JPanel(new GridLayout(3, 1));
        textos.setBackground(COR_FUNDO_PAINEL_ESCURO);
        textos.setOpaque(false);

        JLabel lblNome = new JLabel(produto.nome);
        lblNome.setForeground(COR_TEXTO);
        textos.add(lblNome);

        JLabel lblPreco = new JLabel(String.format("R$ %.2f", produto.preco));
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

    private void atualizarLinhaProduto(ProdutoDemo produto) {
        JLabel lblEstoque = labelsEstoque.get(produto);
        JButton btnAdicionar = botoesAdicionar.get(produto);

        if (produto.estoque <= 0) {
            lblEstoque.setText("Esgotado");
            lblEstoque.setForeground(COR_BOTAO_VERMELHO);
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

        JLabel lblCpf = new JLabel("CPF do cliente:");
        lblCpf.setForeground(COR_TEXTO);
        painel.add(lblCpf);

        txtCpf = new JTextField(15);
        painel.add(txtCpf);

        JButton btnBuscar = Interface.botaoArredondado("Buscar cliente", COR_BOTAO_PRIMARIO);
        btnBuscar.addActionListener(e -> buscarCliente());
        painel.add(btnBuscar);

        lblClienteEncontrado = new JLabel("Nenhum cliente selecionado");
        lblClienteEncontrado.setForeground(COR_TEXTO);
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

    /*public static void main(String[] args) {
        JFrame frame = new JFrame("Miautcha - Teste do Formulario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setLocationRelativeTo(null);
        frame.add(new Formulario());
        frame.setVisible(true);
    }*/
}