package conexao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoDB {

    private static String url;
    private static String usuario;
    private static String senha;

    static {
        try (FileInputStream input = new FileInputStream("src/config.properties")) {
            Properties props = new Properties();
            props.load(input);
            url = props.getProperty("db.url");
            usuario = props.getProperty("db.usuario");
            senha = props.getProperty("db.senha");
        } catch (IOException e) {
            System.out.println("Erro ao carregar config.properties: " + e.getMessage());
        }
    }

    public static Connection conectar() {
        try {
            Connection conexao = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conectado ao banco optibuy com sucesso!");
            return conexao;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        conectar();
    }
}