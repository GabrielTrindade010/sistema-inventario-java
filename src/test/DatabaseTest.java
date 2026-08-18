package test;

import database.ConnectionProvider;
import java.sql.Connection;

public class DatabaseTest {
    public static void executar(ConnectionProvider provider) {
        System.out.println("\n--- [TESTE] Conexão com o Banco ---");
        try (Connection conn = provider.getConnection()) {
            boolean conectou = (conn != null && !conn.isClosed());
            TestRunner.assertIsTrue(conectou, "Conexão estabelecida!", "Falha na conexão.");
        } catch (Exception e) {
            TestRunner.reportarFalha("Erro na conexão: " + e.getMessage());
        }
    }
}