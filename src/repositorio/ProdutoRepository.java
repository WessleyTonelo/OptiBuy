package repositorio;

import conexao.ConexaoDB;
import modelo.Produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    // CREATE - Salva um novo produto no banco
    public void salvar(Produto produto) {
        String sql = "INSERT INTO produto (nome, descricao, categoria, quantidade, ativo, preco) VALUES (?,?,?,?,?,?) ";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, produto.getCategoria());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setBoolean(5, produto.isAtivo());
            stmt.setDouble(6, produto.getPreco());


            stmt.executeUpdate();
            System.out.println("Produto salvo com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
        }
    }

    // READ - Lista todos os produtos
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto(
                rs.getBoolean("ativo"),
                rs.getString("categoria"),
                rs.getString("descricao"),
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getInt("quantidade")
                );
                produtos.add(produto);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }

    // READ - Busca um produto específico pelo ID
    public Produto buscarPorId(int id) {
        String sql = "SELECT * FROM produto WHERE id = ?";
        Produto produto = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1 , id);
            ResultSet rs = stmt.executeQuery();

            // TODO: use if (rs.next()) para montar o objeto, igual fizemos no Contato
            if (rs.next()) {
                produto = new Produto(
                        rs.getBoolean("ativo"),
                        rs.getString("categoria"),
                        rs.getString("descricao"),
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto: " + e.getMessage());
        }

        return produto;
    }

    // UPDATE - Atualiza um produto existente
    public void atualizar(Produto produto, int id) {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, categoria = ?, quantidade = ?, ativo = ?, preco = ?   WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, produto.getCategoria());
            stmt.setInt(4, produto.getQuantidade());
            stmt.setBoolean(5, produto.isAtivo());
            stmt.setDouble(6, produto.getPreco());
            stmt.setInt(7, id);

            stmt.executeUpdate();
            System.out.println("Produto atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    // DELETE - Remove um produto pelo ID
    public void deletar(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
            System.out.println("Produto removido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar produto: " + e.getMessage());
        }
    }
}