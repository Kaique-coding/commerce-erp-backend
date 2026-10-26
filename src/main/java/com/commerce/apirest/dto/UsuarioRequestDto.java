package com.commerce.apirest.dto;

import java.time.LocalDate;

import com.commerce.apirest.enums.Genero;
import com.commerce.apirest.enums.PerfilUsuario;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioRequestDto {

	private String usuario;
	private String nome;
	private String sobrenome;
	private String emailPrincipal;
	private String emailSecundario;
	private LocalDate dataNascimento;
	private String cpf;
	private String rg;
	private String senha;
	private String telefonePrincipal;
	private String telefoneSecundario;
	private PerfilUsuario perfilUsuario;
	private Genero genero;
	private Boolean ativo;
}
