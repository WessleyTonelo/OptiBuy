package modelo;

public class Fornecedor {
    private int id;
    private String nome;
    private String cnpj;
    private Endereco endereco;
    private boolean ativo;
    private Contato contato;

    public Fornecedor(boolean ativo, String cnpj, Contato contato, Endereco endereco, int id, String nome) {
        this.ativo = ativo;
        this.cnpj = cnpj;
        this.contato = contato;
        this.endereco = endereco;
        this.id = id;
        this.nome = nome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
