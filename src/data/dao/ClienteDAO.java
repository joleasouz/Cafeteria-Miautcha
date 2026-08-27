package data.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import data.Conexao;

public class ClienteDAO{
    public List<Cliente> buscarClientes(String termo){
            List<Cliente> lista = new ArrayList<>();
             String sql = "SELECT * FROM cliente WHERE nome LIKE ? OR cpf LIKE ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + termo + "%");
            stmt.setString(2, "%" + termo + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("email"),
                    rs.getString("telefone")
                );
                lista.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}