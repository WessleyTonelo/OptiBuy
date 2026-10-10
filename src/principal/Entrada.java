package principal;

import java.util.Scanner;

public class Entrada {

    private static final Scanner scanner = new Scanner(System.in);

    public static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    // Devolve -1 se o usuário digitar algo que não é número inteiro
    public static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Aceita vírgula ou ponto (4,50 ou 4.50). Devolve -1 se não for número
    public static double lerDecimal(String mensagem) {
        System.out.print(mensagem);
        try {
            return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}