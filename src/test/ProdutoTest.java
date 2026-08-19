package test;

import model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto("Notebook", "Informática", 10, new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Deve calcular corretamente o valor total do estoque")
    void testCalcularValorTotalEstoque() {
        BigDecimal totalEsperado = new BigDecimal("10000.00");
        assertEquals(0, totalEsperado.compareTo(produto.getValorTotalEstoque()));
    }

    @Test
    @DisplayName("Deve identificar corretamente quando o estoque está baixo (< 10)")
    void testIsEstoqueBaixo() {
        assertFalse(produto.isEstoqueBaixo()); // Qtd = 10, não é baixo

        produto.setQuantidade(9);
        assertTrue(produto.isEstoqueBaixo()); // Qtd = 9, estoque baixo
    }

    @Test
    @DisplayName("Deve calcular o valor unitário com desconto de 10%")
    void testCalcularValorComDescontoSucesso() {
        BigDecimal valorComDesconto = produto.calcularValorComDesconto(new BigDecimal("10"));
        BigDecimal valorEsperado = new BigDecimal("900.00");

        assertEquals(0, valorEsperado.compareTo(valorComDesconto));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar aplicar desconto negativo ou superior a 100%")
    void testCalcularValorComDescontoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.calcularValorComDesconto(new BigDecimal("-5"));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            produto.calcularValorComDesconto(new BigDecimal("105"));
        });
    }
}