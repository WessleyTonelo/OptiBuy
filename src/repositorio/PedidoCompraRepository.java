package repositorio;

import conexao.ConexaoDB;
import modelo.Cotacao;
import modelo.Fornecedor;
import modelo.PedidoCompra;
import modelo.Solicitante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoCompraRepository {

    // CREATE - Salva um novo pedido de compra
    public int salvar(PedidoCompra pedido) {
        String sql = "INSERT INTO pedido_compra (numero_pedido, id_cotacao_origem, id_fornecedor, id_comprador, data_emissao_pedido, data_entrega_prevista_pedido, data_da_entrega, valor_frete, valor_total_pedido, status, obs) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int idGerado = 0;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pedido.getNumeroPedido());
            stmt.setInt(2, pedido.getCotacaoOrigem().getId());
            stmt.setInt(3, pedido.getFornecedor().getId());
            stmt.setInt(4, pedido.getComprador().getId());
            stmt.setString(5, pedido.getDataEmissaoPedido());
            stmt.setString(6, pedido.getDataEntregaPrevistaPedido());
            stmt.setString(7, pedido.getDataDaEntrega());
            stmt.setDouble(8, pedido.getValorFrete());
            stmt.setDouble(9, pedido.getValorTotalPedido());
            stmt.setString(10, pedido.getStatus());
            stmt.setString(11, pedido.getObs());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                idGerado = rs.getInt(1);
            }

            System.out.println("Pedido de compra salvo com sucesso! ID: " + idGerado);

        } catch (SQLException e) {
            System.out.println("Erro ao salvar pedido de compra: " + e.getMessage());
        }

        return idGerado;
    }

    // READ - Lista todos os pedidos
    public List<PedidoCompra> listarTodos() {
        List<PedidoCompra> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido_compra";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(montarPedido(rs));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    // READ - Busca um pedido específico pelo ID
    public PedidoCompra buscarPorId(int id) {
        String sql = "SELECT * FROM pedido_compra WHERE id = ?";
        PedidoCompra pedido = null;

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pedido = montarPedido(rs);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar pedido: " + e.getMessage());
        }

        return pedido;
    }

    // UPDATE - Atualiza um pedido existente
    public void atualizar(PedidoCompra pedido, int id) {
        String sql = "UPDATE pedido_compra SET numero_pedido = ?, id_cotacao_origem = ?, id_fornecedor = ?, id_comprador = ?, data_emissao_pedido = ?, data_entrega_prevista_pedido = ?, data_da_entrega = ?, valor_frete = ?, valor_total_pedido = ?, status = ?, obs = ? WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getNumeroPedido());
            stmt.setInt(2, pedido.getCotacaoOrigem().getId());
            stmt.setInt(3, pedido.getFornecedor().getId());
            stmt.setInt(4, pedido.getComprador().getId());
            stmt.setString(5, pedido.getDataEmissaoPedido());
            stmt.setString(6, pedido.getDataEntregaPrevistaPedido());
            stmt.setString(7, pedido.getDataDaEntrega());
            stmt.setDouble(8, pedido.getValorFrete());
            stmt.setDouble(9, pedido.getValorTotalPedido());
            stmt.setString(10, pedido.getStatus());
            stmt.setString(11, pedido.getObs());
            stmt.setInt(12, id);

            stmt.executeUpdate();
            System.out.println("Pedido de compra atualizado com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar pedido: " + e.getMessage());
        }
    }

    // DELETE - Remove um pedido pelo ID
    public void deletar(int id) {
        String sql = "DELETE FROM pedido_compra WHERE id = ?";

        try (Connection con = ConexaoDB.conectar();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Pedido de compra removido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao deletar pedido: " + e.getMessage());
        }
    }

    // Método auxiliar - monta um PedidoCompra completo, buscando Cotacao, Fornecedor e Solicitante relacionados
    private PedidoCompra montarPedido(ResultSet rs) throws SQLException {
        CotacaoRepository cotacaoRepo = new CotacaoRepository();
        FornecedorRepository fornecedorRepo = new FornecedorRepository();
        SolicitanteRepository solicitanteRepo = new SolicitanteRepository();

        Cotacao cotacao = cotacaoRepo.buscarPorId(rs.getInt("id_cotacao_origem"));
        Fornecedor fornecedor = fornecedorRepo.buscarPorId(rs.getInt("id_fornecedor"));
        Solicitante comprador = solicitanteRepo.buscarPorId(rs.getInt("id_comprador"));

        return new PedidoCompra(
                comprador,
                cotacao,
                rs.getString("data_da_entrega"),
                rs.getString("data_emissao_pedido"),
                rs.getString("data_entrega_prevista_pedido"),
                rs.getInt("id"),
                fornecedor,
                rs.getInt("numero_pedido"),
                rs.getString("obs"),
                rs.getString("status"),
                rs.getDouble("valor_frete"),
                rs.getDouble("valor_total_pedido")
        );
    }
}