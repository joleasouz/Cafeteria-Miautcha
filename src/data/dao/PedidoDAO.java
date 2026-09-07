package data.dao;

import data.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.ItemPedido;
import model.Pedido;
import model.Produto;
import model.Cliente;
import model.StatusPedido;

public class PedidoDAO {

    public boolean salvar(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedidos (id_cliente, id_funcionario, data_pedido, status, valor_total) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_pedidos (id_produtos, id_pedidos, quantidade, valor_subtotal) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = Conexao.conectar();
            conn.setAutoCommit(false);

            int idPedidoGerado;
            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setInt(1, pedido.getCliente().getId());
                stmtPedido.setNull(2, Types.INTEGER);
                stmtPedido.setObject(3, pedido.getData());
                stmtPedido.setString(4, pedido.getStatus().name());
                stmtPedido.setDouble(5, pedido.getValorTotal());
                stmtPedido.executeUpdate();

                ResultSet keys = stmtPedido.getGeneratedKeys();
                keys.next();
                idPedidoGerado = keys.getInt(1);
            }

            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
                for (ItemPedido item : pedido.getItens()) {
                    stmtItem.setInt(1, item.getProduto().getId());
                    stmtItem.setInt(2, idPedidoGerado);
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.calcularSubtotal());
                    stmtItem.addBatch();
                }
                stmtItem.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("Erro ao salvar pedido: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) {}
        }
    }
    public List<Pedido> listarAtivos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.id, p.id_cliente, p.data_pedido, p.status, p.valor_total, p.numero_mesa, c.nome AS nome_cliente "
                + "FROM pedidos p "
                + "LEFT JOIN clientes c ON c.id = p.id_cliente "
                + "WHERE p.status NOT IN ('ENVIADO', 'CANCELADO') "
                + "ORDER BY p.data_pedido";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idPedido = rs.getInt("id");

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome_cliente"));

                Pedido pedido = new Pedido(
                        idPedido,
                        cliente,
                        rs.getTimestamp("data_pedido").toLocalDateTime(),
                        StatusPedido.valueOf(rs.getString("status")),
                        rs.getDouble("valor_total"),
                        rs.getInt("numero_mesa")
                );

                for (ItemPedido item : buscarItens(conn, idPedido)) {
                    pedido.adicionarItem(item);
                }

                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pedidos ativos: " + e.getMessage());
        }

        return pedidos;
    }

    private List<ItemPedido> buscarItens(Connection conn, int idPedido) throws SQLException {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT p.id AS id_produto, p.nome_produto, ip.quantidade, ip.valor_subtotal "
                + "FROM item_pedidos ip "
                + "JOIN produtos p ON p.id = ip.id_produtos "
                + "WHERE ip.id_pedidos = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPedido);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int quantidade = rs.getInt("quantidade");
                    double subtotal = rs.getDouble("valor_subtotal");
                    double precoUnitario = quantidade > 0 ? subtotal / quantidade : 0;

                    Produto produto = new Produto(
                            rs.getInt("id_produto"),
                            rs.getString("nome_produto"),
                            precoUnitario,
                            0
                    );

                    itens.add(new ItemPedido(0, produto, quantidade, precoUnitario));
                }
            }
        }

        return itens;
    }

    //atualiza o status
    public boolean atualizarStatus(int idPedido, StatusPedido novoStatus) {
        String sql = "UPDATE pedidos SET status = ? WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus.name());
            stmt.setInt(2, idPedido);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar status do pedido: " + e.getMessage());
            return false;
        }
    }

    //cancela o pedido, apenas  atualizando o status no banco  para cancelado
    public boolean cancelar(int idPedido) {
        return atualizarStatus(idPedido, StatusPedido.cancelado());
    }
}
