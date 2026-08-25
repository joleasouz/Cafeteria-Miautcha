import view.PainelEstoque;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Define o visual padrão do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Cafeteria Miautcha - Gestão de Estoque");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setSize(900, 600);
            janela.setLocationRelativeTo(null); // Centraliza na tela

            // Adiciona o painel de estoque estilizado
            janela.add(new PainelEstoque());

            janela.setVisible(true);
        });
    }
}