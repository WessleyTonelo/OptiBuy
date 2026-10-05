package repositorio;

import conexao.ConexaoDB;
import modelo.ItemRecebimento;
import modelo.PedidoCompra;
import modelo.Produto;
import modelo.Recebimento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecebimentoRepository {

    // CREATE - Salva o recebimento e todos os itens recebidos
    public int salvar(Recebimento recebimento) {
        String sqlRecebimento = "INSERT INTO recebimento (id_pedido_compra, responsavel_recebimento, data_recebimento, status, divergencia_encontrada) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_recebimento (id_recebimento, id_produto, quantidade_esperada, quantidade_entregue, divergencia, obs) VALUES (?, ?, ?, ?, ?, ?)";
        int idGerado = 0;

        try (Connection con = ConexaoDB.conectar()) {

            try (PreparedStatement stmt = con.prepareStatement(sqlRecebimento, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, recebimento.getPedidoCompra().getId());
                stmt.setString(2, recebimento.getResponsavelRecebimento());
                stmt.setString(3, recebimento.getDataRecebimento());
                stmt.setString(4, recebimento.getStatus());
                stmt.setBoolean(5, recebimento.isDivergenciaEncontrada());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                }
            }

            try (PreparedStatement stmtItem = con.prepareStatement(sqlItem)) {
                for (ItemRecebimento item : recebimento.getItensrecebidos()) {
                    stmtItem.setInt(1, idGerado);
                    stmtItem.setInt(2, item.getProduto().getId());
                    stmtItem.setInt(3, item.getQuantidadeEsperada());
                    stmtItem.setInt(4, item.getQuantidadeEntregue());
                    stmtItem.setBoolean(5, item.isDivergencia());
                    stmtItem.setString(6, item.getObs());
                    stmtItem.executeUpdate();
                }
            }

            System.out.println("Recebimento salvo com sucesso! ID: " + idGerado);

        } catch (SQLException e) {
            System.out.println("Erro ao salvar recebimento: " + e.getMessage());
        }

        return idGerado;
    }

    // READ - Lista todos os recebimentos
    public List<Recebimento> listarTodos() {
        List<Recebimento> recebimentos = new ArrayList<>();
        String sql = "SELECT * FROM recebimento";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                recebimentos.add(montarRecebimento(rs, con));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar recebimentos: " + e.getMessage());
        }

        return recebimentos;
    }

    // READ - Busca um recebimento específico pelo ID
    public Recebimento buscarPorId(int id) {
        String sql = "SELECT * FROM recebimento WHERE id = ?";
        Recebimento recebimento = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                recebimento = montarRecebimento(rs, con);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar recebimento: " + e.getMessage());
        }

        return recebimento;
    }

    // DELETE - Remove um recebimento e seus itens
    public void deletar(int id) {
        String sqlItens = "DELETE FROM item_recebimento WHERE id_recebimento = ?";
        String sqlRecebimento = "DELETE FROM recebimento WHERE id = ?";

        try (Connection con = ConexaoDB.conectar()) {

            try (PreparedStatement stmtItens = con.prepareStatement(sqlItens)) {
                stmtItens.setInt(1, id);
                stmtItens.executeUpdate();
            }

            try (PreparedStatement stmtRec = con.prepareStatement(sqlRecebimento)) {
                stmtRec.setInt(1, id);
                stmtRec.executeUpdate();
            }

            System.out.println("Recebimento removido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar recebimento: " + e.getMessage());
        }
    }

    // Método auxiliar - monta um Recebimento completo, incluindo PedidoCompra e lista de ItemRecebimento
    private Recebimento montarRecebimento(ResultSet rs, Connection con) throws SQLException {
        PedidoCompraRepository pedidoRepo = new PedidoCompraRepository();
        ProdutoRepository produtoRepo = new ProdutoRepository();

        int idRecebimento = rs.getInt("id");

        PedidoCompra pedido = pedidoRepo.buscarPorId(rs.getInt("id_pedido_compra"));

        List<ItemRecebimento> itens = new ArrayList<>();
        String sqlItens = "SELECT * FROM item_recebimento WHERE id_recebimento = ?";

        try (PreparedStatement stmtItens = con.prepareStatement(sqlItens)) {
            stmtItens.setInt(1, idRecebimento);
            ResultSet rsItens = stmtItens.executeQuery();

            while (rsItens.next()) {
                Produto produto = produtoRepo.buscarPorId(rsItens.getInt("id_produto"));

                ItemRecebimento item = new ItemRecebimento(
                        rsItens.getBoolean("divergencia"),
                        rsItens.getInt("id"),
                        rsItens.getString("obs"),
                        produto,
                        rsItens.getInt("quantidade_entregue"),
                        rsItens.getInt("quantidade_esperada")
                );
                itens.add(item);
            }
        }

        return new Recebimento(
                rs.getString("data_recebimento"),
                rs.getBoolean("divergencia_encontrada"),
                idRecebimento,
                pedido,
                itens,
                rs.getString("responsavel_recebimento"),
                rs.getString("status")
        );
    }
}