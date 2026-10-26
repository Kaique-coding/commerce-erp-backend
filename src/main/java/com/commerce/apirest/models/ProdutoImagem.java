package com.commerce.apirest.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_produto_imagem")
public class ProdutoImagem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "produto_id", nullable = false)
	private Produto produto;

	@NotNull
	@Column(name = "imagem", nullable = false, columnDefinition = "bytea")
	private byte[] imagem;

	@Column(name = "nome_arquivo", length = 255)
	private String nomeArquivo;

	@NotBlank
	@Column(name = "criado_por", nullable = false, length = 100)
	private String criadoPor;

	@NotNull
	@Column(name = "criado_em", nullable = false, updatable = false)
	private LocalDateTime criadoEm;

	@Column(name = "atualizado_em")
	private LocalDateTime atualizadoEm;

	@PrePersist
	protected void aoCriar() {
		if (criadoEm == null) {
			criadoEm = LocalDateTime.now();
		}
	}

	@PreUpdate
	protected void aoAtualizar() {
		atualizadoEm = LocalDateTime.now();
	}
}