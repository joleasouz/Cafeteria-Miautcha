package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public interface Interface {
    
    // paleta de cores
    Color COR_FUNDO_PAINEL = new Color(243, 237, 233); // off-white
    Color COR_FUNDO_PAINEL_ESCURO = new Color(230, 223, 218); // off-white mais escuro
    Color COR_CABECALHO = new Color(111, 78, 55);       // marrom médio
    Color COR_TITULO = new Color(111, 78, 55);       // marrom médio
    Color COR_TEXTO = new Color(66, 48, 36);            // marrom escuro
    Color COR_BOTAO_PRIMARIO = new Color(111, 78, 55);   // ações principais
    Color COR_BOTAO_VERDE = new Color(63, 71, 62);      // aumento
    Color COR_BOTAO_VERMELHO = new Color(140, 14, 0);   // baixa
    Color COR_TEXTO_BOTAO = Color.WHITE;
    Color COR_SELECAO_TABELA = new Color(230, 215, 195);

    // método para criar botões arredondados e com efeito de clique
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
        botao.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 11));
        botao.setForeground(COR_TEXTO_BOTAO);
        botao.setFocusPainted(false);
        botao.setContentAreaFilled(false);
        botao.setOpaque(false);
        botao.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        return botao;
    }

    // paineis modernos
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

    // estilizacao do cabecalho das tabelas
    static void estilizarCabecalhoTabela(JTable tabela) {
        JTableHeader header = tabela.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBackground(COR_CABECALHO);
        header.setForeground(Color.white);
        header.setPreferredSize(new Dimension(100, 32));
    }

}