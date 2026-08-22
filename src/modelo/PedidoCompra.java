package modelo;

public class PedidoCompra {
    private int id;
    private int numeroPedido;
    private Cotacao cotacaoOrigem;
    private Fornecedor fornecedor;
    private Solicitante comprador;
    private String dataEmissaoPedido;
    private String dataEntregaPrevistaPedido;
    private String dataDaEntrega;
    private double valorFrete;
    private String obs;
    private String status;
    private double valorTotalPedido;

    public PedidoCompra(Solicitante comprador, Cotacao cotacaoOrigem, String dataDaEntrega, String dataEmissaoPedido, String dataEntregaPrevistaPedido, int id, Fornecedor fornecedor, int numeroPedido, String obs, String status, double valorFrete, double valorTotalPedido) {
        this.comprador = comprador;
        this.cotacaoOrigem = cotacaoOrigem;
        this.dataDaEntrega = dataDaEntrega;
        this.dataEmissaoPedido = dataEmissaoPedido;
        this.dataEntregaPrevistaPedido = dataEntregaPrevistaPedido;
        this.id = id;
        this.fornecedor = fornecedor;
        this.numeroPedido = numeroPedido;
        this.obs = obs;
        this.status = status;
        this.valorFrete = valorFrete;
        this.valorTotalPedido = valorTotalPedido;
    }

    public Solicitante getComprador() {
        return comprador;
    }

    public void setComprador(Solicitante comprador) {
        this.comprador = comprador;
    }

    public Cotacao getCotacaoOrigem() {
        return cotacaoOrigem;
    }

    public void setCotacaoOrigem(Cotacao cotacaoOrigem) {
        this.cotacaoOrigem = cotacaoOrigem;
    }

    public String getDataDaEntrega() {
        return dataDaEntrega;
    }

    public void setDataDaEntrega(String dataDaEntrega) {
        this.dataDaEntrega = dataDaEntrega;
    }

    public String getDataEntregaPrevistaPedido() {
        return dataEntregaPrevistaPedido;
    }

    public void setDataEntregaPrevistaPedido(String dataEntregaPrevistaPedido) {
        this.dataEntregaPrevistaPedido = dataEntregaPrevistaPedido;
    }

    public String getDataEmissaoPedido() {
        return dataEmissaoPedido;
    }

    public void setDataEmissaoPedido(String dataEmissaoPedido) {
        this.dataEmissaoPedido = dataEmissaoPedido;
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

    public int getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(int numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setValorTotalPedido(double valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }

    public double getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(double valorFrete) {
        this.valorFrete = valorFrete;
    }
}
