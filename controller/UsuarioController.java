package controller;

import dao.UsuarioDAO;
import model.Usuario;
import util.PasswordUtil;

import java.util.List;

/**
 * Controller para gerenciar a lógica de negócio de Usuários
 */
public class UsuarioController {

    private final UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Cadastra um novo usuário com validações
     */
    public boolean cadastrarUsuario(String nome, String login, String senha,
                                    Usuario.TipoUsuario tipoUsuario) {
        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login é obrigatório");
        }

        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
        }

        // Verificar se login já existe
        if (usuarioDAO.buscarPorLogin(login) != null) {
            throw new IllegalArgumentException("Login já cadastrado no sistema");
        }

        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setLogin(login);
        usuario.setSenha(PasswordUtil.encrypt(senha));
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setAtivo(true);

        return usuarioDAO.inserir(usuario);
    }

    /**
     * Atualiza um usuário existente
     */
    public boolean atualizarUsuario(Integer id, String nome, String login,
                                    String senha, Usuario.TipoUsuario tipoUsuario, boolean ativo) {
        // Validações
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login é obrigatório");
        }

        // Buscar usuário existente
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        // Verificar se login já existe em outro usuário
        Usuario usuarioComMesmoLogin = usuarioDAO.buscarPorLogin(login);
        if (usuarioComMesmoLogin != null && !usuarioComMesmoLogin.getId().equals(id)) {
            throw new IllegalArgumentException("Login já cadastrado para outro usuário");
        }

        // Atualizar dados
        usuario.setNome(nome);
        usuario.setLogin(login);

        // Só atualiza senha se foi informada
        if (senha != null && !senha.trim().isEmpty()) {
            if (senha.length() < 6) {
                throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres");
            }
            usuario.setSenha(PasswordUtil.encrypt(senha));
        }

        usuario.setTipoUsuario(tipoUsuario);
        usuario.setAtivo(ativo);

        return usuarioDAO.atualizar(usuario);
    }

    /**
     * Exclui um usuário
     */
    public boolean excluirUsuario(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório");
        }

        return usuarioDAO.excluir(id);
    }

    /**
     * Autentica um usuário
     */
    public Usuario autenticar(String login, String senha) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login é obrigatório");
        }

        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória");
        }

        String senhaHash = PasswordUtil.encrypt(senha);
        return usuarioDAO.autenticar(login, senhaHash);
    }

    /**
     * Lista todos os usuários
     */
    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    /**
     * Busca usuários por nome
     */
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioDAO.buscarPorNome(nome);
    }

    /**
     * Busca usuário por ID
     */
    public Usuario buscarPorId(Integer id) {
        return usuarioDAO.buscarPorId(id);
    }

    /**
     * Conta total de usuários ativos
     */
    public int contarUsuariosAtivos() {
        return usuarioDAO.contarUsuariosAtivos();
    }

    /**
     * Altera o status ativo/inativo de um usuário
     */
    public boolean alterarStatus(Integer id, boolean ativo) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        usuario.setAtivo(ativo);
        return usuarioDAO.atualizar(usuario);
    }
}