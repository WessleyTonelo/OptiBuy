package servico;

import modelo.OrcamentoFornecedor;
import modelo.PedidoCompra;
import modelo.Produto;
import repositorio.PedidoCompraRepository;

import java.util.List;

public class UltimoPrecoPagoService {

    // Busca o último preço pago por um produto, olhando os pedidos de compra já confirmados
    public Double buscarUltimoPrecoPago(Produto produto) {
        PedidoCompraRepository pedidoRepo = new PedidoCompraRepository();
        List<PedidoCompra> todosPedidos = pedidoRepo.listarTodos();

        PedidoCompra pedidoMaisRecente = null;
        Double precoEncontrado = null;

        for (PedidoCompra pedido : todosPedidos) {
            for (OrcamentoFornecedor orcamento : pedido.getCotacaoOrigem().getOrcamentoDoFornecedor()) {

                boolean mesmoProduto = orcamento.getProduto().getId() == produto.getId();
                boolean mesmoFornecedorDoPedido = orcamento.getFornecedor().getId() == pedido.getFornecedor().getId();

                if (mesmoProduto && mesmoFornecedorDoPedido) {
                    if (pedidoMaisRecente == null || pedido.getDataEmissaoPedido().compareTo(pedidoMaisRecente.getDataEmissaoPedido()) > 0) {
                        pedidoMaisRecente = pedido;
                        precoEncontrado = orcamento.getPrecoOfertado();
                    }
                }
            }
        }

        return precoEncontrado; // null se o produto nunca foi comprado antes
    }

    // Monta uma mensagem amigável com o resultado
    public String consultarUltimoPreco(Produto produto) {
        Double preco = buscarUltimoPrecoPago(produto);

        if (preco == null) {
            return "Nenhuma compra anterior registrada para o produto " + produto.getNome() + ".";
        }

        return "Último preço pago por " + produto.getNome() + ": R$ " + String.format("%.2f", preco);
    }
}