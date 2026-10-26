package com.commerce.apirest.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PedidoClienteResponseDto {

	private UUID id;
	private String nome;
	private String sobrenome;
	private String emailPrincipal;
	private String telefonePrincipal;
}