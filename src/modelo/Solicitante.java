package modelo;

public class Solicitante {
    private int id;
    private String nome;
    private String setor;
    private String cargo;
    private Contato contato;

    public Solicitante(String cargo, Contato contato, int id, String nome, String setor) {
        this.cargo = cargo;
        this.contato = contato;
        this.id = id;
        this.nome = nome;
        this.setor = setor;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
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

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
}
