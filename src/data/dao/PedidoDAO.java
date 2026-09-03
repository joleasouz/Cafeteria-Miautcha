package data.dao;

import data.Conexao;
import java.sql.*;
import model.ItemPedido;
import model.Pedido;

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
}