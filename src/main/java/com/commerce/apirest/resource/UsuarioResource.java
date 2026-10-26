
package com.commerce.apirest.resource;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.apirest.dto.UsuarioRequestDto;
import com.commerce.apirest.dto.UsuarioResponseDto;
import com.commerce.apirest.enums.PerfilUsuario;
import com.commerce.apirest.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Usuários", description = "Gerenciamento de usuários")
public class UsuarioResource {

	@Autowired
	UsuarioService usuarioService;

	@GetMapping("/usuarios")
	@Operation(summary = "Listar usuários", description = "Retorna todos os usuários cadastrados.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))) })
	public List<UsuarioResponseDto> findAll() {
		return usuarioService.findAll();
	}

	@GetMapping("/usuario/{id}")
	@Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico.", responses = {
			@ApiResponse(responseCode = "200", description = "Usuário encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content) })
	public UsuarioResponseDto findById(@Parameter(description = "ID do usuário") @PathVariable("id") UUID id) {
		return usuarioService.findById(id);
	}

	@PostMapping("/usuario")
	@Operation(summary = "Cadastrar usuário", description = "Cadastra um novo usuário.", responses = {
			@ApiResponse(responseCode = "200", description = "Usuário cadastrado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Dados do usuário inválidos.", content = @Content) })
	public UsuarioResponseDto save(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do usuário a cadastrar", required = true, content = @Content(schema = @Schema(implementation = UsuarioRequestDto.class))) @RequestBody UsuarioRequestDto usuario) {
		return usuarioService.save(usuario, PerfilUsuario.CLIENTE);
	}
	@PostMapping("/usuario/interno")
	@Operation(summary = "Cadastrar usuário interno", description = "Cadastra um novo usuário interno.")
	public UsuarioResponseDto saveInterno(@RequestBody UsuarioRequestDto usuario) {
		return usuarioService.save(usuario, PerfilUsuario.USUARIO_INTERNO);
	}

	@DeleteMapping("/usuario/{id}")
	@Operation(summary = "Excluir usuário", description = "Exclui um usuário com base no ID informado.", responses = {
			@ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content) })
	public void delete(@Parameter(description = "ID do usuário a excluir") @PathVariable("id") UUID id)
			throws Exception {
		usuarioService.delete(id);
	}

	@PutMapping("/usuario/{id}")
	@Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente.", responses = {
			@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Dados do usuário inválidos.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content) })
	public UsuarioResponseDto update(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do usuário a atualizar", required = true, content = @Content(schema = @Schema(implementation = UsuarioRequestDto.class))) @RequestBody UsuarioRequestDto usuario,
			@PathVariable("id") UUID id) throws Exception {
		return usuarioService.update(id, usuario);
	}

	@PatchMapping("/usuario/{id}/status")
	@Operation(summary = "Ativar ou desativar usuário", description = "Altera o status de um usuário existente.", responses = {
			@ApiResponse(responseCode = "200", description = "Status do usuário alterado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Status inválido ou não informado.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content) })
	public UsuarioResponseDto updateStatus(
			@Parameter(description = "ID do usuário", required = true) @PathVariable("id") UUID id,
			@Parameter(description = "Novo status do usuário", required = true, example = "false") @RequestParam Boolean ativo)
			throws Exception {
		return usuarioService.updateStatus(id, ativo);
	}
}
