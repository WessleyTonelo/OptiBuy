package principal;

public class MenuCadastros {

    public void exibir() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n------ CADASTROS ------");
            System.out.println("1 - Produtos");
            System.out.println("2 - Fornecedores");
            System.out.println("3 - Solicitantes");
            System.out.println("0 - Voltar");
            System.out.println("-----------------------");
            opcao = Entrada.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> new MenuProdutos().exibir();
                case 2 -> new MenuFornecedores().exibir();
                case 3 -> new MenuSolicitantes().exibir();
                case 0 -> { }
                default -> System.out.println("\nOpção inválida.");
            }
        }
    }
}