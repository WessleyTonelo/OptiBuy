package servico;

import modelo.Produto;

public class DescontoVolumeService {

    private PrecoMedioPonderadoService precoMedioService = new PrecoMedioPonderadoService();

    // descontoEsperadoPercentual: ex. 15 significa "espero pelo menos 15% de desconto"
    public String analisarCompra(Produto produto, double quantidade, double precoCotado, double descontoEsperadoPercentual) {
        Double precoReferencia = precoMedioService.calcularPrecoMedioPonderado(produto);

        if (precoReferencia == null) {
            return "Sem histórico de compras de " + produto.getNome() + ": não há preço de referência para comparar.";
        }

        double precoAlvo = precoReferencia * (1 - descontoEsperadoPercentual / 100);
        double economiaTotal = (precoReferencia - precoCotado) * quantidade;

        String cabecalho = "Produto: " + produto.getNome()
                + " | Quantidade: " + quantidade
                + " | Referência (média ponderada): R$ " + String.format("%.2f", precoReferencia)
                + " | Preço alvo com " + descontoEsperadoPercentual + "% de desconto: R$ " + String.format("%.2f", precoAlvo)
                + " | Preço cotado: R$ " + String.format("%.2f", precoCotado) + "\n";

        if (precoCotado <= precoAlvo) {
            return cabecalho + "VALE A PENA: o preço atingiu o desconto esperado. Economia total estimada: R$ "
                    + String.format("%.2f", economiaTotal);
        } else if (precoCotado <= precoReferencia) {
            return cabecalho + "NEGOCIAR MAIS: está abaixo do que você já pagou, mas não chegou no desconto esperado. Economia total estimada: R$ "
                    + String.format("%.2f", economiaTotal);
        } else {
            return cabecalho + "NÃO VALE: o preço está acima do que você já pagou em média. Custo extra estimado: R$ "
                    + String.format("%.2f", Math.abs(economiaTotal));
        }
    }
}