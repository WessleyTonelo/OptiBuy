package modelo;

public class Contato {
    private String nome;
    private String cargo;
    private String email;
    private String telefone;
    private String celular;

    public Contato(String cargo, String celular, String email, String nome, String telefone) {
        this.cargo = cargo;
        this.celular = celular;
        this.email = email;
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
