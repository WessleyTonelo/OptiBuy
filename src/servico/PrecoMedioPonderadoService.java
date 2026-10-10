package servico;

import modelo.ItemSolicitacao;
import modelo.OrcamentoFornecedor;
import modelo.PedidoCompra;
import modelo.Produto;
import repositorio.PedidoCompraRepository;

import java.util.List;

public class PrecoMedioPonderadoService {

    public Double calcularPrecoMedioPonderado(Produto produto) {
        PedidoCompraRepository pedidoRepo = new PedidoCompraRepository();
        List<PedidoCompra> todosPedidos = pedidoRepo.listarTodos();

        double somaValores = 0;
        double somaQuantidades = 0;

        for (PedidoCompra pedido : todosPedidos) {
            for (OrcamentoFornecedor orcamento : pedido.getCotacaoOrigem().getOrcamentoDoFornecedor()) {

                boolean mesmoProduto = orcamento.getProduto().getId() == produto.getId();
                boolean mesmoFornecedorDoPedido = orcamento.getFornecedor().getId() == pedido.getFornecedor().getId();

                if (mesmoProduto && mesmoFornecedorDoPedido) {
                    double quantidade = buscarQuantidade(pedido, produto);

                    if (quantidade > 0) {
                        somaValores += orcamento.getPrecoOfertado() * quantidade;
                        somaQuantidades += quantidade;
                    }
                }
            }
        }

        if (somaQuantidades == 0) {
            return null; // nunca foi comprado
        }

        return somaValores / somaQuantidades;
    }

    // Método auxiliar - busca a quantidade comprada daquele produto, na solicitação de origem do pedido
    private double buscarQuantidade(PedidoCompra pedido, Produto produto) {
        List<ItemSolicitacao> itens = pedido.getCotacaoOrigem().getSolicitacaoDeCompra().getItens();

        for (ItemSolicitacao item : itens) {
            if (item.getProduto().getId() == produto.getId()) {
                return item.getQuantidade();
            }
        }

        return 0;
    }

    public String consultarPrecoMedio(Produto produto) {
        Double media = calcularPrecoMedioPonderado(produto);

        if (media == null) {
            return "Nenhuma compra registrada para o produto " + produto.getNome() + ".";
        }

        return "Preço médio ponderado de " + produto.getNome() + ": R$ " + String.format("%.2f", media);
    }
}