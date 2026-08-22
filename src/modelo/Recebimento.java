package modelo;

import java.util.List;

public class Recebimento {
    private int id;
    private PedidoCompra pedidoCompra;
    private String responsavelRecebimento;
    private String dataRecebimento;
    private String status;
    private boolean divergenciaEncontrada;
    private List<ItemRecebimento> itensrecebidos;

    public Recebimento(String dataRecebimento, boolean divergenciaEncontrada, int id, PedidoCompra pedidoCompra, List<ItemRecebimento> itensrecebidos, String responsavelRecebimento, String status) {
        this.dataRecebimento = dataRecebimento;
        this.divergenciaEncontrada = divergenciaEncontrada;
        this.id = id;
        this.pedidoCompra = pedidoCompra;
        this.itensrecebidos = itensrecebidos;
        this.responsavelRecebimento = responsavelRecebimento;
        this.status = status;
    }

    public String getDataRecebimento() {
        return dataRecebimento;
    }

    public void setDataRecebimento(String dataRecebimento) {
        this.dataRecebimento = dataRecebimento;
    }

    public boolean isDivergenciaEncontrada() {
        return divergenciaEncontrada;
    }

    public void setDivergenciaEncontrada(boolean divergenciaEncontrada) {
        this.divergenciaEncontrada = divergenciaEncontrada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ItemRecebimento> getItensrecebidos() {
        return itensrecebidos;
    }

    public void setItensrecebidos(List<ItemRecebimento> itensrecebidos) {
        this.itensrecebidos = itensrecebidos;
    }

    public PedidoCompra getPedidoCompra() {
        return pedidoCompra;
    }

    public void setPedidoCompra(PedidoCompra pedidoCompra) {
        this.pedidoCompra = pedidoCompra;
    }

    public String getResponsavelRecebimento() {
        return responsavelRecebimento;
    }

    public void setResponsavelRecebimento(String responsavelRecebimento) {
        this.responsavelRecebimento = responsavelRecebimento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
