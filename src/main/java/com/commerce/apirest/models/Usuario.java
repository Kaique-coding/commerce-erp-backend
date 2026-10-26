package com.commerce.apirest.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.commerce.apirest.enums.Genero;
import com.commerce.apirest.enums.PerfilUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private String usuario;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String sobrenome;

	@Column(name = "email_principal", nullable = false, unique = true)
	private String emailPrincipal;
	
	@Column(name = "email_secundario", nullable = true, unique = true)
	private String emailSecundario;

	@Column(name = "data_nascimento", nullable = true)
	private LocalDate dataNascimento;

	@Column(nullable = false, unique = true)
	private String cpf;

	@Column(nullable = true)
	private String rg;

	@Column(nullable = false)
	private String senha;

	@Column(name = "telefone_principal", nullable = false, unique = true)
	private String telefonePrincipal;

	@Column(name = "telefone_secundario", nullable = true, unique = true)
	private String telefoneSecundario;

	@Enumerated(EnumType.STRING)
	@Column(name = "perfil_usuario")
	private PerfilUsuario perfilUsuario;

	@Enumerated(EnumType.STRING)
	@Column(name = "genero")
	private Genero genero;

	@Column(nullable = false)
	private Boolean ativo;

	@Column(name = "criado_por", nullable = true)
	private String criadoPor;

	@Column(name = "criado_em", nullable = false)
	private LocalDateTime criadoEm;

	@Column(name = "notificacao_boas_vindas_pendente", nullable = true)
	private Boolean notificacaoBoasVindasPendente;

	@Column(name = "notificacao_boas_vindas_exibida", nullable = true)
	private Boolean notificacaoBoasVindasExibida;

	@PrePersist
	private void prePersist() {
		this.criadoEm = LocalDateTime.now();
	}

}
