package dao;

import model.Produto;
import util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de banco de dados relacionadas a Produtos
 */
public class ProdutoDAO {

    /**
     * Insere um novo produto no banco de dados
     */
    public boolean inserir(Produto produto) {
        String sql = "INSERT INTO produtos (nome, categoria, quantidade, valor) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setBigDecimal(4, produto.getValor());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Recuperar o ID gerado
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir produto: " + e.getMessage());
        }

        return false;
    }

    /**
     * Atualiza um produto existente
     */
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, categoria = ?, quantidade = ?, valor = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setBigDecimal(4, produto.getValor());
            stmt.setInt(5, produto.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }

        return false;
    }

    /**
     * Exclui um produto pelo ID
     */
    public boolean excluir(Integer id) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao excluir produto: " + e.getMessage());
        }

        return false;
    }

    /**
     * Busca um produto pelo ID
     */
    public Produto buscarPorId(Integer id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extrairProduto(rs);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto por ID: " + e.getMessage());
        }

        return null;
    }

    /**
     * Lista todos os produtos
     */
    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY nome";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                produtos.add(extrairProduto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }

        return produtos;
    }

    /**
     * Busca produtos por nome (filtro)
     */
    public List<Produto> buscarPorNome(String nome) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE nome LIKE ? ORDER BY nome";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                produtos.add(extrairProduto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos por nome: " + e.getMessage());
        }

        return produtos;
    }

    /**
     * Busca produtos por categoria
     */
    public List<Produto> buscarPorCategoria(String categoria) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos WHERE categoria = ? ORDER BY nome";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                produtos.add(extrairProduto(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar produtos por categoria: " + e.getMessage());
        }

        return produtos;
    }

    /**
     * Lista todas as categorias distintas
     */
    public List<String> listarCategorias() {
        List<String> categorias = new ArrayList<>();
        String sql = "SELECT DISTINCT categoria FROM produtos ORDER BY categoria";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(rs.getString("categoria"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }

        return categorias;
    }

    /**
     * Conta o total de produtos
     */
    public int contarProdutos() {
        String sql = "SELECT COUNT(*) FROM produtos";

        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao contar produtos: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Extrai um objeto Produto do ResultSet
     */
    private Produto extrairProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setCategoria(rs.getString("categoria"));
        produto.setQuantidade(rs.getInt("quantidade"));
        produto.setValor(rs.getBigDecimal("valor"));

        Timestamp dataCadastro = rs.getTimestamp("data_cadastro");
        if (dataCadastro != null) {
            produto.setDataCadastro(dataCadastro.toLocalDateTime());
        }

        Timestamp dataAtualizacao = rs.getTimestamp("data_atualizacao");
        if (dataAtualizacao != null) {
            produto.setDataAtualizacao(dataAtualizacao.toLocalDateTime());
        }

        return produto;
    }
}