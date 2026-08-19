package test;

import dao.ProdutoDAO;
import database.ConnectionProvider;
import database.MySqlConnectionProvider;
import model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ProdutoService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoServiceTest {

    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        ConnectionProvider provider = new MySqlConnectionProvider();
        ProdutoDAO produtoDAO = new ProdutoDAO(provider);
        produtoService = new ProdutoService(produtoDAO);
    }

    @Test
    @DisplayName("Deve cadastrar produto e ajustar estoque com sucesso")
    void testFluxoProdutoEEstoque() {
        Produto prod = new Produto("Teclado Teste", "Periféricos", 15, new BigDecimal("200.00"));

        boolean cadastrou = produtoService.cadastrarProduto(prod);
        assertTrue(cadastrou);
        assertNotNull(prod.getId());

        // Ajusta estoque (retira 5 unidades)
        produtoService.ajustarEstoque(prod.getId(), 5, false);

        Produto atualizado = produtoService.buscarPorId(prod.getId());
        assertEquals(10, atualizado.getQuantidade(), "A quantidade em estoque deve ser reduzida para 10.");
    }

    @Test
    @DisplayName("Deve recusar cadastro de produto com preço negativo")
    void testCadastrarProdutoPrecoNegativo() {
        Produto invalido = new Produto("Erro", "Geral", 5, new BigDecimal("-50.00"));

        assertThrows(IllegalArgumentException.class, () -> {
            produtoService.cadastrarProduto(invalido);
        });
    }
}