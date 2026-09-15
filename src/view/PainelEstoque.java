package view;

import data.dao.ProdutoDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Produto;

public final class PainelEstoque extends JPanel implements Interface {

    private final JTable tabela;
    private final DefaultTableModel modeloTabela;
    private final JTextField txtNome, txtPreco, txtQtd, txtPesquisa, txtAjusteQtd;
    private final JButton btnCadastrar, btnExcluir, btnPesquisar, btnAtualizar, btnAdicionarEstoque, btnRemoverEstoque;
    private final JLabel lblStatusEstoque;
    private int idProdutoSelecionado = -1;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public PainelEstoque() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO_PAINEL);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Painel superior - Cadastro de produtos
        txtNome = Interface.comTamanho(Interface.campoDados("Nome do Produto"), 200, 35);
        txtPreco = Interface.comTamanho(Interface.campoDados("Preço (R$)"), 120, 35);
        txtQtd = Interface.comTamanho(Interface.campoDados("Qtd Inicial"), 100, 35);
        btnCadastrar = Interface.comTamanho(Interface.botaoArredondado("Cadastrar Produto", COR_BOTAO_PRIMARIO), 160, 35);

        JPanel painelFormulario = Interface.criarPainelFormulario(80, txtNome, txtPreco, txtQtd, btnCadastrar);
        add(painelFormulario, BorderLayout.NORTH);

        // Painel central - Tabela de produtos
        modeloTabela = new DefaultTableModel(new Object[] { "ID", "Nome", "Preço (R$)", "Qtd Estoque", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabela = new JTable(modeloTabela);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabela.setRowHeight(28);
        tabela.setSelectionBackground(COR_SELECAO_TABELA);
        tabela.setSelectionForeground(COR_TEXTO);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(new Color(230, 230, 230));

        // Estilização do cabeçalho da tabela
        Interface.estilizarCabecalhoTabela(tabela);

        // Centralizar texto nas colunas numéricas e de status
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tabela.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior - Controle de estoque e ações
        JPanel painelInferior = new JPanel(new GridLayout(2, 1, 10, 10));
        painelInferior.setBackground(COR_FUNDO_PAINEL);

        // Subpainel de ajuste de estoque
        JPanel painelAjuste = Interface.paineis(new FlowLayout(FlowLayout.LEFT, 12, 5), COR_FUNDO_PAINEL_ESCURO);

        txtAjusteQtd = criarCampoTexto();
        txtAjusteQtd.setPreferredSize(new Dimension(60, 28));
        btnAdicionarEstoque = Interface.botaoArredondado("+ Entrada", COR_BOTAO_VERDE);
        btnRemoverEstoque = Interface.botaoArredondado("- Baixa", COR_BOTAO_VERMELHO);

        lblStatusEstoque = new JLabel("Status: Selecione um produto");
        lblStatusEstoque.setFont(new Font("SansSerif", Font.BOLD, 12));

        painelAjuste.add(criarRotulo("Qtd Ajuste:"));
        painelAjuste.add(txtAjusteQtd);
        painelAjuste.add(btnAdicionarEstoque);
        painelAjuste.add(btnRemoverEstoque);
        painelAjuste.add(Box.createHorizontalStrut(20));
        painelAjuste.add(lblStatusEstoque);

        // Subpainel de busca e ações
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        painelAcoes.setBackground(COR_FUNDO_PAINEL);

        txtPesquisa = criarCampoTexto();
        txtPesquisa.setPreferredSize(new Dimension(160, 28));
        btnPesquisar = Interface.botaoArredondado("Pesquisar", COR_BOTAO_PRIMARIO);
        btnAtualizar = Interface.botaoArredondado("Atualizar Tabela", COR_BOTAO_PRIMARIO);
        btnExcluir = Interface.botaoArredondado("Excluir Produto", COR_BOTAO_VERMELHO);

        painelAcoes.add(criarRotulo("Buscar Nome:"));
        painelAcoes.add(txtPesquisa);
        painelAcoes.add(btnPesquisar);
        painelAcoes.add(btnAtualizar);
        painelAcoes.add(Box.createHorizontalStrut(30));
        painelAcoes.add(btnExcluir);

        painelInferior.add(painelAjuste);
        painelInferior.add(painelAcoes);

        add(painelInferior, BorderLayout.SOUTH);

        // --- Eventos ---
        btnCadastrar.addActionListener((ActionEvent e) -> cadastrarProduto());
        btnAdicionarEstoque.addActionListener((ActionEvent e) -> alterarEstoque(true));
        btnRemoverEstoque.addActionListener((ActionEvent e) -> alterarEstoque(false));
        btnAtualizar.addActionListener((ActionEvent e) -> {
            carregarTabela("");
            limparCampos();
        });
        btnPesquisar.addActionListener((ActionEvent e) -> carregarTabela(txtPesquisa.getText().trim()));
        btnExcluir.addActionListener((ActionEvent e) -> excluirProduto());

        tabela.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                selecionarLinhaTabela();
            }
        });

        carregarTabela("");
    }

    private JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return campo;
    }

    private JLabel criarRotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(COR_TEXTO);
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        return label;
    }

    private void cadastrarProduto() {
        String nome = txtNome.getText().trim();
        String precoStr = txtPreco.getText().trim().replace(",", ".");
        String qtdStr = txtQtd.getText().trim();

        if (nome.isEmpty() || nome.equals("Nome do Produto") ||
            precoStr.isEmpty() || precoStr.equals("Preço (R$)") ||
            qtdStr.isEmpty() || qtdStr.equals("Qtd Inicial")) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos para o novo produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double preco;
        int quantidade;
        try {
            preco = Double.parseDouble(precoStr);
            quantidade = Integer.parseInt(qtdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Insira valores válidos para Preço e Quantidade!", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (quantidade < 0) {
            JOptionPane.showMessageDialog(this, "A quantidade inicial não pode ser negativa!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sucesso = produtoDAO.cadastrar(nome, preco, quantidade);
        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            limparCampos();
            carregarTabela("");
        } else {
            JOptionPane.showMessageDialog(this, "Erro no Banco de Dados ao cadastrar produto.", "Erro MySQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarEstoque(boolean aumentar) {
        if (idProdutoSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String qtdStr = txtAjusteQtd.getText().trim();
        if (qtdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a quantidade a ser " + (aumentar ? "adicionada" : "removida") + "!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int valorAjuste;
        try {
            valorAjuste = Integer.parseInt(qtdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe apenas números inteiros para o ajuste!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (valorAjuste <= 0) {
            JOptionPane.showMessageDialog(this, "O valor de ajuste deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean sucesso = produtoDAO.ajustarEstoque(idProdutoSelecionado, valorAjuste, aumentar);
        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Estoque " + (aumentar ? "atualizado (+)" : "reduzido (-)") + " com sucesso!");
            txtAjusteQtd.setText("");
            carregarTabela("");
        } else {
            JOptionPane.showMessageDialog(this,
                    aumentar ? "Erro ao atualizar o estoque no banco." : "Não foi possível dar baixa. Quantidade em estoque é insuficiente!",
                    aumentar ? "Erro MySQL" : "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        if (idProdutoSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o produto selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            boolean sucesso = produtoDAO.excluir(idProdutoSelecionado);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                limparCampos();
                carregarTabela("");
            } else {
                JOptionPane.showMessageDialog(this, "Não é possível excluir produtos vinculados a vendas registradas!", "Erro MySQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void carregarTabela(String pesquisa) {
        modeloTabela.setRowCount(0);

        List<Produto> produtos = produtoDAO.pesquisar(pesquisa);
        for (Produto p : produtos) {
            String status;
            if (p.getQntdEstoque() == 0) {
                status = "SEM ESTOQUE";
            } else if (p.getQntdEstoque() <= 5) {
                status = "ESTOQUE BAIXO";
            } else {
                status = "DISPONÍVEL";
            }

            modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    String.format("%.2f", p.getValor()),
                    p.getQntdEstoque(),
                    status
            });
        }
    }

    private void selecionarLinhaTabela() {
        int linhaSelecionada = tabela.getSelectedRow();
        if (linhaSelecionada != -1) {
            idProdutoSelecionado = Integer.parseInt(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
            int qtdAtual = Integer.parseInt(modeloTabela.getValueAt(linhaSelecionada, 3).toString());

            if (qtdAtual == 0) {
                lblStatusEstoque.setText("ALERTA: PRODUTO ESGOTADO!");
                lblStatusEstoque.setForeground(COR_TEXTO);
            } else if (qtdAtual <= 5) {
                lblStatusEstoque.setText("ATENÇÃO: ESTOQUE BAIXO (" + qtdAtual + " un)");
                lblStatusEstoque.setForeground(COR_TEXTO);
            } else {
                lblStatusEstoque.setText("ESTOQUE NORMAL (" + qtdAtual + " un)");
                lblStatusEstoque.setForeground(COR_TEXTO);
            }
        }
    }

    private void limparCampos() {
        txtNome.setText("Nome do Produto");
        txtNome.setForeground(Color.GRAY);
        
        txtPreco.setText("Preço (R$)");
        txtPreco.setForeground(Color.GRAY);
        
        txtQtd.setText("Qtd Inicial");
        txtQtd.setForeground(Color.GRAY);

        txtAjusteQtd.setText("");
        txtPesquisa.setText("");
        idProdutoSelecionado = -1;
        lblStatusEstoque.setText("Status: Selecione um produto");
        lblStatusEstoque.setForeground(COR_TEXTO);
        tabela.clearSelection();
    }
}