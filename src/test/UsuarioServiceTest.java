package test;

import model.Usuario;
import service.UsuarioService;

public class UsuarioServiceTest {
    public static void executar(UsuarioService usuarioService) {
        System.out.println("\n--- [TESTE] Usuário Service ---");

        try {
            Usuario user = new Usuario("Teste Silva", "user." + System.currentTimeMillis(), "senha123", Usuario.TipoUsuario.ADMIN);
            boolean cadastrou = usuarioService.cadastrarUsuario(user);

            TestRunner.assertIsTrue(
                    cadastrou && user.getId() != null,
                    "Usuário cadastrado com sucesso! ID: " + user.getId(),
                    "Falha ao cadastrar usuário no banco de dados."
            );
        } catch (Exception e) {
            TestRunner.reportarFalha("Falha ao cadastrar usuário: " + e.getMessage());
        }

        try {
            Usuario duplicado = new Usuario("Outro", "admin", "123456", Usuario.TipoUsuario.FUNCIONARIO);
            usuarioService.cadastrarUsuario(duplicado);
            TestRunner.reportarFalha("Permitiu cadastrar login duplicado.");
        } catch (IllegalArgumentException e) {
            TestRunner.assertIsTrue(true, "Bloqueio de login duplicado OK: " + e.getMessage(), "");
        } catch (Exception e) {
            TestRunner.reportarFalha("Erro inesperado no teste de duplicação: " + e.getMessage());
        }
    }
}