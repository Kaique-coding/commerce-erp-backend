package com.commerce.apirest.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.apirest.dto.UsuarioRequestDto;
import com.commerce.apirest.dto.UsuarioResponseDto;
import com.commerce.apirest.enums.PerfilUsuario;
import com.commerce.apirest.mapper.UsuarioMapper;
import com.commerce.apirest.repository.UsuarioRepository;
import com.commerce.apirest.util.HashUtil;
import com.commerce.apirest.util.PasswordUtil;

@Service
public class UsuarioService {

	@Autowired
	HashUtil hashUtil;
	
	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	UsuarioMapper usuarioMapper;

	public UsuarioResponseDto save(UsuarioRequestDto dto, PerfilUsuario perfilUsuario) {
		var entity = usuarioMapper.toEntity(dto);
		entity.setPerfilUsuario(perfilUsuario);
		entity.setNotificacaoBoasVindasPendente(true);
		String senha = dto.getSenha();
		
		if (senha == null || senha.isBlank()) {
			senha = PasswordUtil.gerarSenha();
		}
		
		System.out.println(senha);
		entity.setSenha(hashUtil.calculateHash(senha, "SHA256"));

		var response = usuarioRepository.save(entity);

		return usuarioMapper.toDto(response);
	}

	public UsuarioResponseDto findById(UUID id) {
		var usuario = usuarioRepository.findById(id);

		if (usuario.isPresent()) {
			return usuarioMapper.toDto(usuario.get());
		}

		return null;
	}

	public UsuarioResponseDto update(UUID id, UsuarioRequestDto dto) throws Exception {
		var usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new Exception("Usuario não encontrado com ID: " + id));

		usuario.setNome(dto.getNome());
		usuario.setSobrenome(dto.getSobrenome());
		usuario.setPerfilUsuario(dto.getPerfilUsuario());
		usuario.setCpf(dto.getCpf());
		usuario.setEmailPrincipal(dto.getEmailPrincipal());
		usuario.setEmailSecundario(dto.getEmailSecundario());
		usuario.setTelefonePrincipal(dto.getTelefonePrincipal());
		usuario.setTelefoneSecundario(dto.getTelefoneSecundario());
		usuario.setGenero(dto.getGenero());
		usuario.setAtivo(dto.getAtivo());
		usuario.setDataNascimento(dto.getDataNascimento());
		usuario.setRg(dto.getRg());
		if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
			usuario.setSenha(hashUtil.calculateHash(dto.getSenha(), "SHA256"));
		}

		var response = usuarioRepository.save(usuario);

		return usuarioMapper.toDto(response);
	}

	public void delete(UUID id) throws Exception {
		var usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new Exception("Usuario não encontrado com o ID: " + id));

		usuarioRepository.delete(usuario);
	}

	public List<UsuarioResponseDto> findAll() {
		return usuarioRepository.findAll().stream().map(usuarioMapper::toDto).toList();
	}

	public UsuarioResponseDto updateStatus(UUID id, Boolean ativo) throws Exception {
		var usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new Exception("Usuario não encontrado com o ID: " + id));

		if (ativo == null) {
			throw new IllegalArgumentException("O status do usuario deve ser informado.");
		}

		usuario.setAtivo(ativo);

		var response = usuarioRepository.save(usuario);

		return usuarioMapper.toDto(response);
	}
}
