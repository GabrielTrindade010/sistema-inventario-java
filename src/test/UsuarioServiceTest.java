package test;

import dao.UsuarioDAO;
import database.ConnectionProvider;
import database.MySqlConnectionProvider;
import model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.UsuarioService;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        ConnectionProvider provider = new MySqlConnectionProvider();
        UsuarioDAO usuarioDAO = new UsuarioDAO(provider);
        usuarioService = new UsuarioService(usuarioDAO);
    }

    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso")
    void testCadastrarUsuarioSucesso() {
        String loginUnico = "user." + System.currentTimeMillis();
        Usuario usuario = new Usuario("Teste JUnit", loginUnico, "senha123", Usuario.TipoUsuario.ADMIN);

        boolean cadastrou = usuarioService.cadastrarUsuario(usuario);

        assertTrue(cadastrou, "O cadastro deve retornar true.");
        assertNotNull(usuario.getId(), "O ID do usuário deve ser gerado pelo banco.");
    }

    @Test
    @DisplayName("Deve bloquear o cadastro de login duplicado")
    void testCadastrarUsuarioLoginDuplicado() {
        // Assume que o login 'admin' já existe via script SQL
        Usuario duplicado = new Usuario("Outro Admin", "admin", "123456", Usuario.TipoUsuario.FUNCIONARIO);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.cadastrarUsuario(duplicado);
        });
    }
}