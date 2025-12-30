package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Singleton para gerenciar conexões com o banco de dados MySQL
 * Garante que apenas uma instância seja criada e reutilizada
 */
public class ConnectionFactory {

    // Configurações do banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_inventario?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456789"; // Alterar conforme sua configuração

    private static ConnectionFactory instance;
    private Connection connection;

    /**
     * Construtor privado para implementar Singleton
     */
    private ConnectionFactory() {
        try {
            // Registrar o driver JDBC do MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL não encontrado", e);
        }
    }

    /**
     * Retorna a instância única da ConnectionFactory (Singleton)
     */
    public static synchronized ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    /**
     * Obtém uma conexão com o banco de dados
     * Cria nova conexão se não existir ou se estiver fechada
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Fecha a conexão com o banco de dados
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

    /**
     * Testa a conexão com o banco de dados
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
}