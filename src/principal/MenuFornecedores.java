package principal;

import modelo.Contato;
import modelo.Endereco;
import modelo.Fornecedor;
import repositorio.ContatoRepository;
import repositorio.EnderecoRepository;
import repositorio.FornecedorRepository;

import java.util.List;

public class MenuFornecedores {

    private final FornecedorRepository fornecedorRepo = new FornecedorRepository();
    private final EnderecoRepository enderecoRepo = new EnderecoRepository();
    private final ContatoRepository contatoRepo = new ContatoRepository();

    public void exibir() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n------ FORNECEDORES ------");
            System.out.println("1 - Cadastrar fornecedor");
            System.out.println("2 - Listar fornecedores");
            System.out.println("3 - Buscar fornecedor por ID");
            System.out.println("4 - Ativar/Inativar fornecedor");
            System.out.println("0 - Voltar");
            System.out.println("--------------------------");
            opcao = Entrada.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> cadastrarFornecedor();
                case 2 -> listarFornecedores();
                case 3 -> buscarFornecedor();
                case 4 -> alternarAtivo();
                case 0 -> { }
                default -> System.out.println("\nOpção inválida.");
            }
        }
    }

    private void cadastrarFornecedor() {
        System.out.println("\n--- Novo fornecedor ---");

        // ===== 1. Coleta TUDO antes de salvar qualquer coisa =====
        String nome = Entrada.lerTexto("Razão social / nome: ");
        if (nome.isEmpty()) {
            System.out.println("O nome é obrigatório. Cadastro cancelado.");
            return;
        }

        // Guarda só os números (aceita 12.345.678/0001-99 ou 12345678000199)
        String cnpj = Entrada.lerTexto("CNPJ: ").replaceAll("[^0-9]", "");
        if (cnpj.length() != 14) {
            System.out.println("CNPJ inválido: precisa ter 14 números. Cadastro cancelado.");
            return;
        }
        if (fornecedorRepo.existeCnpj(cnpj)) {
            System.out.println("Já existe um fornecedor com esse CNPJ. Cadastro cancelado.");
            return;
        }

        System.out.println("\n-- Contato do fornecedor --");
        String nomeContato = Entrada.lerTexto("Nome do contato: ");
        if (nomeContato.isEmpty()) {
            System.out.println("O nome do contato é obrigatório. Cadastro cancelado.");
            return;
        }
        String cargo = Entrada.lerTexto("Cargo: ");
        String email = Entrada.lerTexto("E-mail: ");
        String telefone = Entrada.lerTexto("Telefone: ");
        String celular = Entrada.lerTexto("Celular: ");

        System.out.println("\n-- Endereço do fornecedor --");
        String logradouro = Entrada.lerTexto("Logradouro (rua/avenida): ");
        if (logradouro.isEmpty()) {
            System.out.println("O logradouro é obrigatório. Cadastro cancelado.");
            return;
        }
        int numero = Entrada.lerInteiro("Número: ");
        if (numero < 0) {
            System.out.println("Número inválido. Cadastro cancelado.");
            return;
        }
        String complemento = Entrada.lerTexto("Complemento: ");
        String bairro = Entrada.lerTexto("Bairro: ");
        String cidade = Entrada.lerTexto("Cidade: ");
        String estado = Entrada.lerTexto("Estado (UF): ").toUpperCase();
        String cep = Entrada.lerTexto("CEP: ");
        String pais = Entrada.lerTexto("País (Enter para Brasil): ");
        if (pais.isEmpty()) {
            pais = "Brasil";
        }

        // ===== 2. Agora sim salva, na ordem certa: endereço, contato, fornecedor =====
        Endereco endereco = new Endereco(0, bairro, cep, cidade, complemento, estado, logradouro, numero, pais);
        Contato contato = new Contato(0, cargo, celular, email, nomeContato, telefone);

        int idEndereco = enderecoRepo.salvar(endereco);
        int idContato = contatoRepo.salvar(contato);

        if (idEndereco == 0 || idContato == 0) {
            System.out.println("Erro ao salvar endereço ou contato. Cadastro cancelado.");
            return;
        }

        // O fornecedor guarda o ID dos dois, então preenchemos os IDs que o banco gerou
        endereco.setId(idEndereco);
        contato.setId(idContato);

        Fornecedor fornecedor = new Fornecedor(true, cnpj, contato, endereco, 0, nome);
        fornecedorRepo.salvar(fornecedor);
    }

    private void listarFornecedores() {
        List<Fornecedor> fornecedores = fornecedorRepo.listarTodos();

        if (fornecedores.isEmpty()) {
            System.out.println("\nNenhum fornecedor cadastrado.");
            return;
        }

        System.out.println("\n--- Fornecedores cadastrados ---");
        for (Fornecedor f : fornecedores) {
            exibirFornecedor(f);
        }
    }

    private void buscarFornecedor() {
        int id = Entrada.lerInteiro("\nID do fornecedor: ");
        Fornecedor fornecedor = fornecedorRepo.buscarPorId(id);

        if (fornecedor == null) {
            System.out.println("Fornecedor não encontrado.");
            return;
        }

        exibirFornecedor(fornecedor);
    }

    // Em vez de apagar, inativa: o fornecedor já pode estar em cotações e pedidos antigos
    private void alternarAtivo() {
        int id = Entrada.lerInteiro("\nID do fornecedor: ");
        Fornecedor fornecedor = fornecedorRepo.buscarPorId(id);

        if (fornecedor == null) {
            System.out.println("Fornecedor não encontrado.");
            return;
        }

        fornecedor.setAtivo(!fornecedor.isAtivo());
        fornecedorRepo.atualizar(fornecedor, id);
        System.out.println("Fornecedor agora está: " + (fornecedor.isAtivo() ? "ATIVO" : "INATIVO"));
    }

    private void exibirFornecedor(Fornecedor f) {
        String local = f.getEndereco() != null
                ? f.getEndereco().getCidade() + "/" + f.getEndereco().getEstado()
                : "-";
        String contato = f.getContato() != null
                ? f.getContato().getNome() + " (" + f.getContato().getEmail() + ")"
                : "-";

        System.out.printf("[%d] %s | CNPJ %s | %s | Contato: %s | %s%n",
                f.getId(), f.getNome(), formatarCnpj(f.getCnpj()), local, contato,
                f.isAtivo() ? "Ativo" : "Inativo");
    }

    // 12345678000199 -> 12.345.678/0001-99
    private String formatarCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) {
            return cnpj;
        }
        return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8)
                + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
    }
}