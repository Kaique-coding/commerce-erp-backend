package com.commerce.apirest.dto;

import com.commerce.apirest.enums.PerfilUsuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
	
	private Boolean acessoPermitido;
	private String token;
	private String mensagem;
	private Boolean notificacaoBoasVindas;
	private PerfilUsuario perfilUsuario;

}
