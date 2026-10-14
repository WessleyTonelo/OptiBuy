package principal;

import modelo.Contato;
import modelo.Solicitante;
import repositorio.ContatoRepository;
import repositorio.SolicitanteRepository;

import java.util.List;

public class MenuSolicitantes {

    private final SolicitanteRepository solicitanteRepo = new SolicitanteRepository();
    private final ContatoRepository contatoRepo = new ContatoRepository();

    public void exibir() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n------ SOLICITANTES ------");
            System.out.println("1 - Cadastrar solicitante");
            System.out.println("2 - Listar solicitantes");
            System.out.println("3 - Buscar solicitante por ID");
            System.out.println("0 - Voltar");
            System.out.println("--------------------------");
            opcao = Entrada.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> cadastrarSolicitante();
                case 2 -> listarSolicitantes();
                case 3 -> buscarSolicitante();
                case 0 -> { }
                default -> System.out.println("\nOpção inválida.");
            }
        }
    }

    private void cadastrarSolicitante() {
        System.out.println("\n--- Novo solicitante ---");

        // ===== 1. Coleta tudo antes de salvar =====
        String nome = Entrada.lerTexto("Nome: ");
        if (nome.isEmpty()) {
            System.out.println("O nome é obrigatório. Cadastro cancelado.");
            return;
        }

        String setor = Entrada.lerTexto("Setor: ");
        if (setor.isEmpty()) {
            System.out.println("O setor é obrigatório. Cadastro cancelado.");
            return;
        }

        String cargo = Entrada.lerTexto("Cargo: ");
        String email = Entrada.lerTexto("E-mail: ");
        String telefone = Entrada.lerTexto("Telefone: ");
        String celular = Entrada.lerTexto("Celular: ");

        // ===== 2. Salva: primeiro o contato, depois o solicitante =====
        // O contato usa o mesmo nome e cargo do solicitante, então não perguntamos duas vezes
        Contato contato = new Contato(0, cargo, celular, email, nome, telefone);
        int idContato = contatoRepo.salvar(contato);

        if (idContato == 0) {
            System.out.println("Erro ao salvar o contato. Cadastro cancelado.");
            return;
        }

        contato.setId(idContato);

        Solicitante solicitante = new Solicitante(cargo, contato, 0, nome, setor);
        solicitanteRepo.salvar(solicitante);
    }

    private void listarSolicitantes() {
        List<Solicitante> solicitantes = solicitanteRepo.listarTodos();

        if (solicitantes.isEmpty()) {
            System.out.println("\nNenhum solicitante cadastrado.");
            return;
        }

        System.out.println("\n--- Solicitantes cadastrados ---");
        for (Solicitante s : solicitantes) {
            exibirSolicitante(s);
        }
    }

    private void buscarSolicitante() {
        int id = Entrada.lerInteiro("\nID do solicitante: ");
        Solicitante solicitante = solicitanteRepo.buscarPorId(id);

        if (solicitante == null) {
            System.out.println("Solicitante não encontrado.");
            return;
        }

        exibirSolicitante(solicitante);
    }

    private void exibirSolicitante(Solicitante s) {
        String email = s.getContato() != null ? s.getContato().getEmail() : "-";
        String telefone = s.getContato() != null ? s.getContato().getTelefone() : "-";

        System.out.printf("[%d] %s | %s | Setor: %s | %s | Tel: %s%n",
                s.getId(), s.getNome(), s.getCargo(), s.getSetor(), email, telefone);
    }
}