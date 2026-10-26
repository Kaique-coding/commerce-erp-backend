package com.commerce.apirest.dto;

import java.math.BigDecimal;

import com.commerce.apirest.enums.UnidadeMedida;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoRequestDto {

	private String nome;
	private String descricao;
	private String marca;
	private String modelo;
	private String cor;
	private String localizacao;
	private BigDecimal estoque;
	private BigDecimal estoqueMinimo;
	private BigDecimal estoqueMaximo;
	private UnidadeMedida unidadeMedidaCompra;
	private UnidadeMedida unidadeMedidaVenda;
	private BigDecimal valorVenda;
	private BigDecimal valorCompra;
	private Boolean ativo;
	private byte[] imagem;
	private String nomeArquivo;
}
