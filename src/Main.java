import dao.ProdutoDAO;
import dao.UsuarioDAO;
import database.ConnectionProvider;
import database.MySqlConnectionProvider;
import service.ProdutoService;
import service.UsuarioService;

void main() {
    System.out.println("==========================================");
    System.out.println("   SISTEMA DE GERENCIAMENTO DE INVENTÁRIO ");
    System.out.println("==========================================");

    try {
        // 1. Inicialização da infraestrutura do banco
        ConnectionProvider connectionProvider = new MySqlConnectionProvider();

        // 2. Instanciação da camada de persistência (DAOs)
        ProdutoDAO produtoDAO = new ProdutoDAO(connectionProvider);
        UsuarioDAO usuarioDAO = new UsuarioDAO(connectionProvider);

        // 3. Instanciação da camada de serviços (Regras de Negócio)
        ProdutoService produtoService = new ProdutoService(produtoDAO);
        UsuarioService usuarioService = new UsuarioService(usuarioDAO);

        // 4. Verificação de saúde da aplicação
        System.out.println("✅ Serviços e conexões inicializados com sucesso!");
        System.out.println("📊 Total de produtos cadastrados: " + produtoService.contarProdutos());
        System.out.println("👤 Total de usuários ativos: " + usuarioService.contarUsuariosAtivos());
        System.out.println("==========================================");
        System.out.println("🚀 Backend pronto para receber a camada Web!");
        System.out.println("==========================================");

    } catch (Exception e) {
        System.err.println("❌ Erro ao inicializar o sistema: " + e.getMessage());
        e.printStackTrace();
    }
}