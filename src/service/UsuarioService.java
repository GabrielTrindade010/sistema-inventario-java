package service;

import dao.UsuarioDAO;
import model.Usuario;
import util.PasswordUtil;

import java.util.List;

/**
 * Camada de Serviço responsável pelas regras de negócio e autenticação de Usuários.
 */
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    // Injeção de Dependência do DAO pelo construtor
    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Cadastra um novo usuário e realiza a criptografia da senha
     */
    public boolean cadastrarUsuario(Usuario usuario) {
        validarCamposObrigatorios(usuario);

        if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
            throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres.");
        }

        // Regra de Negócio: Não pode haver logins duplicados
        if (usuarioDAO.buscarPorLogin(usuario.getLogin()) != null) {
            throw new IllegalArgumentException("Login já cadastrado no sistema.");
        }

        // Criptografa a senha antes de enviar para o DAO
        usuario.setSenha(PasswordUtil.encrypt(usuario.getSenha()));
        usuario.setAtivo(true);

        return usuarioDAO.inserir(usuario);
    }

    /**
     * Atualiza dados de um usuário existente
     */
    public boolean atualizarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getId() == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório.");
        }

        validarCamposObrigatorios(usuario);

        Usuario usuarioExistente = usuarioDAO.buscarPorId(usuario.getId());
        if (usuarioExistente == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        // Regra de Negócio: Valida se o login pertence a outro usuário
        Usuario usuarioComMesmoLogin = usuarioDAO.buscarPorLogin(usuario.getLogin());
        if (usuarioComMesmoLogin != null && !usuarioComMesmoLogin.getId().equals(usuario.getId())) {
            throw new IllegalArgumentException("Login já cadastrado para outro usuário.");
        }

        // Criptografa nova senha apenas se foi informada
        if (usuario.getSenha() != null && !usuario.getSenha().trim().isEmpty()) {
            if (usuario.getSenha().length() < 6) {
                throw new IllegalArgumentException("Senha deve ter no mínimo 6 caracteres.");
            }
            usuario.setSenha(PasswordUtil.encrypt(usuario.getSenha()));
        } else {
            // Mantém a senha antiga se não for alterada
            usuario.setSenha(usuarioExistente.getSenha());
        }

        return usuarioDAO.atualizar(usuario);
    }

    private void validarCamposObrigatorios(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Dados do usuário não fornecidos.");
        }
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("Login é obrigatório.");
        }
    }

    public Usuario autenticar(String login, String senha) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }

        String senhaHash = PasswordUtil.encrypt(senha);
        return usuarioDAO.autenticar(login, senhaHash);
    }

    public boolean excluirUsuario(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do usuário é obrigatório.");
        }
        return usuarioDAO.excluir(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public List<Usuario> buscarPorNome(String nome) {
        return usuarioDAO.buscarPorNome(nome);
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioDAO.buscarPorId(id);
    }

    public int contarUsuariosAtivos() {
        return usuarioDAO.contarUsuariosAtivos();
    }

    public boolean alterarStatus(Integer id, boolean ativo) {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        usuario.setAtivo(ativo);
        return usuarioDAO.atualizar(usuario);
    }
}