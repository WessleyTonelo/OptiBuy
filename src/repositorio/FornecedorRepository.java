package repositorio;

import conexao.ConexaoDB;
import modelo.Contato;
import modelo.Endereco;
import modelo.Fornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorRepository {

    // CREATE - Salva um novo fornecedor no banco
    public void salvar(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor (nome, cnpj, ativo, id_endereco, id_contato) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setBoolean(3, fornecedor.isAtivo());
            stmt.setInt(4, fornecedor.getEndereco().getId());
            stmt.setInt(5, fornecedor.getContato().getId());

            stmt.executeUpdate();
            System.out.println("Fornecedor salvo com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar fornecedor: " + e.getMessage());
        }
    }

    // READ - Lista todos os fornecedores
    public List<Fornecedor> listarTodos() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fornecedor f = montarFornecedor(rs);
                fornecedores.add(f);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar fornecedores: " + e.getMessage());
        }

        return fornecedores;
    }

    // READ - Busca um fornecedor específico pelo ID
    public Fornecedor buscarPorId(int id) {
        String sql = "SELECT * FROM fornecedor WHERE id = ?";
        Fornecedor fornecedor = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                fornecedor = montarFornecedor(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar fornecedor: " + e.getMessage());
        }

        return fornecedor;
    }

    // UPDATE - Atualiza um fornecedor existente
    public void atualizar(Fornecedor fornecedor, int id) {
        String sql = "UPDATE fornecedor SET nome = ?, cnpj = ?, ativo = ?, id_endereco = ?, id_contato = ? WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setBoolean(3, fornecedor.isAtivo());
            stmt.setInt(4, fornecedor.getEndereco().getId());
            stmt.setInt(5, fornecedor.getContato().getId());
            stmt.setInt(6, id);

            stmt.executeUpdate();
            System.out.println("Fornecedor atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar fornecedor: " + e.getMessage());
        }
    }

    // DELETE - Remove um fornecedor pelo ID
    public void deletar(int id) {
        String sql = "DELETE FROM fornecedor WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Fornecedor removido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar fornecedor: " + e.getMessage());
        }
    }

    // Método auxiliar - monta um objeto Fornecedor a partir de uma linha do ResultSet
    private Fornecedor montarFornecedor(ResultSet rs) throws SQLException {
        EnderecoRepository enderecoRepo = new EnderecoRepository();
        ContatoRepository contatoRepo = new ContatoRepository();

        Endereco endereco = enderecoRepo.buscarPorId(rs.getInt("id_endereco"));
        Contato contato = contatoRepo.buscarPorId(rs.getInt("id_contato"));

        return new Fornecedor(
                rs.getBoolean("ativo"),
                rs.getString("cnpj"),
                contato,
                endereco,
                rs.getInt("id"),
                rs.getString("nome")
        );
    }
}