package com.commerce.apirest.dto;

import java.util.List;
import java.util.UUID;

import com.commerce.apirest.enums.FormaPagamento;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PedidoRequestDto {

	/** Obrigatório para usuário interno; cliente autenticado usa a própria conta. */
	private UUID clienteId;

	@NotNull(message = "Informe a forma de pagamento.")
	private FormaPagamento formaPagamento;

	@NotEmpty(message = "O pedido deve conter ao menos um produto.")
	private List<@Valid PedidoProdutoRequestDto> produtos;
}