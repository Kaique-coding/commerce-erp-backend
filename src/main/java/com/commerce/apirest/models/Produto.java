package com.commerce.apirest.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.commerce.apirest.enums.UnidadeMedida;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_produto")
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = true, columnDefinition = "TEXT")
	private String descricao;

	@Column(nullable = true, length = 100)
	private String marca;

	@Column(nullable = true, length = 100)
	private String modelo;
	
	@Column(nullable = true)
	private String cor;
	
	@Column(nullable = true)
	private String localizacao;

	@Column(nullable = true)
	private BigDecimal estoque;

	@Column(name = "estoque_minimo", nullable = true)
	private BigDecimal estoqueMinimo;

	@Column(name = "estoque_maximo", nullable = true)
	private BigDecimal estoqueMaximo;

	@Enumerated(EnumType.STRING)
	@Column(name = "unidade_medida_compra")
	private UnidadeMedida unidadeMedidaCompra;

	@Enumerated(EnumType.STRING)
	@Column(name = "unidade_medida_venda")
	private UnidadeMedida unidadeMedidaVenda;

	@Column(name = "valor_venda", nullable = false)
	private BigDecimal valorVenda;

	@Column(name = "valor_compra", nullable = true)
	private BigDecimal valorCompra;

	@Column(nullable = false)
	private Boolean ativo;
	
	@Column(name = "criado_por", nullable = false)
	private String criadoPor;

	@Column(name = "criado_em", nullable = false)
	private LocalDateTime criadoEm;

	@Column(name = "atualizado_em", nullable = true)
	private LocalDateTime atualizadoEm;

	@PreUpdate
	private void preUpdate() {
		this.atualizadoEm = LocalDateTime.now();
	}

	@PrePersist
	private void prePersist() {
		this.criadoEm = LocalDateTime.now();
	}
}
