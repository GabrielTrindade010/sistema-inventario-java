package model;

import java.time.LocalDateTime;

/**
 * Classe que representa um usuário do sistema
 */
public class Usuario {

    private Integer id;
    private String nome;
    private String login;
    private String senha;
    private TipoUsuario tipoUsuario;
    private boolean ativo;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    /**
     * Enum para tipos de usuário
     */
    public enum TipoUsuario {
        ADMIN("ADMIN"),
        FUNCIONARIO("FUNCIONARIO");

        private final String valor;

        TipoUsuario(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static TipoUsuario fromString(String valor) {
            for (TipoUsuario tipo : TipoUsuario.values()) {
                if (tipo.valor.equalsIgnoreCase(valor)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("Tipo de usuário inválido: " + valor);
        }
    }

    // Construtores
    public Usuario() {
        this.ativo = true;
        this.tipoUsuario = TipoUsuario.FUNCIONARIO;
    }

    public Usuario(String nome, String login, String senha, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
        this.ativo = true;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    /**
     * Verifica se o usuário é administrador
     */
    public boolean isAdmin() {
        return tipoUsuario == TipoUsuario.ADMIN;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", tipoUsuario=" + tipoUsuario +
                ", ativo=" + ativo +
                '}';
    }
}