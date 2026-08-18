package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Classe que representa um produto do inventário
 */
public class Produto {

    private Integer id;
    private String nome;
    private String categoria;
    private Integer quantidade;
    private BigDecimal valor;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    // Construtores
    public Produto() {
        this.quantidade = 0;
        this.valor = BigDecimal.ZERO;
    }

    public Produto(String nome, String categoria, Integer quantidade, BigDecimal valor) {
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.valor = valor;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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
     * Calcula o valor total em estoque (quantidade * valor unitário)
     */
    public BigDecimal getValorTotalEstoque() {
        return valor.multiply(new BigDecimal(quantidade));
    }

    /**
     * Verifica se o produto está com estoque baixo (menos de 10 unidades)
     */
    public boolean isEstoqueBaixo() {
        return quantidade < 10;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", quantidade=" + quantidade +
                ", valor=" + valor +
                '}';
    }
}