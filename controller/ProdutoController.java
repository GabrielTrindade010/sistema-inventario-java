package controller;

import dao.ProdutoDAO;
import model.Produto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller para gerenciar a lógica de negócio de Produtos
 */
public class ProdutoController {

    private final ProdutoDAO produtoDAO;

    public ProdutoController() {
        this.produtoDAO = new ProdutoDAO();
    }

    /**
     * Cadastra um novo produto com validações
     */
    public boolean cadastrarProduto(String nome, String categoria,
                                    Integer quantidade, BigDecimal valor) {
        // Validações
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }

        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor deve ser maior ou igual a zero");
        }

        // Criar produto
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setCategoria(categoria);
        produto.setQuantidade(quantidade);
        produto.setValor(valor);

        return produtoDAO.inserir(produto);
    }

    /**
     * Atualiza um produto existente
     */
    public boolean atualizarProduto(Integer id, String nome, String categoria,
                                    Integer quantidade, BigDecimal valor) {
        // Validações
        if (id == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório");
        }

        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }

        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero");
        }

        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor deve ser maior ou igual a zero");
        }

        // Buscar produto existente
        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado");
        }

        // Atualizar dados
        produto.setNome(nome);
        produto.setCategoria(categoria);
        produto.setQuantidade(quantidade);
        produto.setValor(valor);

        return produtoDAO.atualizar(produto);
    }

    /**
     * Exclui um produto
     */
    public boolean excluirProduto(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório");
        }

        return produtoDAO.excluir(id);
    }

    /**
     * Lista todos os produtos
     */
    public List<Produto> listarTodos() {
        return produtoDAO.listarTodos();
    }

    /**
     * Busca produtos por nome
     */
    public List<Produto> buscarPorNome(String nome) {
        return produtoDAO.buscarPorNome(nome);
    }

    /**
     * Busca produtos por categoria
     */
    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoDAO.buscarPorCategoria(categoria);
    }

    /**
     * Busca produto por ID
     */
    public Produto buscarPorId(Integer id) {
        return produtoDAO.buscarPorId(id);
    }

    /**
     * Lista todas as categorias
     */
    public List<String> listarCategorias() {
        return produtoDAO.listarCategorias();
    }

    /**
     * Conta total de produtos
     */
    public int contarProdutos() {
        return produtoDAO.contarProdutos();
    }

    /**
     * Ajusta a quantidade em estoque
     */
    public boolean ajustarEstoque(Integer id, Integer quantidadeAjuste, boolean isEntrada) {
        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado");
        }

        int novaQuantidade;
        if (isEntrada) {
            novaQuantidade = produto.getQuantidade() + quantidadeAjuste;
        } else {
            novaQuantidade = produto.getQuantidade() - quantidadeAjuste;
            if (novaQuantidade < 0) {
                throw new IllegalArgumentException("Quantidade insuficiente em estoque");
            }
        }

        produto.setQuantidade(novaQuantidade);
        return produtoDAO.atualizar(produto);
    }
}