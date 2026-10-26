
package com.commerce.apirest.mapper;

import org.springframework.stereotype.Component;

import com.commerce.apirest.dto.ProdutoRequestDto;
import com.commerce.apirest.dto.ProdutoResponseDto;
import com.commerce.apirest.models.Produto;

@Component
public class ProdutoMapper {

	public ProdutoResponseDto toDto(Produto produto) {
		if (produto == null) {
			return null;
		}

		ProdutoResponseDto dto = new ProdutoResponseDto();

		dto.setId(produto.getId());
		dto.setNome(produto.getNome());
		dto.setDescricao(produto.getDescricao());
		dto.setMarca(produto.getMarca());
		dto.setModelo(produto.getModelo());
		dto.setCor(produto.getCor());
		dto.setLocalizacao(produto.getLocalizacao());
		dto.setEstoque(produto.getEstoque());
		dto.setEstoqueMinimo(produto.getEstoqueMinimo());
		dto.setEstoqueMaximo(produto.getEstoqueMaximo());
 
		dto.setUnidadeMedidaCompra(
				produto.getUnidadeMedidaCompra() != null ? produto.getUnidadeMedidaCompra().getDescricao() : null);

		dto.setUnidadeMedidaVenda(
				produto.getUnidadeMedidaVenda() != null ? produto.getUnidadeMedidaVenda().getDescricao() : null);

		dto.setValorVenda(produto.getValorVenda());
		dto.setValorCompra(produto.getValorCompra());
		dto.setAtivo(produto.getAtivo());
		dto.setCriadoPor(produto.getCriadoPor());
		dto.setCriadoEm(produto.getCriadoEm());
		dto.setAtualizadoEm(produto.getAtualizadoEm());

		return dto;
	}

	public Produto toEntity(ProdutoRequestDto dto) {
		if (dto == null) {
			return null;
		}

		Produto produto = new Produto();

		produto.setNome(dto.getNome());
		produto.setDescricao(dto.getDescricao());
		produto.setMarca(dto.getMarca());
		produto.setModelo(dto.getModelo());
		produto.setCor(dto.getCor());
		produto.setLocalizacao(dto.getLocalizacao());
		produto.setEstoque(dto.getEstoque());
		produto.setEstoqueMinimo(dto.getEstoqueMinimo());
		produto.setEstoqueMaximo(dto.getEstoqueMaximo());
		produto.setUnidadeMedidaCompra(dto.getUnidadeMedidaCompra());
		produto.setUnidadeMedidaVenda(dto.getUnidadeMedidaVenda());
		produto.setValorVenda(dto.getValorVenda());
		produto.setValorCompra(dto.getValorCompra());
		produto.setAtivo(dto.getAtivo());

		return produto;
	}
}
