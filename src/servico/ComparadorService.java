package servico;

import modelo.Cotacao;
import modelo.OrcamentoFornecedor;
import modelo.Produto;

import java.util.HashMap;
import java.util.Map;

public class ComparadorService {

    // Para cada produto da cotação, encontra o orçamento (fornecedor) com o menor preço
    public Map<Produto, OrcamentoFornecedor> encontrarMelhoresPrecos(Cotacao cotacao) {
        Map<Produto, OrcamentoFornecedor> melhoresOrcamentos = new HashMap<>();

        for (OrcamentoFornecedor orcamento : cotacao.getOrcamentoDoFornecedor()) {
            Produto produto = orcamento.getProduto();

            // Se ainda não temos um orçamento registrado pra esse produto, registra o atual
            if (!melhoresOrcamentos.containsKey(produto)) {
                melhoresOrcamentos.put(produto, orcamento);
            } else {
                // Se já temos, compara: o atual é mais barato que o já registrado?
                OrcamentoFornecedor melhorAtual = melhoresOrcamentos.get(produto);
                if (orcamento.getPrecoOfertado() < melhorAtual.getPrecoOfertado()) {
                    melhoresOrcamentos.put(produto, orcamento); // substitui pelo mais barato
                }
            }
        }

        return melhoresOrcamentos;
    }
}