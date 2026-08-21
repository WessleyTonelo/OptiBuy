package modelo;

import java.util.List;

public class Cotacao {
    private int id;
    private SolicitacaoCompra solicitacaoDeCompra;
    private String dataInicio;
    private String dataLimiteRespostaFornecedor;
    private String Status;
    private String obs;
    private List<OrcamentoFornecedor> OrcamentoDoFornecedor;

    public Cotacao(String dataInicio, String dataLimiteRespostaFornecedor, int id, String obs, SolicitacaoCompra solicitacaoDeCompra, List<OrcamentoFornecedor> orcamentoDoFornecedor, String status) {
        this.dataInicio = dataInicio;
        this.dataLimiteRespostaFornecedor = dataLimiteRespostaFornecedor;
        this.id = id;
        this.obs = obs;
        this.solicitacaoDeCompra = solicitacaoDeCompra;
        OrcamentoDoFornecedor = orcamentoDoFornecedor;
        Status = status;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataLimiteRespostaFornecedor() {
        return dataLimiteRespostaFornecedor;
    }

    public void setDataLimiteRespostaFornecedor(String dataLimiteRespostaFornecedor) {
        this.dataLimiteRespostaFornecedor = dataLimiteRespostaFornecedor;
    }

    public List<OrcamentoFornecedor> getOrcamentoDoFornecedor() {
        return OrcamentoDoFornecedor;
    }

    public void setOrcamentoDoFornecedor(List<OrcamentoFornecedor> orcamentoDoFornecedor) {
        OrcamentoDoFornecedor = orcamentoDoFornecedor;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SolicitacaoCompra getSolicitacaoDeCompra() {
        return solicitacaoDeCompra;
    }

    public void setSolicitacaoDeCompra(SolicitacaoCompra solicitacaoDeCompra) {
        this.solicitacaoDeCompra = solicitacaoDeCompra;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
