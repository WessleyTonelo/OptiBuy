package servico;

import modelo.Cotacao;
import modelo.Fornecedor;
import modelo.OrcamentoFornecedor;
import modelo.Produto;
import repositorio.CotacaoRepository;

import java.util.ArrayList;
import java.util.List;

public class HistoricoPrecoFornecedorService {

    // Busca o histórico de preços que um fornecedor específico já ofertou para um produto específico
    public List<Double> buscarHistoricoPrecos(Produto produto, Fornecedor fornecedor) {
        List<Double> historico = new ArrayList<>();

        CotacaoRepository cotacaoRepo = new CotacaoRepository();
        List<Cotacao> todasCotacoes = cotacaoRepo.listarTodos();

        for (Cotacao cotacao : todasCotacoes) {
            for (OrcamentoFornecedor orcamento : cotacao.getOrcamentoDoFornecedor()) {
                boolean mesmoProduto = orcamento.getProduto().getId() == produto.getId();
                boolean mesmoFornecedor = orcamento.getFornecedor().getId() == fornecedor.getId();

                if (mesmoProduto && mesmoFornecedor) {
                    historico.add(orcamento.getPrecoOfertado());
                }
            }
        }

        return historico;
    }

    // Compara um preço novo com o menor preço já visto no histórico daquele fornecedor/produto
    public String compararComHistorico(Produto produto, Fornecedor fornecedor, double precoAtual) {
        List<Double> historico = buscarHistoricoPrecos(produto, fornecedor);

        if (historico.isEmpty()) {
            return "Sem histórico anterior desse fornecedor para esse produto.";
        }

        double menorPrecoHistorico = historico.get(0);
        for (double preco : historico) {
            if (preco < menorPrecoHistorico) {
                menorPrecoHistorico = preco;
            }
        }

        if (precoAtual > menorPrecoHistorico) {
            double diferenca = precoAtual - menorPrecoHistorico;
            return "ATENÇÃO: preço atual (R$ " + String.format("%.2f", precoAtual) + ") está R$ " + String.format("%.2f", diferenca) + " acima do menor preço já pago por esse fornecedor (R$ " + String.format("%.2f", menorPrecoHistorico) + ").";
        } else if (precoAtual < menorPrecoHistorico) {
            return "Ótimo! Preço atual (R$ " + String.format("%.2f", precoAtual) + ") é o menor já visto para esse fornecedor.";
        } else {
            return "Preço igual ao menor já registrado (R$ " + String.format("%.2f", menorPrecoHistorico) + ").";
        }
    }
}