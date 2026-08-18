package service;

import dao.ProdutoDAO;
import model.Produto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Camada de Serviço responsável pelas regras de negócio de Produtos.
 */
public class ProdutoService {

    private final ProdutoDAO produtoDAO;

    // Injeção de Dependência do DAO pelo construtor
    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    /**
     * Cadastra um novo produto após aplicar regras de validação
     */
    public boolean cadastrarProduto(Produto produto) {
        validarProduto(produto);
        return produtoDAO.inserir(produto);
    }

    /**
     * Atualiza um produto existente
     */
    public boolean atualizarProduto(Produto produto) {
        if (produto == null || produto.getId() == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório para atualização.");
        }

        validarProduto(produto);

        if (produtoDAO.buscarPorId(produto.getId()) == null) {
            throw new IllegalArgumentException("Produto não encontrado no sistema.");
        }

        return produtoDAO.atualizar(produto);
    }

    /**
     * Validações centralizadas das regras de negócio do produto
     */
    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Dados do produto não foram fornecidos.");
        }
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        if (produto.getCategoria() == null || produto.getCategoria().trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }
        if (produto.getQuantidade() == null || produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior ou igual a zero.");
        }
        if (produto.getValor() == null || produto.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor deve ser maior ou igual a zero.");
        }
    }

    public boolean excluirProduto(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório.");
        }
        return produtoDAO.excluir(id);
    }

    public List<Produto> listarTodos() {
        return produtoDAO.listarTodos();
    }

    public List<Produto> buscarPorNome(String nome) {
        return produtoDAO.buscarPorNome(nome);
    }

    public List<Produto> buscarPorCategoria(String categoria) {
        return produtoDAO.buscarPorCategoria(categoria);
    }

    public Produto buscarPorId(Integer id) {
        return produtoDAO.buscarPorId(id);
    }

    public List<String> listarCategorias() {
        return produtoDAO.listarCategorias();
    }

    public int contarProdutos() {
        return produtoDAO.contarProdutos();
    }

    /**
     * Regra de negócio para movimentação de estoque
     */
    public boolean ajustarEstoque(Integer id, Integer quantidadeAjuste, boolean isEntrada) {
        if (quantidadeAjuste == null || quantidadeAjuste <= 0) {
            throw new IllegalArgumentException("A quantidade de ajuste deve ser maior que zero.");
        }

        Produto produto = produtoDAO.buscarPorId(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        int novaQuantidade = isEntrada
                ? produto.getQuantidade() + quantidadeAjuste
                : produto.getQuantidade() - quantidadeAjuste;

        if (novaQuantidade < 0) {
            throw new IllegalArgumentException("Estoque insuficiente para realizar a saída.");
        }

        produto.setQuantidade(novaQuantidade);
        return produtoDAO.atualizar(produto);
    }
}