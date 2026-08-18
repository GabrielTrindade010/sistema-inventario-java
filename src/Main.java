import dao.ProdutoDAO;
import dao.UsuarioDAO;
import database.ConnectionProvider;
import database.MySqlConnectionProvider;
import service.ProdutoService;
import service.UsuarioService;
import test.*;

void main() {
    System.out.println("==========================================");
    System.out.println("   INICIANDO BATERIA DE TESTES DO SISTEMA ");
    System.out.println("==========================================");

    try {
        ConnectionProvider connectionProvider = new MySqlConnectionProvider();

        ProdutoDAO produtoDAO = new ProdutoDAO(connectionProvider);
        UsuarioDAO usuarioDAO = new UsuarioDAO(connectionProvider);

        ProdutoService produtoService = new ProdutoService(produtoDAO);
        UsuarioService usuarioService = new UsuarioService(usuarioDAO);

        DatabaseTest.executar(connectionProvider);
        UsuarioServiceTest.executar(usuarioService);
        ProdutoServiceTest.executar(produtoService);

    } catch (Exception e) {
        TestRunner.reportarFalha("Erro fatal na execução dos testes: " + e.getMessage());
    } finally {
        TestRunner.exibirResumo();
    }
}