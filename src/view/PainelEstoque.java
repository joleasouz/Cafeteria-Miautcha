package view;

import data.Conexao;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class PainelEstoque extends JPanel implements Interface {

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JTextField txtNome, txtPreco, txtQuantidadeCad, txtPesquisa, txtAjusteQtd;
    private JButton btnCadastrar, btnExcluir, btnPesquisar, btnAtualizar, btnAdicionarEstoque, btnRemoverEstoque;
    private JLabel lblStatusEstoque;
    private int idProdutoSelecionado = -1;

    public PainelEstoque() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO_PAINEL);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // painel superiror - cadastro de produtos
        JPanel painelFormulario = Interface.paineis(new GridLayout(2, 4, 15, 10), COR_FUNDO_PAINEL_ESCURO);
        painelFormulario.setPreferredSize(new Dimension(0, 120));

        txtNome = criarCampoTexto();
        txtPreco = criarCampoTexto();
        txtQuantidadeCad = criarCampoTexto();
        btnCadastrar = Interface.botaoArredondado("Cadastrar Produto", COR_BOTAO_PRIMARIO);
        painelFormulario.add(criarRotulo("Nome do Produto:"));
        painelFormulario.add(criarRotulo("Preço (R$):"));
        painelFormulario.add(criarRotulo("Qtd Inicial:"));
        painelFormulario.add(new JLabel(""));

        painelFormulario.add(txtNome);
        painelFormulario.add(txtPreco);
        painelFormulario.add(txtQuantidadeCad);
        painelFormulario.add(btnCadastrar);

        add(painelFormulario, BorderLayout.NORTH);

        // painel central - tabela de produtos
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço (R$)", "Qtd Estoque", "Status"}, 0) {
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

        // estilo do cabeçalho
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(COR_CABECALHO);
        header.setForeground(Color.white);
        header.setPreferredSize(new Dimension(100, 32));

        // centralizar texto nas colunas
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

        // painel inferior - controle de estoque e ações
        JPanel painelInferior = new JPanel(new GridLayout(2, 1, 10, 10));
        painelInferior.setBackground(COR_FUNDO_PAINEL);

        // subpainel de entrada e saida de estoque
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

        // subpainel de busca e exclusão
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

        // --- eventos ---
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarProduto();
            }
        });

        btnAdicionarEstoque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarEstoque(true);
            }
        });

        btnRemoverEstoque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alterarEstoque(false);
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarTabela("");
                limparCampos();
            }
        });

        btnPesquisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarTabela(txtPesquisa.getText().trim());
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirProduto();
            }
        });

        tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selecionarLinhaTabela();
                }
            }
        });

        carregarTabela("");
    }

    // estilizacao
    private JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return campo;
    }

    private JLabel criarRotulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(COR_TEXTO);
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        return label;
    }

    // rn e bd
    private void cadastrarProduto() {
        String nome = txtNome.getText().trim();
        String precoStr = txtPreco.getText().trim().replace(",", ".");
        String qtdStr = txtQuantidadeCad.getText().trim();

        if (nome.isEmpty() || precoStr.isEmpty() || qtdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos para o novo produto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double preco = Double.parseDouble(precoStr);
            int quantidade = Integer.parseInt(qtdStr);

            if (quantidade < 0) {
                JOptionPane.showMessageDialog(this, "A quantidade inicial não pode ser negativa!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "INSERT INTO produto (nome, preco, quantidade) VALUES (?, ?, ?)";
            try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setDouble(2, preco);
                stmt.setInt(3, quantidade);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            }

            limparCampos();
            carregarTabela("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Insira valores válidos para Preço e Quantidade!", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro no Banco de Dados: " + e.getMessage(), "Erro MySQL", JOptionPane.ERROR_MESSAGE);
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

        try {
            int valorAjuste = Integer.parseInt(qtdStr);
            if (valorAjuste <= 0) {
                JOptionPane.showMessageDialog(this, "O valor de ajuste deve ser maior que zero!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String sql = aumentar
                    ? "UPDATE produto SET quantidade = quantidade + ? WHERE id = ?"
                    : "UPDATE produto SET quantidade = quantidade - ? WHERE id = ? AND quantidade >= ?";

            try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, valorAjuste);
                stmt.setInt(2, idProdutoSelecionado);
                if (!aumentar) {
                    stmt.setInt(3, valorAjuste);
                }

                int linhasAfetadas = stmt.executeUpdate();
                if (linhasAfetadas > 0) {
                    JOptionPane.showMessageDialog(this, "Estoque " + (aumentar ? "atualizado (+)" : "reduzido (-)") + " com sucesso!");
                    txtAjusteQtd.setText("");
                    carregarTabela("");
                } else {
                    JOptionPane.showMessageDialog(this, "Não foi possível dar baixa. Quantidade em estoque é insuficiente!", "Erro de Estoque", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Informe apenas números inteiros para o ajuste!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar banco: " + e.getMessage(), "Erro MySQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        if (idProdutoSelecionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o produto selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM produto WHERE id = ?";
            try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idProdutoSelecionado);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                limparCampos();
                carregarTabela("");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Não é possível excluir produtos vinculados a vendas registradas!", "Erro MySQL", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void carregarTabela(String pesquisa) {
        modeloTabela.setRowCount(0);
        String sql = "SELECT id, nome, preco, quantidade FROM produto";
        if (!pesquisa.isEmpty()) {
            sql += " WHERE nome LIKE ?";
        }
        sql += " ORDER BY id ASC";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!pesquisa.isEmpty()) {
                stmt.setString(1, "%" + pesquisa + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int qtd = rs.getInt("quantidade");

                String status;
                if (qtd == 0) {
                    status = "SEM ESTOQUE";
                } else if (qtd <= 5) {
                    status = "ESTOQUE BAIXO";
                } else {
                    status = "DISPONÍVEL";
                }

                modeloTabela.addRow(new Object[]{id, nome, String.format("%.2f", preco), qtd, status});
            }
        } catch (SQLException e) {
            // silencioso caso a conexão inicial ainda não esteja configurada
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
        txtNome.setText("");
        txtPreco.setText("");
        txtQuantidadeCad.setText("");
        txtAjusteQtd.setText("");
        txtPesquisa.setText("");
        idProdutoSelecionado = -1;
        lblStatusEstoque.setText("Status: Selecione um produto");
        lblStatusEstoque.setForeground(COR_TEXTO);
        tabela.clearSelection();
    }
}
