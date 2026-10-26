package com.commerce.apirest.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoImagemResponseDto {

	private UUID id;
	private byte[] imagem;
	private String nomeArquivo;
	private String criadoPor;
	private LocalDateTime criadoEm;
	private LocalDateTime atualizadoEm;
}
