package repositorio;

import conexao.ConexaoDB;
import modelo.Endereco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoRepository {

    // CREATE - Salva um novo endereço no banco
    public void salvar(Endereco endereco) {
        String sql = "INSERT INTO endereco (logradouro, numero, complemento, bairro, cidade, estado, cep, pais) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, endereco.getLogradouro());
            stmt.setInt(2, endereco.getNumero());
            stmt.setString(3, endereco.getComplemento());
            stmt.setString(4, endereco.getBairro());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getEstado());
            stmt.setString(7, endereco.getCep());
            stmt.setString(8, endereco.getPais());

            stmt.executeUpdate();
            System.out.println("Endereço salvo com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar endereço: " + e.getMessage());
        }
    }

    // READ - Lista todos os endereços
    public List<Endereco> listarTodos() {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT * FROM endereco";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Endereco e = new Endereco(
                        rs.getInt("id"),
                        rs.getString("bairro"),
                        rs.getString("cep"),
                        rs.getString("cidade"),
                        rs.getString("complemento"),
                        rs.getString("estado"),
                        rs.getString("logradouro"),
                        rs.getInt("numero"),
                        rs.getString("pais")
                );
                enderecos.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar endereços: " + e.getMessage());
        }

        return enderecos;
    }

    // READ - Busca um endereço específico pelo ID
    public Endereco buscarPorId(int id) {
        String sql = "SELECT * FROM endereco WHERE id = ?";
        Endereco endereco = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                endereco = new Endereco(
                        rs.getInt("id"),
                        rs.getString("bairro"),
                        rs.getString("cep"),
                        rs.getString("cidade"),
                        rs.getString("complemento"),
                        rs.getString("estado"),
                        rs.getString("logradouro"),
                        rs.getInt("numero"),
                        rs.getString("pais")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar endereço: " + e.getMessage());
        }

        return endereco;
    }

    // UPDATE - Atualiza um endereço existente
    public void atualizar(Endereco endereco, int id) {
        String sql = "UPDATE endereco SET logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ?, cep = ?, pais = ? WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, endereco.getLogradouro());
            stmt.setInt(2, endereco.getNumero());
            stmt.setString(3, endereco.getComplemento());
            stmt.setString(4, endereco.getBairro());
            stmt.setString(5, endereco.getCidade());
            stmt.setString(6, endereco.getEstado());
            stmt.setString(7, endereco.getCep());
            stmt.setString(8, endereco.getPais());
            stmt.setInt(9, id);

            stmt.executeUpdate();
            System.out.println("Endereço atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar endereço: " + e.getMessage());
        }
    }

    // DELETE - Remove um endereço pelo ID
    public void deletar(int id) {
        String sql = "DELETE FROM endereco WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Endereço removido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar endereço: " + e.getMessage());
        }
    }
}