package com.commerce.apirest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.commerce.apirest.enums.FormaPagamento;
import com.commerce.apirest.enums.PedidoStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PedidoResponseDto {

	private UUID id;
	private UUID clienteId;
	private String clienteNome;
	private PedidoClienteResponseDto cliente;
	private PedidoStatus status;
	private FormaPagamento formaPagamento;
	private BigDecimal valorTotal;
	private String criadoPor;
	private LocalDateTime criadoEm;
	private LocalDateTime atualizadoEm;
	private List<PedidoProdutoResponseDto> produtos;
}