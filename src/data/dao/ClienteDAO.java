package data.dao;

import data.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
/*---Jolea
        mudei só de cliente pra clientes na linha 17 pra ficar igual no banco */
public class ClienteDAO {

    public boolean cadastrar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, cpf, email, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("erro ao cadastrar cliente: " + e.getMessage());
            return false;
        }
    }

    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, email, telefone FROM clientes";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setCpf(rs.getString("cpf"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("erro ao listar clientes: " + e.getMessage());
        }

        return clientes;
    }
}
