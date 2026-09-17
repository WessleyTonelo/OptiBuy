package repositorio;

import conexao.ConexaoDB;
import modelo.Contato;
import modelo.Solicitante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitanteRepository {

    // CREATE - Salva um novo solicitante no banco
    public void salvar(Solicitante solicitante) {
        String sql = "INSERT INTO solicitante (nome, setor, cargo, id_contato) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, solicitante.getNome());
            stmt.setString(2, solicitante.getSetor());
            stmt.setString(3, solicitante.getSetor());
            stmt.setInt(4, solicitante.getContato().getId());

            stmt.executeUpdate();
            System.out.println("Solicitante salvo com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao salvar solicitante: " + e.getMessage());
        }
    }

    // READ - Lista todos os solicitantes
    public List<Solicitante> listarTodos() {
        List<Solicitante> solicitantes = new ArrayList<>();
        String sql = "SELECT * FROM solicitante";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Solicitante s = montarSolicitante(rs);
                solicitantes.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar solicitantes: " + e.getMessage());
        }

        return solicitantes;
    }

    // READ - Busca um solicitante específico pelo ID
    public Solicitante buscarPorId(int id) {
        String sql = "SELECT * FROM solicitante WHERE id = ?";
        Solicitante solicitante = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                solicitante = montarSolicitante(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar solicitante: " + e.getMessage());
        }

        return solicitante;
    }

    // UPDATE - Atualiza um solicitante existente
    public void atualizar(Solicitante solicitante, int id) {
        String sql = "UPDATE solicitante SET nome = ?, setor = ?, cargo = ?, id_contato = ? WHERE id = ? ";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // TODO: preencha todos os "?", sem esquecer o id do WHERE
            stmt.setString(1, solicitante.getNome());
            stmt.setString(2, solicitante.getSetor());
            stmt.setString(3, solicitante.getCargo());
            stmt.setInt(4, solicitante.getContato().getId());
            stmt.setInt(5, id);

            stmt.executeUpdate();
            System.out.println("Solicitante atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar solicitante: " + e.getMessage());
        }
    }

    // DELETE - Remove um solicitante pelo ID
    public void deletar(int id) {
        String sql = "DELETE FROM  solicitante WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Solicitante removido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar solicitante: " + e.getMessage());
        }
    }

    private Solicitante montarSolicitante(ResultSet rs) throws SQLException {
        ContatoRepository contatoRepo = new ContatoRepository();

        Contato contato = contatoRepo.buscarPorId(rs.getInt("id_contato"));

        return new Solicitante(
                rs.getString("cargo"),
                contato,
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("setor")
        );
    }
}