package repositorio;

import conexao.ConexaoDB;
import modelo.ItemSolicitacao;
import modelo.Produto;
import modelo.Solicitante;
import modelo.SolicitacaoCompra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoCompraRepository {

    // CREATE - Salva a solicitação e todos os seus itens
    public int salvar(SolicitacaoCompra solicitacao) {
        String sqlSolicitacao = "INSERT INTO solicitacao_compra (id_solicitante, setor, prioridade, data_solicitacao, status) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO solicitacao_produto (id_solicitacao, id_produto, quantidade) VALUES (?, ?, ?)";
        int idGerado = 0;

        try (Connection con = ConexaoDB.conectar()) {

            // 1. Insere a solicitação principal e pega o ID gerado
            try (PreparedStatement stmt = con.prepareStatement(sqlSolicitacao, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, solicitacao.getSolicitante().getId());
                stmt.setString(2, solicitacao.getSetor());
                stmt.setString(3, solicitacao.getPrioridade());
                stmt.setString(4, solicitacao.getDataSolicitacao());
                stmt.setString(5, solicitacao.getStatus());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                }
            }

            // 2. Insere cada item da lista, usando o ID que acabamos de gerar
            try (PreparedStatement stmtItem = con.prepareStatement(sqlItem)) {
                for (ItemSolicitacao item : solicitacao.getItens()) {
                    stmtItem.setInt(1, idGerado);
                    stmtItem.setInt(2, item.getProduto().getId());
                    stmtItem.setDouble(3, item.getQuantidade());
                    stmtItem.executeUpdate();
                }
            }

            System.out.println("Solicitação salva com sucesso! ID: " + idGerado);

        } catch (SQLException e) {
            System.out.println("Erro ao salvar solicitação: " + e.getMessage());
        }

        return idGerado;
    }

    // READ - Lista todas as solicitações
    public List<SolicitacaoCompra> listarTodos() {
        List<SolicitacaoCompra> solicitacoes = new ArrayList<>();
        String sql = "SELECT * FROM solicitacao_compra";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                SolicitacaoCompra s = montarSolicitacao(rs, con);
                solicitacoes.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar solicitações: " + e.getMessage());
        }

        return solicitacoes;
    }

    // READ - Busca uma solicitação específica pelo ID
    public SolicitacaoCompra buscarPorId(int id) {
        String sql = "SELECT * FROM solicitacao_compra WHERE id = ?";
        SolicitacaoCompra solicitacao = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            // TODO: use if (rs.next()) para chamar montarSolicitacao(rs, con)
            if(rs.next()){
                solicitacao = montarSolicitacao(rs, con);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar solicitação: " + e.getMessage());
        }

        return solicitacao;
    }

    // DELETE - Remove uma solicitação e seus itens
    public void deletar(int id) {
        String sqlItens = "DELETE FROM solicitacao_produto WHERE id_solicitacao = ?";
        String sqlSolicitacao = "DELETE FROM solicitacao_compra WHERE id = ?";

        try (Connection con = ConexaoDB.conectar()) {

            // 1. Primeiro apaga os itens (tabela solicitacao_produto)
            try (PreparedStatement stmtItens = con.prepareStatement(sqlItens)) {
                stmtItens.setInt(1, id);
                stmtItens.executeUpdate();
            }

            // 2. Depois apaga a solicitação principal (tabela solicitacao_compra)
            try (PreparedStatement stmtSolicitacao = con.prepareStatement(sqlSolicitacao)) {
                stmtSolicitacao.setInt(1, id);
                stmtSolicitacao.executeUpdate();
            }

            System.out.println("Solicitação removida com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar solicitação: " + e.getMessage());
        }
    }

    private SolicitacaoCompra montarSolicitacao(ResultSet rs, Connection con) throws SQLException {
        SolicitanteRepository solicitanteRepo = new SolicitanteRepository();
        ProdutoRepository produtoRepo = new ProdutoRepository();

        int idSolicitacao = rs.getInt("id");

        // TODO 1: busca o Solicitante completo
        Solicitante solicitante = solicitanteRepo.buscarPorId(rs.getInt("id_solicitante"));

        // TODO 2: busca os itens dessa solicitação específica
        List<ItemSolicitacao> itens = new ArrayList<>();
        String sqlItens = "SELECT * FROM solicitacao_produto WHERE id_solicitacao = ?";

        try (PreparedStatement stmtItens = con.prepareStatement(sqlItens)) {
            stmtItens.setInt(1, idSolicitacao);
            ResultSet rsItens = stmtItens.executeQuery();

            while (rsItens.next()) {
                Produto produto = produtoRepo.buscarPorId(rsItens.getInt("id_produto"));
                ItemSolicitacao item = new ItemSolicitacao(produto, rsItens.getDouble("quantidade"));
                itens.add(item);
            }
        }

        // TODO 3: monta o objeto final
        return new SolicitacaoCompra(
                rs.getString("data_solicitacao"),
                idSolicitacao,
                rs.getString("prioridade"),
                itens,
                rs.getString("setor"),
                rs.getString("status"),
                solicitante
        );
    }
}