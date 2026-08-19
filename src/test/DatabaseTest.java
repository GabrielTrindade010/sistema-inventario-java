package test;

import database.ConnectionProvider;
import java.sql.Connection;

import database.MySqlConnectionProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    @Test
    @DisplayName("Deve conectar ao banco de dados MySQL com sucesso")
    void testConexaoBanco() {
        ConnectionProvider provider = new MySqlConnectionProvider();

        assertDoesNotThrow(() -> {
            try (Connection conn = provider.getConnection()) {
                assertNotNull(conn, "A conexão não deve ser nula.");
                assertFalse(conn.isClosed(), "A conexão deve estar aberta.");
            }
        });
    }
}