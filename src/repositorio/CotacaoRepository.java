package repositorio;

import conexao.ConexaoDB;
import modelo.Cotacao;
import modelo.Fornecedor;
import modelo.OrcamentoFornecedor;
import modelo.Produto;
import modelo.SolicitacaoCompra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CotacaoRepository {

    // CREATE - Salva a cotação e todos os orçamentos recebidos
    public int salvar(Cotacao cotacao) {
        String sqlCotacao = "INSERT INTO cotacao (id_solicitacao_compra, data_inicio, data_limite_resposta_fornecedor, status, obs) VALUES (?, ?, ?, ?, ?)";
        String sqlOrcamento = "INSERT INTO orcamento_fornecedor (id_cotacao, id_fornecedor, id_produto, preco_ofertado, prazo_entrega_dias, obs) VALUES (?, ?, ?, ?, ?, ?)";
        int idGerado = 0;

        try (Connection con = ConexaoDB.conectar()) {

            try (PreparedStatement stmt = con.prepareStatement(sqlCotacao, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, cotacao.getSolicitacaoDeCompra().getId());
                stmt.setString(2, cotacao.getDataInicio());
                stmt.setString(3, cotacao.getDataLimiteRespostaFornecedor());
                stmt.setString(4, cotacao.getStatus());
                stmt.setString(5, cotacao.getObs());

                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                }
            }

            try (PreparedStatement stmtOrc = con.prepareStatement(sqlOrcamento)) {
                for (OrcamentoFornecedor orc : cotacao.getOrcamentoDoFornecedor()) {
                    stmtOrc.setInt(1, idGerado);
                    stmtOrc.setInt(2, orc.getFornecedor().getId());
                    stmtOrc.setInt(3, orc.getProduto().getId());
                    stmtOrc.setDouble(4, orc.getPrecoOfertado());
                    stmtOrc.setInt(5, orc.getPrazoEntregaDias());
                    stmtOrc.setString(6, orc.getObs());
                    stmtOrc.executeUpdate();
                }
            }

            System.out.println("Cotação salva com sucesso! ID: " + idGerado);

        } catch (SQLException e) {
            System.out.println("Erro ao salvar cotação: " + e.getMessage());
        }

        return idGerado;
    }

    // READ - Lista todas as cotações
    public List<Cotacao> listarTodos() {
        List<Cotacao> cotacoes = new ArrayList<>();
        String sql = "SELECT * FROM cotacao";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cotacoes.add(montarCotacao(rs, con));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar cotações: " + e.getMessage());
        }

        return cotacoes;
    }

    // READ - Busca uma cotação específica pelo ID
    public Cotacao buscarPorId(int id) {
        String sql = "SELECT * FROM cotacao WHERE id = ?";
        Cotacao cotacao = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cotacao = montarCotacao(rs, con);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar cotação: " + e.getMessage());
        }

        return cotacao;
    }

    // DELETE - Remove uma cotação e seus orçamentos
    public void deletar(int id) {
        String sqlOrcamentos = "DELETE FROM orcamento_fornecedor WHERE id_cotacao = ?";
        String sqlCotacao = "DELETE FROM cotacao WHERE id = ?";

        try (Connection con = ConexaoDB.conectar()) {

            try (PreparedStatement stmtOrc = con.prepareStatement(sqlOrcamentos)) {
                stmtOrc.setInt(1, id);
                stmtOrc.executeUpdate();
            }

            try (PreparedStatement stmtCotacao = con.prepareStatement(sqlCotacao)) {
                stmtCotacao.setInt(1, id);
                stmtCotacao.executeUpdate();
            }

            System.out.println("Cotação removida com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar cotação: " + e.getMessage());
        }
    }

    // Método auxiliar - monta uma Cotacao completa, incluindo SolicitacaoCompra e lista de Orçamentos
    private Cotacao montarCotacao(ResultSet rs, Connection con) throws SQLException {
        SolicitacaoCompraRepository solicitacaoRepo = new SolicitacaoCompraRepository();
        FornecedorRepository fornecedorRepo = new FornecedorRepository();
        ProdutoRepository produtoRepo = new ProdutoRepository();

        int idCotacao = rs.getInt("id");

        SolicitacaoCompra solicitacao = solicitacaoRepo.buscarPorId(rs.getInt("id_solicitacao_compra"));

        List<OrcamentoFornecedor> orcamentos = new ArrayList<>();
        String sqlOrc = "SELECT * FROM orcamento_fornecedor WHERE id_cotacao = ?";

        try (PreparedStatement stmtOrc = con.prepareStatement(sqlOrc)) {
            stmtOrc.setInt(1, idCotacao);
            ResultSet rsOrc = stmtOrc.executeQuery();

            while (rsOrc.next()) {
                Fornecedor fornecedor = fornecedorRepo.buscarPorId(rsOrc.getInt("id_fornecedor"));
                Produto produto = produtoRepo.buscarPorId(rsOrc.getInt("id_produto"));

                OrcamentoFornecedor orc = new OrcamentoFornecedor(
                        fornecedor,
                        rsOrc.getInt("id"),
                        rsOrc.getInt("prazo_entrega_dias"),
                        rsOrc.getString("obs"),
                        rsOrc.getDouble("preco_ofertado"),
                        produto
                );
                orcamentos.add(orc);
            }
        }

        return new Cotacao(
                rs.getString("data_inicio"),
                rs.getString("data_limite_resposta_fornecedor"),
                idCotacao,
                rs.getString("obs"),
                solicitacao,
                orcamentos,
                rs.getString("status")
        );
    }
}