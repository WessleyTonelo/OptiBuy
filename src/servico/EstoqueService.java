package servico;

import modelo.ItemRecebimento;
import modelo.Produto;
import modelo.Recebimento;
import repositorio.ProdutoRepository;
import repositorio.RecebimentoRepository;

public class EstoqueService {

    private ProdutoRepository produtoRepo = new ProdutoRepository();
    private RecebimentoRepository recebimentoRepo = new RecebimentoRepository();

    // Salva o recebimento e coloca no estoque o que REALMENTE chegou
    public int registrarRecebimento(Recebimento recebimento) {
        int idRecebimento = recebimentoRepo.salvar(recebimento);

        if (idRecebimento > 0) {
            for (ItemRecebimento item : recebimento.getItensrecebidos()) {
                produtoRepo.adicionarEstoque(item.getProduto().getId(), item.getQuantidadeEntregue());
            }
        }

        return idRecebimento;
    }

    public int consultarEstoque(Produto produto) {
        Produto atual = produtoRepo.buscarPorId(produto.getId());
        return atual == null ? 0 : atual.getQuantidade();
    }
}