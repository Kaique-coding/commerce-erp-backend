
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

import com.commerce.apirest.dto.ProdutoRequestDto;
import com.commerce.apirest.dto.ProdutoResponseDto;
import com.commerce.apirest.service.ProdutoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Produtos", description = "Gerenciamento de produtos")
public class ProdutoResource {

	@Autowired
	ProdutoService produtoService;

	@GetMapping("/produtos")
	@Operation(summary = "Listar produtos", description = "Retorna todos os produtos cadastrados.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDto.class))) })
	public List<ProdutoResponseDto> findAll() {
		return produtoService.findAll();
	}

	@GetMapping("/produto/{id}")
	@Operation(summary = "Buscar produto por ID", description = "Retorna os dados de um produto específico.", responses = {
			@ApiResponse(responseCode = "200", description = "Produto encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDto.class))),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado.", content = @Content) })
	public ProdutoResponseDto findById(
			@Parameter(description = "ID do produto", example = "1") @PathVariable("id") UUID id) {
		return produtoService.findById(id);
	}

	@PostMapping("/produto")
	@Operation(summary = "Cadastrar produto", description = "Cadastra um novo produto.", responses = {
			@ApiResponse(responseCode = "200", description = "Produto cadastrado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Dados do produto inválidos.", content = @Content) })
	public ProdutoResponseDto save(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do produto a cadastrar", required = true, content = @Content(schema = @Schema(implementation = ProdutoRequestDto.class))) @RequestBody ProdutoRequestDto produto) {
		return produtoService.save(produto);
	}

	@DeleteMapping("/produto/{id}")
	@Operation(summary = "Excluir produto", description = "Exclui um produto com base nos dados enviados no corpo da requisição.", responses = {
			@ApiResponse(responseCode = "200", description = "Requisição processada sem conteúdo de resposta.", content = @Content),
			@ApiResponse(responseCode = "400", description = "Dados inválidos para exclusão.", content = @Content) })
	public void delete(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Id do produto a excluir", required = true) @PathVariable("id") UUID id)
			throws Exception {
		produtoService.delete(id);
	}

	@PutMapping("/produto/{id}")
	@Operation(summary = "Cadastrar ou atualizar produto", description = "Salva os dados do produto. O comportamento de inclusão ou atualização depende da entidade e de sua identificação.", responses = {
			@ApiResponse(responseCode = "200", description = "Produto salvo com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Dados do produto inválidos.", content = @Content) })
	public ProdutoResponseDto update(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do produto a salvar", required = true) @RequestBody ProdutoRequestDto produto,
			@PathVariable("id") UUID id) throws Exception {
		return produtoService.update(id, produto);
	}

	@PatchMapping("/produto/{id}/status")
	@Operation(summary = "Ativar ou desativar produto", description = "Altera o status de um produto existente.", responses = {
			@ApiResponse(responseCode = "200", description = "Status do produto alterado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Status inválido ou não informado.", content = @Content),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado.", content = @Content) })
	public ProdutoResponseDto updateStatus(
			@Parameter(description = "ID do produto", required = true) @PathVariable("id") UUID id,
			@Parameter(description = "Novo status do produto", required = true, example = "false") @RequestParam Boolean ativo)
			throws Exception {
		return produtoService.updateStatus(id, ativo);
	}
}
