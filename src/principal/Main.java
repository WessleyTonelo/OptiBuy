package principal;

import modelo.Contato;
import modelo.Endereco;
import modelo.Fornecedor;
import repositorio.ContatoRepository;
import repositorio.EnderecoRepository;
import repositorio.FornecedorRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        EnderecoRepository enderecoRepo = new EnderecoRepository();
        ContatoRepository contatoRepo = new ContatoRepository();
        FornecedorRepository fornecedorRepo = new FornecedorRepository();

        // 1. Criar e salvar um Endereço
        Endereco endereco = new Endereco(0, "Centro", "89800-000", "Chapecó", "Sala 2", "SC", "Rua Brasil", 123, "Brasil");
        enderecoRepo.salvar(endereco);

        // 2. Criar e salvar um Contato
        Contato contato = new Contato(0, "Vendedor", "49999998888", "contato@fornecedor.com", "João Silva", "4933334444");
        contatoRepo.salvar(contato);

        // 3. Buscar os IDs reais que o banco gerou (o mais recente de cada)
        List<Endereco> enderecos = enderecoRepo.listarTodos();
        Endereco enderecoSalvo = enderecos.get(enderecos.size() - 1); // pega o último salvo

        List<Contato> contatos = contatoRepo.listarTodos();
        Contato contatoSalvo = contatos.get(contatos.size() - 1); // pega o último salvo

        // 4. Criar e salvar o Fornecedor usando os IDs reais
        Fornecedor fornecedor = new Fornecedor(true, "12345678000199", contatoSalvo, enderecoSalvo, 0, "Fornecedor Teste LTDA");
        fornecedorRepo.salvar(fornecedor);

        // 5. Listar todos os fornecedores pra conferir
        List<Fornecedor> fornecedores = fornecedorRepo.listarTodos();
        for (Fornecedor f : fornecedores) {
            System.out.println(f.getNome() + " - CNPJ: " + f.getCnpj() + " - Endereço: " + f.getEndereco().getCidade());
        }
    }
}