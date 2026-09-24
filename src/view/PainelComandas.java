package view;

import data.dao.PedidoDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import model.ItemPedido;
import model.Pedido;
import model.StatusPedido;

public class PainelComandas extends JPanel implements Interface {

    private final PedidoDAO pedidoDAO = new PedidoDAO();
    private final JPanel painelCards;

    public PainelComandas() {
        setLayout(new BorderLayout(15, 15));
        setBackground(COR_FUNDO_PAINEL);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        add(criarCabecalho(), BorderLayout.NORTH);

        painelCards = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        painelCards.setBackground(COR_FUNDO_PAINEL);

        JScrollPane scroll = new JScrollPane(painelCards);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(COR_FUNDO_PAINEL);
        add(scroll, BorderLayout.CENTER);

        carregarComandas();
    }

    private JComponent criarCabecalho() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(COR_FUNDO_PAINEL);
        cabecalho.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COR_TITULO));

        JLabel titulo = new JLabel("Comanda Digital");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setForeground(COR_TITULO);
        titulo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        cabecalho.add(titulo, BorderLayout.WEST);

        // Botão de recarregar utilizando o método padronizado da Interface
        JButton btnRecarregar = Interface.botaoRecarregar(this::carregarComandas);

        JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        painelBotao.setOpaque(false);
        painelBotao.add(btnRecarregar);
        cabecalho.add(painelBotao, BorderLayout.EAST);

        return cabecalho;
    }

    // pega pedidos e monta as janelinhas dos cards
    public void carregarComandas() {
        painelCards.removeAll();

        List<Pedido> pedidosAtivos = pedidoDAO.listarAtivos();

        if (pedidosAtivos.isEmpty()) {
            JLabel lblVazio = new JLabel("Nenhuma comanda em aberto no momento.");
            lblVazio.setForeground(COR_TEXTO);
            lblVazio.setFont(new Font("SansSerif", Font.PLAIN, 13));
            painelCards.add(lblVazio);
        } else {
            for (Pedido pedido : pedidosAtivos) {
                painelCards.add(new CardPedido(pedido));
            }
        }

        painelCards.revalidate();
        painelCards.repaint();
    }

    // atualiza o status do pedido no banco e atualiza o card na tela
    private void trocarStatus(CardPedido card, Pedido pedido, StatusPedido novoStatus) {
        boolean sucesso = pedidoDAO.atualizarStatus(pedido.getId(), novoStatus);

        if (!sucesso) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao atualizar o status.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (novoStatus == StatusPedido.enviado()) {
            // se entregue, o card some
            removerCard(card);
        } else {
            card.atualizarStatusExibido(novoStatus);
        }
    }

    // chamado quando é confirmado o cancelamento
    private void cancelarPedido(CardPedido card, Pedido pedido) {
        boolean sucesso = pedidoDAO.cancelar(pedido.getId());

        if (sucesso) {
            removerCard(card);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erro ao cancelar o pedido.",
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerCard(CardPedido card) {
        painelCards.remove(card);
        painelCards.revalidate();
        painelCards.repaint();
    }

    // painelzinho individual da comanda
    private class CardPedido extends JPanel implements Interface {

        private final Pedido pedido;
        private final JLabel lblStatus;

        CardPedido(Pedido pedido) {
            this.pedido = pedido;

            setOpaque(false);
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(230, 210));

            JPanel fundo = Interface.paineis(new BorderLayout(8, 8), COR_FUNDO_PAINEL_ESCURO);
            add(fundo, BorderLayout.CENTER);

            JPanel painelTopo = new JPanel(new GridLayout(2, 1));
            painelTopo.setOpaque(false);

            JLabel lblMesa = new JLabel("Mesa " + pedido.getNumeroMesa());
            lblMesa.setFont(new Font("SansSerif", Font.BOLD, 16));
            lblMesa.setForeground(COR_TITULO);
            painelTopo.add(lblMesa);

            String nomeCliente = pedido.getCliente() != null ? pedido.getCliente().getNome() : null;
            JLabel lblCliente = new JLabel(nomeCliente != null && !nomeCliente.isEmpty() ? nomeCliente : "Cliente não identificado");
            lblCliente.setFont(new Font("SansSerif", Font.PLAIN, 11));
            lblCliente.setForeground(COR_TEXTO);
            painelTopo.add(lblCliente);

            fundo.add(painelTopo, BorderLayout.NORTH);

            JTextArea txtItens = new JTextArea(montarTextoItens(pedido));
            txtItens.setEditable(false);
            txtItens.setOpaque(false);
            txtItens.setLineWrap(true);
            txtItens.setWrapStyleWord(true);
            txtItens.setForeground(COR_TEXTO);
            txtItens.setFont(new Font("SansSerif", Font.PLAIN, 12));

            JScrollPane scrollItens = new JScrollPane(txtItens);
            scrollItens.setBorder(BorderFactory.createEmptyBorder());
            scrollItens.setOpaque(false);
            scrollItens.getViewport().setOpaque(false);
            fundo.add(scrollItens, BorderLayout.CENTER);

            lblStatus = new JLabel();
            lblStatus.setFont(new Font("SansSerif", Font.BOLD, 12));
            atualizarTextoStatus();

            JButton btnAtualizar = Interface.botaoArredondado("Atualizar", COR_BOTAO_PRIMARIO);
            JButton btnCancelar = Interface.botaoArredondado("Cancelar pedido", COR_VERMELHO);

            btnAtualizar.addActionListener(e -> abrirSelecaoStatus());
            btnCancelar.addActionListener(e -> confirmarCancelamento());

            JPanel painelBotoes = new JPanel(new GridLayout(1, 2, 6, 0));
            painelBotoes.setOpaque(false);
            painelBotoes.add(btnAtualizar);
            painelBotoes.add(btnCancelar);

            JPanel painelInferior = new JPanel(new BorderLayout(0, 6));
            painelInferior.setOpaque(false);
            painelInferior.add(lblStatus, BorderLayout.NORTH);
            painelInferior.add(painelBotoes, BorderLayout.SOUTH);
            fundo.add(painelInferior, BorderLayout.SOUTH);
        }

        private String montarTextoItens(Pedido pedido) {
            List<ItemPedido> itens = pedido.getItens();
            if (itens == null || itens.isEmpty()) {
                return "Nenhum item registrado";
            }

            StringBuilder texto = new StringBuilder();
            for (ItemPedido item : itens) {
                texto.append(item.getQuantidade())
                        .append("x ")
                        .append(item.getProduto().getNome())
                        .append("\n");
            }
            return texto.toString().trim();
        }

        private void abrirSelecaoStatus() {
            JComboBox<StatusPedido> combo = new JComboBox<>(StatusPedido.values());
            combo.removeItem(StatusPedido.cancelado());
            combo.setSelectedItem(pedido.getStatus());

            int opcao = JOptionPane.showConfirmDialog(
                    this,
                    combo,
                    "Alterar status - Mesa " + pedido.getNumeroMesa(),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (opcao == JOptionPane.OK_OPTION) {
                StatusPedido novoStatus = (StatusPedido) combo.getSelectedItem();
                if (novoStatus != null && novoStatus != pedido.getStatus()) {
                    trocarStatus(this, pedido, novoStatus);
                }
            }
        }

        private void confirmarCancelamento() {
            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente cancelar o pedido da mesa " + pedido.getNumeroMesa() + "?",
                    "Cancelar pedido",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirmacao == JOptionPane.YES_OPTION) {
                cancelarPedido(this, pedido);
            }
        }

        void atualizarStatusExibido(StatusPedido novoStatus) {
            pedido.setStatus(novoStatus);
            atualizarTextoStatus();
        }

        private void atualizarTextoStatus() {
            lblStatus.setText("Status: " + pedido.getStatus().exibicao());

            switch (pedido.getStatus()) {
                case PENDENTE:
                    lblStatus.setForeground(COR_VERMELHO);
                    break;
                case PRONTO:
                    lblStatus.setForeground(COR_VERDE_ESCURO);
                    break;
                case PREPARANDO:
                default:
                    lblStatus.setForeground(COR_TITULO);
            }
        }
    }
}