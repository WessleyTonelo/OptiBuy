package modelo;

import java.util.List;

public class SolicitacaoCompra {
    private int id;
    private Solicitante solicitante;
    private String setor;
    private String prioridade;
    private String dataSolicitacao;
    private String status;
    private List<Produto> produtos;

    public SolicitacaoCompra(String dataSolicitacao, int id, String prioridade, List<Produto> produtos, String setor, String status, Solicitante solicitante) {
        this.dataSolicitacao = dataSolicitacao;
        this.id = id;
        this.prioridade = prioridade;
        this.produtos = produtos;
        this.setor = setor;
        this.status = status;
        this.solicitante = solicitante;
    }

    public String getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(String dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Solicitante getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Solicitante solicitante) {
        this.solicitante = solicitante;
    }
}
