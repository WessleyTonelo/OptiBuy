package repositorio;

import conexao.ConexaoDB;
import modelo.Contato;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoRepository {

    // CREATE - Salva um novo contato no banco
    public void salvar(Contato contato) {
        String sql = "INSERT INTO contato (nome, cargo, email, telefone, celular) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getCargo());
            stmt.setString(3, contato.getEmail());
            stmt.setString(4, contato.getTelefone());
            stmt.setString(5, contato.getCelular());

            stmt.executeUpdate();
            System.out.println("Contato salvo com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar contato: " + e.getMessage());
        }
    }
    // READ - Lista todos os contatos
    public List<Contato> listarTodos() {
        List<Contato> contatos = new ArrayList<>();
        String sql = "SELECT * FROM contato";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Contato c = new Contato(
                        rs.getInt("id"),
                        rs.getString("cargo"),
                        rs.getString("celular"),
                        rs.getString("email"),
                        rs.getString("nome"),
                        rs.getString("telefone")
                );
                contatos.add(c);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar contatos: " + e.getMessage());
        }

        return contatos;
    }
    // READ - Busca um contato específico pelo ID
    public Contato buscarPorId(int id) {
        String sql = "SELECT * FROM contato WHERE id = ?";
        Contato contato = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Contato c = new Contato(
                        rs.getInt("id"),
                        rs.getString("cargo"),
                        rs.getString("celular"),
                        rs.getString("email"),
                        rs.getString("nome"),
                        rs.getString("telefone")
                );
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar contato: " + e.getMessage());
        }

        return contato;
    }

    // DELETE - Remove um contato pelo ID
    public void deletar(int id) {
        String sql = "DELETE FROM contato WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Contato removido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar contato: " + e.getMessage());
        }
    }
    // UPDATE - Atualiza um contato existente
    public void atualizar(Contato contato, int id) {
        String sql = "UPDATE contato SET nome = ?, cargo = ?, email = ?, telefone = ?, celular = ? WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getCargo());
            stmt.setString(3, contato.getEmail());
            stmt.setString(4, contato.getTelefone());
            stmt.setString(5, contato.getCelular());
            stmt.setInt(6, id);

            stmt.executeUpdate();
            System.out.println("Contato atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar contato: " + e.getMessage());
        }
    }
}