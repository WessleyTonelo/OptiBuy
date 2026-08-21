package modelo;

public class OrcamentoFornecedor {
    private int id;
    private Fornecedor fornecedor;
    private Produto produto;
    private Double precoOfertado;
    private int prazoEntregaDias;
    private String obs;

    public OrcamentoFornecedor(Fornecedor fornecedor, int id, int prazoEntregaDias, String obs, Double precoOfertado, Produto produto) {
        this.fornecedor = fornecedor;
        this.id = id;
        this.prazoEntregaDias = prazoEntregaDias;
        this.obs = obs;
        this.precoOfertado = precoOfertado;
        this.produto = produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public int getPrazoEntregaDias() {
        return prazoEntregaDias;
    }

    public void setPrazoEntregaDias(int prazoEntregaDias) {
        this.prazoEntregaDias = prazoEntregaDias;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Double getPrecoOfertado() {
        return precoOfertado;
    }

    public void setPrecoOfertado(Double precoOfertado) {
        this.precoOfertado = precoOfertado;
    }
}
