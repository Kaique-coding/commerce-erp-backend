	package com.commerce.apirest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoResponseDto {

	private UUID id;
	private String nome;
	private String descricao;
	private String marca;
	private String modelo;
	private String cor;
	private String localizacao;
	private BigDecimal estoque;
	private BigDecimal estoqueMinimo;
	private BigDecimal estoqueMaximo;
	private String unidadeMedidaCompra;
	private String unidadeMedidaVenda;
	private BigDecimal valorVenda;
	private BigDecimal valorCompra;
	private Boolean ativo;
	private String criadoPor;
	private LocalDateTime criadoEm;
	private LocalDateTime atualizadoEm;
	private ProdutoImagemResponseDto imagem;
}
