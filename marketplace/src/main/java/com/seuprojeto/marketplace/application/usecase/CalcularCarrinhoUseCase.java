package com.seuprojeto.marketplace.application.usecase;

import com.seuprojeto.marketplace.application.dto.SelecaoCarrinho;
import com.seuprojeto.marketplace.domain.model.CategoriaProduto;
import com.seuprojeto.marketplace.domain.model.Produto;
import com.seuprojeto.marketplace.domain.model.ResumoCarrinho;
import com.seuprojeto.marketplace.domain.repository.ProdutoRepositorio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class CalcularCarrinhoUseCase {

    private final ProdutoRepositorio produtoRepositorio;

    private static final Map<CategoriaProduto, BigDecimal> DESCONTOS_CATEGORIA = Map.of(
            CategoriaProduto.CAPINHA, new BigDecimal("3"),
            CategoriaProduto.CARREGADOR, new BigDecimal("5"),
            CategoriaProduto.FONE, new BigDecimal("3"),
            CategoriaProduto.PELICULA, new BigDecimal("2"),
            CategoriaProduto.SUPORTE, new BigDecimal("2")
    );

    private static final BigDecimal DESCONTO_MAXIMO = new BigDecimal("25");

    public CalcularCarrinhoUseCase(ProdutoRepositorio produtoRepositorio) {
        this.produtoRepositorio = produtoRepositorio;
    }

    public ResumoCarrinho executar(List<SelecaoCarrinho> selecaoCarrinhos) {
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalItens = 0;
        BigDecimal descontoCategoriaPercentual = BigDecimal.ZERO;

        for (SelecaoCarrinho selecao : selecaoCarrinhos) {
            Produto produto = produtoRepositorio.findById(selecao.getIdProduto())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + selecao.getIdProduto()));
            BigDecimal preco = produto.getPreco();
            int quantidade = selecao.getQuantidade();
            subtotal = subtotal.add(preco.multiply(new BigDecimal(quantidade)));
            totalItens += quantidade;

            BigDecimal descontoCat = DESCONTOS_CATEGORIA.get(produto.getCategoriaProduto());
            descontoCategoriaPercentual = descontoCategoriaPercentual.add(descontoCat.multiply(new BigDecimal(quantidade)));
        }

        // Desconto por quantidade
        BigDecimal descontoQuantidadePercentual;
        if (totalItens == 1) {
            descontoQuantidadePercentual = BigDecimal.ZERO;
        } else if (totalItens == 2) {
            descontoQuantidadePercentual = new BigDecimal("5");
        } else if (totalItens == 3) {
            descontoQuantidadePercentual = new BigDecimal("7");
        } else {
            descontoQuantidadePercentual = new BigDecimal("10");
        }

        // Total desconto percentual
        BigDecimal descontoTotalPercentual = descontoQuantidadePercentual.add(descontoCategoriaPercentual);

        // Aplicar limite máximo
        if (descontoTotalPercentual.compareTo(DESCONTO_MAXIMO) > 0) {
            descontoTotalPercentual = DESCONTO_MAXIMO;
        }

        // Valor desconto
        BigDecimal valorDesconto = subtotal.multiply(descontoTotalPercentual).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        return new ResumoCarrinho(subtotal, valorDesconto);
    }
}
