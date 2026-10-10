package principal;

public class MenuPrincipal {

    public void iniciar() {
        int opcao = -1;

        while (opcao != 0) {
            exibirMenu();
            opcao = Entrada.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> new MenuCadastros().exibir();
                case 2 -> menuFluxoCompras();
                case 3 -> menuInteligenciaPrecos();
                case 0 -> System.out.println("\nEncerrando o OptiBuy. Até logo!");
                default -> System.out.println("\nOpção inválida. Tente novamente.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n==============================");
        System.out.println("          OPTIBUY");
        System.out.println("  Gestão de Compras Inteligente");
        System.out.println("==============================");
        System.out.println("1 - Cadastros");
        System.out.println("2 - Fluxo de Compras");
        System.out.println("3 - Inteligência de Preços");
        System.out.println("0 - Sair");
        System.out.println("------------------------------");
    }

    private void menuFluxoCompras() {
        System.out.println("\n[Fluxo de Compras] Em construção...");
    }

    private void menuInteligenciaPrecos() {
        System.out.println("\n[Inteligência de Preços] Em construção...");
    }
}