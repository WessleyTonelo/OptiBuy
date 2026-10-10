package servico;

import modelo.ItemSolicitacao;
import modelo.OrcamentoFornecedor;
import modelo.PedidoCompra;
import modelo.Produto;
import repositorio.PedidoCompraRepository;

import java.time.LocalDate;
import java.util.List;

public class AlertaRecompraService {

    private EstoqueService estoqueService = new EstoqueService();

    // diasJanela: 1 = dia, 7 = semana, 30 = mês
    public String verificarRecompra(Produto produto, int diasJanela) {
        PedidoCompraRepository pedidoRepo = new PedidoCompraRepository();
        List<PedidoCompra> pedidos = pedidoRepo.listarTodos();

        LocalDate limite = LocalDate.now().minusDays(diasJanela);
        int pedidosRecentes = 0;
        double quantidadeRecente = 0;

        for (PedidoCompra pedido : pedidos) {
            if ("Cancelado".equalsIgnoreCase(pedido.getStatus())) {
                continue; // pedido cancelado não conta como compra
            }

            String data = pedido.getDataEmissaoPedido();
            if (data == null || data.isBlank()) {
                continue;
            }

            LocalDate dataPedido = LocalDate.parse(data); // formato "2026-10-09"
            if (dataPedido.isBefore(limite)) {
                continue; // pedido antigo, fora da janela
            }

            if (pedidoContemProduto(pedido, produto)) {
                pedidosRecentes++;
                quantidadeRecente += buscarQuantidade(pedido, produto);
            }
        }

        int estoque = estoqueService.consultarEstoque(produto);

        if (pedidosRecentes == 0) {
            return "Sem pedidos de " + produto.getNome() + " nos últimos " + diasJanela
                    + " dia(s). Estoque atual: " + estoque + ". Pode pedir.";
        }

        return "ALERTA DE RECOMPRA: " + produto.getNome() + " já foi pedido " + pedidosRecentes
                + " vez(es) nos últimos " + diasJanela + " dia(s), totalizando "
                + String.format("%.0f", quantidadeRecente) + " unidades. Estoque atual: " + estoque
                + ". Confirme se precisa mesmo comprar de novo.";
    }

    private boolean pedidoContemProduto(PedidoCompra pedido, Produto produto) {
        for (OrcamentoFornecedor orcamento : pedido.getCotacaoOrigem().getOrcamentoDoFornecedor()) {
            boolean mesmoProduto = orcamento.getProduto().getId() == produto.getId();
            boolean mesmoFornecedorDoPedido = orcamento.getFornecedor().getId() == pedido.getFornecedor().getId();
            if (mesmoProduto && mesmoFornecedorDoPedido) {
                return true;
            }
        }
        return false;
    }

    private double buscarQuantidade(PedidoCompra pedido, Produto produto) {
        List<ItemSolicitacao> itens = pedido.getCotacaoOrigem().getSolicitacaoDeCompra().getItens();
        for (ItemSolicitacao item : itens) {
            if (item.getProduto().getId() == produto.getId()) {
                return item.getQuantidade();
            }
        }
        return 0;
    }
}