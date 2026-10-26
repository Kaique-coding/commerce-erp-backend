package com.commerce.apirest.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.commerce.apirest.dto.PedidoClienteResponseDto;
import com.commerce.apirest.dto.PedidoProdutoResponseDto;
import com.commerce.apirest.dto.PedidoResponseDto;
import com.commerce.apirest.models.Pedido;
import com.commerce.apirest.models.PedidoProduto;

@Component
public class PedidoMapper {

	public PedidoResponseDto toDto(Pedido pedido) {
		var dto = new PedidoResponseDto();
		dto.setId(pedido.getId());
		dto.setClienteId(pedido.getCliente().getId());
		dto.setClienteNome(pedido.getCliente().getNome() + " " + pedido.getCliente().getSobrenome());
		var clienteDto = new PedidoClienteResponseDto();
		clienteDto.setId(pedido.getCliente().getId());
		clienteDto.setNome(pedido.getCliente().getNome());
		clienteDto.setSobrenome(pedido.getCliente().getSobrenome());
		clienteDto.setEmailPrincipal(pedido.getCliente().getEmailPrincipal());
		clienteDto.setTelefonePrincipal(pedido.getCliente().getTelefonePrincipal());
		dto.setCliente(clienteDto);
		dto.setStatus(pedido.getStatus());
		dto.setFormaPagamento(pedido.getFormaPagamento());
		dto.setValorTotal(pedido.getValorTotal());
		dto.setCriadoPor(pedido.getCriadoPor());
		dto.setCriadoEm(pedido.getCriadoEm());
		dto.setAtualizadoEm(pedido.getAtualizadoEm());
		dto.setProdutos(pedido.getProdutos().stream().map(this::toProdutoDto).toList());
		return dto;
	}

	private PedidoProdutoResponseDto toProdutoDto(PedidoProduto item) {
		var dto = new PedidoProdutoResponseDto();
		dto.setId(item.getId());
		dto.setProdutoId(item.getProduto().getId());
		dto.setNomeProduto(item.getProduto().getNome());
		dto.setMarca(item.getProduto().getMarca());
		dto.setModelo(item.getProduto().getModelo());
		dto.setUnidadeMedidaVenda(item.getProduto().getUnidadeMedidaVenda() == null
				? null : item.getProduto().getUnidadeMedidaVenda().getDescricao());
		dto.setQuantidade(item.getQuantidade());
		dto.setValorUnitario(item.getValorUnitario());
		dto.setSubtotal(item.getSubtotal());
		return dto;
	}

	public List<PedidoResponseDto> toDto(List<Pedido> pedidos) {
		return pedidos.stream().map(this::toDto).toList();
	}
}