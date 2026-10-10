package principal;

import modelo.Produto;
import repositorio.ProdutoRepository;

import java.util.List;

public class MenuCadastros {

    private final ProdutoRepository produtoRepo = new ProdutoRepository();

    public void exibir() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n------ CADASTROS ------");
            System.out.println("1 - Produtos");
            System.out.println("0 - Voltar");
            System.out.println("-----------------------");
            opcao = Entrada.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> menuProdutos();
                case 0 -> { }
                default -> System.out.println("\nOpção inválida.");
            }
        }
    }

    private void menuProdutos() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n------ PRODUTOS ------");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("3 - Buscar produto por ID");
            System.out.println("4 - Ativar/Inativar produto");
            System.out.println("0 - Voltar");
            System.out.println("----------------------");
            opcao = Entrada.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> cadastrarProduto();
                case 2 -> listarProdutos();
                case 3 -> buscarProduto();
                case 4 -> alternarAtivo();
                case 0 -> { }
                default -> System.out.println("\nOpção inválida.");
            }
        }
    }

    private void cadastrarProduto() {
        System.out.println("\n--- Novo produto ---");

        String nome = Entrada.lerTexto("Nome: ");
        if (nome.isEmpty()) {
            System.out.println("O nome é obrigatório. Cadastro cancelado.");
            return;
        }

        String descricao = Entrada.lerTexto("Descrição: ");
        String categoria = Entrada.lerTexto("Categoria: ");

        double preco = Entrada.lerDecimal("Preço de referência (R$): ");
        if (preco < 0) {
            System.out.println("Preço inválido. Cadastro cancelado.");
            return;
        }

        int estoque = Entrada.lerInteiro("Estoque inicial: ");
        if (estoque < 0) {
            System.out.println("Estoque inválido. Cadastro cancelado.");
            return;
        }

        Produto produto = new Produto(true, categoria, descricao, 0, nome, preco, estoque);
        produtoRepo.salvar(produto);
    }

    private void listarProdutos() {
        List<Produto> produtos = produtoRepo.listarTodos();

        if (produtos.isEmpty()) {
            System.out.println("\nNenhum produto cadastrado.");
            return;
        }

        System.out.println("\n--- Produtos cadastrados ---");
        for (Produto p : produtos) {
            exibirProduto(p);
        }
    }

    private void buscarProduto() {
        int id = Entrada.lerInteiro("\nID do produto: ");
        Produto produto = produtoRepo.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        exibirProduto(produto);
    }

    // Em vez de apagar, inativa: o produto já pode estar em pedidos e cotações antigas
    private void alternarAtivo() {
        int id = Entrada.lerInteiro("\nID do produto: ");
        Produto produto = produtoRepo.buscarPorId(id);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        produto.setAtivo(!produto.isAtivo());
        produtoRepo.atualizar(produto, id);
        System.out.println("Produto agora está: " + (produto.isAtivo() ? "ATIVO" : "INATIVO"));
    }

    private void exibirProduto(Produto p) {
        System.out.printf("[%d] %s | %s | R$ %.2f | Estoque: %d | %s%n",
                p.getId(), p.getNome(), p.getCategoria(), p.getPreco(),
                p.getQuantidade(), p.isAtivo() ? "Ativo" : "Inativo");
    }
}