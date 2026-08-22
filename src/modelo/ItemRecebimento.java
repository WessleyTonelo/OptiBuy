package modelo;

public class ItemRecebimento {
    private Produto produto;
    private int quantidadeEsperada;
    private int quantidadeEntregue;
    private boolean divergencia;
    private String obs;

    public ItemRecebimento(boolean divergencia, String obs, Produto produto, int quantidadeEntregue, int quantidadeEsperada) {
        this.divergencia = divergencia;
        this.obs = obs;
        this.produto = produto;
        this.quantidadeEntregue = quantidadeEntregue;
        this.quantidadeEsperada = quantidadeEsperada;
    }

    public boolean isDivergencia() {
        return divergencia;
    }

    public void setDivergencia(boolean divergencia) {
        this.divergencia = divergencia;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public int getQuantidadeEntregue() {
        return quantidadeEntregue;
    }

    public void setQuantidadeEntregue(int quantidadeEntregue) {
        this.quantidadeEntregue = quantidadeEntregue;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidadeEsperada() {
        return quantidadeEsperada;
    }

    public void setQuantidadeEsperada(int quantidadeEsperada) {
        this.quantidadeEsperada = quantidadeEsperada;
    }
}
