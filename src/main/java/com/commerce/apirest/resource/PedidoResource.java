package com.commerce.apirest.resource;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.apirest.dto.PedidoRequestDto;
import com.commerce.apirest.dto.PedidoResponseDto;
import com.commerce.apirest.enums.PedidoStatus;
import com.commerce.apirest.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Pedidos", description = "Criação e consulta de pedidos")
public class PedidoResource {

	@Autowired
	PedidoService pedidoService;

	@PostMapping("/pedidos")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar pedido", description = "Cria um pedido pendente usando os preços atuais dos produtos ativos.")
	public PedidoResponseDto save(@Valid @RequestBody PedidoRequestDto pedido, Principal principal) {
		return pedidoService.save(pedido, principal.getName());
	}

	@GetMapping("/pedidos")
	@Operation(summary = "Listar pedidos", description = "Clientes consultam os próprios pedidos; usuários internos podem listar todos ou filtrar por cliente.")
	public List<PedidoResponseDto> findAll(
			@Parameter(description = "Filtro opcional por ID do cliente, disponível para usuários internos")
			@RequestParam(required = false) UUID clienteId,
			Principal principal) {
		return pedidoService.findAll(principal.getName(), clienteId);
	}

	@GetMapping("/pedido/{id}")
	@Operation(summary = "Buscar pedido", description = "Retorna um pedido; clientes só podem acessar pedidos próprios.")
	public PedidoResponseDto findById(@PathVariable UUID id, Principal principal) {
		return pedidoService.findById(id, principal.getName());
	}

	@PatchMapping("/pedido/{id}/status")
	@Operation(summary = "Atualizar status do pedido", description = "Altera o status de um pedido. Disponível para usuários internos.")
	public PedidoResponseDto updateStatus(
			@PathVariable UUID id,
			@RequestParam PedidoStatus status,
			Principal principal) {
		return pedidoService.updateStatus(id, status, principal.getName());
	}
}