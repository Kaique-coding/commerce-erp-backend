package com.commerce.apirest.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PedidoProdutoResponseDto {

	private UUID id;
	private UUID produtoId;
	private String nomeProduto;
	private String marca;
	private String modelo;
	private String unidadeMedidaVenda;
	private BigDecimal quantidade;
	private BigDecimal valorUnitario;
	private BigDecimal subtotal;
}