package data.dao;

import data.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Produto;

public class ProdutoDAO {

    public List<Produto> listar() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome_produto, preco, quantidade_estoque FROM produtos ORDER BY nome_produto";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(new Produto(
                        rs.getInt("id"),
                        rs.getString("nome_produto"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade_estoque")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    public boolean atualizarEstoque(int idProduto, int novaQuantidade) {
        String sql = "UPDATE produtos SET quantidade_estoque = ? WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, idProduto);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar estoque: " + e.getMessage());
            return false;
        }
    }

    public List<Produto> pesquisar(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome_produto, preco, quantidade_estoque FROM produtos WHERE nome_produto LIKE ? ORDER BY nome_produto";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                produtos.add(new Produto(rs.getInt("id"), rs.getString("nome_produto"),
                        rs.getDouble("preco"), rs.getInt("quantidade_estoque")));
            }
        } catch (SQLException e) { System.err.println(e.getMessage()); }
        return produtos;
    }

    public boolean cadastrar(String nome, double preco, int quantidade) {
        String sql = "INSERT INTO produtos (nome_produto, preco, quantidade_estoque) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setDouble(2, preco);
            stmt.setInt(3, quantidade);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println(e.getMessage()); return false; }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println(e.getMessage()); return false; }
    }

    public boolean ajustarEstoque(int id, int valor, boolean aumentar) {
        String sql = aumentar
                ? "UPDATE produtos SET quantidade_estoque = quantidade_estoque + ? WHERE id = ?"
                : "UPDATE produtos SET quantidade_estoque = quantidade_estoque - ? WHERE id = ? AND quantidade_estoque >= ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, valor);
            stmt.setInt(2, id);
            if (!aumentar) stmt.setInt(3, valor);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { System.err.println(e.getMessage()); return false; }
    }
}