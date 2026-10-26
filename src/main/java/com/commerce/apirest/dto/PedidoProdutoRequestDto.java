package com.commerce.apirest.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PedidoProdutoRequestDto {

	@NotNull
	private UUID produtoId;

	@NotNull
	@DecimalMin(value = "0.001", message = "A quantidade deve ser maior que zero.")
	@Digits(integer = 11, fraction = 3, message = "Informe até três casas decimais para a quantidade.")
	private BigDecimal quantidade;
}