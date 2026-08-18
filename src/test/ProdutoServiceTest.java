package test;

import model.Produto;
import service.ProdutoService;
import java.math.BigDecimal;

public class ProdutoServiceTest {
    public static void executar(ProdutoService produtoService) {
        System.out.println("\n--- [TESTE] Produto Service ---");

        try {
            Produto prod = new Produto("Monitor 27", "Monitores", 10, new BigDecimal("1200.00"));
            boolean cadastrou = produtoService.cadastrarProduto(prod);

            TestRunner.assertIsTrue(
                    cadastrou && prod.getId() != null,
                    "Produto cadastrado! ID: " + prod.getId(),
                    "Falha ao cadastrar produto."
            );

            if (prod.getId() != null) {
                produtoService.ajustarEstoque(prod.getId(), 2, false);
                Produto atualizado = produtoService.buscarPorId(prod.getId());
                TestRunner.assertIsTrue(
                        atualizado != null && atualizado.getQuantidade() == 8,
                        "Ajuste de estoque OK. Nova quantidade: " + (atualizado != null ? atualizado.getQuantidade() : 0),
                        "Falha no ajuste de estoque."
                );
            }
        } catch (Exception e) {
            TestRunner.reportarFalha("Erro no teste de Produto: " + e.getMessage());
        }

        try {
            Produto invalido = new Produto("Erro", "Geral", 5, new BigDecimal("-10.00"));
            produtoService.cadastrarProduto(invalido);
            TestRunner.reportarFalha("Permitiu preço negativo.");
        } catch (IllegalArgumentException e) {
            TestRunner.assertIsTrue(true, "Bloqueio de preço negativo OK: " + e.getMessage(), "");
        } catch (Exception e) {
            TestRunner.reportarFalha("Erro no teste de preço negativo: " + e.getMessage());
        }
    }
}