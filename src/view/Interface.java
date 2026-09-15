package view;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.JTableHeader;

public interface Interface {

    // Paleta de cores
    Color COR_FUNDO_PAINEL = new Color(243, 237, 233); // off-white
    Color COR_FUNDO_PAINEL_ESCURO = new Color(230, 223, 218); // off-white mais escuro
    Color COR_CABECALHO = new Color(111, 78, 55); // marrom médio
    Color COR_TITULO = new Color(111, 78, 55); // marrom médio
    Color COR_TEXTO = new Color(66, 48, 36); // marrom escuro
    Color COR_BOTAO_PRIMARIO = new Color(111, 78, 55); // ações principais
    Color COR_BOTAO_VERDE = new Color(63, 71, 62); // aumento
    Color COR_BOTAO_VERMELHO = new Color(140, 14, 0); // baixa
    Color COR_TEXTO_BOTAO = Color.WHITE;
    Color COR_SELECAO_TABELA = new Color(230, 215, 195);

    // Método para criar botões arredondados e com efeito de clique
    static JButton botaoArredondado(String texto, Color corFundo) {
        JButton botao = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D grafico = (Graphics2D) g.create();
                grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                grafico.setColor(getModel().isPressed() ? corFundo.darker() : corFundo);
                grafico.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                grafico.dispose();
                super.paintComponent(g);
            }
        };
        botao.setFont(new Font("SansSerif", Font.BOLD, 11));
        botao.setForeground(COR_TEXTO_BOTAO);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setOpaque(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        return botao;
    }

    // Painéis modernos
    static JPanel paineis(LayoutManager layout, Color cor) {
        JPanel painel = new JPanel(layout) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D grafico = (Graphics2D) g.create();
                grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                grafico.setColor(cor);
                grafico.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                grafico.dispose();
                super.paintComponent(g);
            }
        };
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return painel;
    }

    // Estilização do cabeçalho das tabelas
    static void estilizarCabecalhoTabela(JTable tabela) {
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(COR_CABECALHO);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 32));
    }

    // caixa de entrada de dados
    static JTextField campoDados(String dado) {
        JTextField campo = new JTextField(dado);

        campo.setForeground(Color.GRAY);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campo.setOpaque(false);

        Border linhaInferior = new MatteBorder(0, 0, 2, 0, new Color(100, 100, 100));
        Border margemInterna = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        campo.setBorder(BorderFactory.createCompoundBorder(linhaInferior, margemInterna));

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(dado)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().trim().isEmpty()) {
                    campo.setText(dado);
                    campo.setForeground(Color.GRAY);
                }
            }
        });

        return campo;
    }

    // auxiliar para definir tamanho do componente
    static <T extends JComponent> T comTamanho(T componente, int largura, int altura) {
        componente.setPreferredSize(new Dimension(largura, altura));
        return componente;
    }

    // montador do painel alinhado no centro
    static JPanel criarPainelFormulario(int altura, Component... componentes) {
        JPanel painel = Interface.paineis(new GridBagLayout(), COR_FUNDO_PAINEL_ESCURO);
        painel.setPreferredSize(new Dimension(0, altura));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 15);

        for (Component c : componentes) {
            painel.add(c, gbc);
        }

        return painel;
    }

    // metodo de criar botao p recarregar
    static JButton botaoRecarregar(Runnable acaoRecarregar) {
        JButton botao = botaoArredondado("↻", COR_BOTAO_PRIMARIO);
        botao.addActionListener(e -> acaoRecarregar.run());
        return botao;
    }

}