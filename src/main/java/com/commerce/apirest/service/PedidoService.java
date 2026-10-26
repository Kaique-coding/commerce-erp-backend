package com.commerce.apirest.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.commerce.apirest.dto.PedidoProdutoRequestDto;
import com.commerce.apirest.dto.PedidoResponseDto;
import com.commerce.apirest.dto.PedidoRequestDto;
import com.commerce.apirest.enums.PedidoStatus;
import com.commerce.apirest.enums.PerfilUsuario;
import com.commerce.apirest.mapper.PedidoMapper;
import com.commerce.apirest.models.Pedido;
import com.commerce.apirest.models.PedidoProduto;
import com.commerce.apirest.models.Usuario;
import com.commerce.apirest.repository.PedidoRepository;
import com.commerce.apirest.repository.ProdutoRepository;
import com.commerce.apirest.repository.UsuarioRepository;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	PedidoMapper pedidoMapper;

	@Transactional
	public PedidoResponseDto save(PedidoRequestDto dto, String emailAutenticado) {
		var solicitante = buscarUsuario(emailAutenticado);
		validarContaAtiva(solicitante);

		var cliente = resolverCliente(dto, solicitante);
		if (dto.getFormaPagamento() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a forma de pagamento.");
		}

		var pedido = new Pedido();
		pedido.setCliente(cliente);
		pedido.setFormaPagamento(dto.getFormaPagamento());
		pedido.setStatus(PedidoStatus.PENDENTE);
		pedido.setCriadoPor(solicitante.getEmailPrincipal());
		pedido.setValorTotal(BigDecimal.ZERO.setScale(2));

		var produtosNoPedido = new HashSet<UUID>();
		var total = BigDecimal.ZERO;
		for (PedidoProdutoRequestDto itemDto : dto.getProdutos()) {
			if (!produtosNoPedido.add(itemDto.getProdutoId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Envie cada produto apenas uma vez e informe a quantidade desejada.");
			}

			var produto = produtoRepository.findById(itemDto.getProdutoId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
							"Produto não encontrado: " + itemDto.getProdutoId()));
			if (!Boolean.TRUE.equals(produto.getAtivo())) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"O produto '" + produto.getNome() + "' não está disponível.");
			}
			if (produto.getValorVenda() == null || produto.getValorVenda().signum() < 0) {
				throw new ResponseStatusException(HttpStatus.CONFLICT,
						"O produto '" + produto.getNome() + "' não possui preço de venda válido.");
			}
			validarQuantidade(itemDto, produto.getNome());

			var valorUnitario = produto.getValorVenda().setScale(2, RoundingMode.HALF_UP);
			var subtotal = valorUnitario.multiply(itemDto.getQuantidade()).setScale(2, RoundingMode.HALF_UP);
			var pedidoProduto = new PedidoProduto();
			pedidoProduto.setPedido(pedido);
			pedidoProduto.setCliente(cliente);
			pedidoProduto.setProduto(produto);
			pedidoProduto.setQuantidade(itemDto.getQuantidade());
			pedidoProduto.setValorUnitario(valorUnitario);
			pedidoProduto.setSubtotal(subtotal);
			pedido.getProdutos().add(pedidoProduto);
			total = total.add(subtotal);
		}

		pedido.setValorTotal(total.setScale(2, RoundingMode.HALF_UP));
		return pedidoMapper.toDto(pedidoRepository.save(pedido));
	}

	@Transactional(readOnly = true)
	public List<PedidoResponseDto> findAll(String emailAutenticado, UUID clienteFiltro) {
		var solicitante = buscarUsuario(emailAutenticado);
		validarContaAtiva(solicitante);

		if (solicitante.getPerfilUsuario() == PerfilUsuario.USUARIO_INTERNO) {
			var pedidos = clienteFiltro == null
					? pedidoRepository.findAllByOrderByCriadoEmDesc()
					: pedidoRepository.findByClienteIdOrderByCriadoEmDesc(clienteFiltro);
			return pedidoMapper.toDto(pedidos);
		}

		if (clienteFiltro != null && !clienteFiltro.equals(solicitante.getId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"Você só pode consultar seus próprios pedidos.");
		}
		return pedidoMapper.toDto(pedidoRepository.findByClienteIdOrderByCriadoEmDesc(solicitante.getId()));
	}

	@Transactional(readOnly = true)
	public PedidoResponseDto findById(UUID id, String emailAutenticado) {
		var solicitante = buscarUsuario(emailAutenticado);
		validarContaAtiva(solicitante);

		var pedido = solicitante.getPerfilUsuario() == PerfilUsuario.USUARIO_INTERNO
				? pedidoRepository.findById(id)
				: pedidoRepository.findByIdAndClienteId(id, solicitante.getId());
		return pedidoMapper.toDto(pedido.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Pedido não encontrado.")));
	}

	@Transactional
	public PedidoResponseDto updateStatus(UUID id, PedidoStatus status, String emailAutenticado) {
		var solicitante = buscarUsuario(emailAutenticado);
		validarContaAtiva(solicitante);
		if (solicitante.getPerfilUsuario() != PerfilUsuario.USUARIO_INTERNO) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,
					"Somente usuários internos podem alterar o status do pedido.");
		}
		if (status == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o status do pedido.");
		}

		var pedido = pedidoRepository.findById(id).orElseThrow(() ->
				new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado."));
		pedido.setStatus(status);
		return pedidoMapper.toDto(pedidoRepository.save(pedido));
	}

	private Usuario resolverCliente(PedidoRequestDto dto, Usuario solicitante) {
		if (solicitante.getPerfilUsuario() == PerfilUsuario.CLIENTE) {
			if (dto.getClienteId() != null && !dto.getClienteId().equals(solicitante.getId())) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN,
						"Um cliente só pode criar pedidos para a própria conta.");
			}
			return solicitante;
		}
		if (solicitante.getPerfilUsuario() != PerfilUsuario.USUARIO_INTERNO || dto.getClienteId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Informe o cliente do pedido.");
		}

		var cliente = usuarioRepository.findById(dto.getClienteId()).orElseThrow(() ->
				new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
		if (cliente.getPerfilUsuario() != PerfilUsuario.CLIENTE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"O usuário informado não possui perfil de cliente.");
		}
		validarContaAtiva(cliente);
		return cliente;
	}

	private Usuario buscarUsuario(String email) {
		return usuarioRepository.findByEmailPrincipal(email).orElseThrow(() ->
				new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário autenticado não encontrado."));
	}

	private void validarContaAtiva(Usuario usuario) {
		if (!Boolean.TRUE.equals(usuario.getAtivo())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "A conta está desativada.");
		}
	}

	private void validarQuantidade(PedidoProdutoRequestDto item, String nomeProduto) {
		if (item.getQuantidade() == null || item.getQuantidade().signum() <= 0
				|| item.getQuantidade().scale() > 3) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"A quantidade do produto '" + nomeProduto + "' deve ser positiva e ter até três casas decimais.");
		}
	}
}