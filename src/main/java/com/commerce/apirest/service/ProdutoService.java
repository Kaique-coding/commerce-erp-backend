package com.commerce.apirest.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.commerce.apirest.dto.ProdutoImagemResponseDto;
import com.commerce.apirest.dto.ProdutoRequestDto;
import com.commerce.apirest.dto.ProdutoResponseDto;
import com.commerce.apirest.mapper.ProdutoMapper;
import com.commerce.apirest.models.Produto;
import com.commerce.apirest.models.ProdutoImagem;
import com.commerce.apirest.repository.ProdutoImagemRepository;
import com.commerce.apirest.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	ProdutoImagemRepository produtoImagemRepository;

	@Autowired
	ProdutoMapper produtoMapper;

	public ProdutoResponseDto save(ProdutoRequestDto dto) {
		var criadoPor = "00000000000";
		var entity = produtoMapper.toEntity(dto);
		entity.setCriadoPor(criadoPor);

		var produtoSalvo = produtoRepository.save(entity);
		ProdutoImagem imagemSalva = null;

		if (Objects.nonNull(dto.getImagem()) && dto.getImagem().length > 0
				&& Objects.nonNull(dto.getNomeArquivo())) {
			var imagem = new ProdutoImagem();
			imagem.setCriadoEm(LocalDateTime.now());
			imagem.setCriadoPor(criadoPor);
			imagem.setImagem(dto.getImagem());
			imagem.setNomeArquivo(dto.getNomeArquivo());
			imagem.setProduto(produtoSalvo);

			imagemSalva = produtoImagemRepository.save(imagem);
		}

		return montarResposta(produtoSalvo, imagemSalva);
	}

	public ProdutoResponseDto findById(UUID id) {
		var produto = produtoRepository.findById(id);
		if (produto.isEmpty()) {
			return null;
		}

		var imagem = produtoImagemRepository.findByProdutoId(id).orElse(null);
		return montarResposta(produto.get(), imagem);
	}

	public ProdutoResponseDto update(UUID id, ProdutoRequestDto dto) throws Exception {
		var produto = produtoRepository.findById(id)
				.orElseThrow(() -> new Exception("Produto não encontrado com o ID: " + id));

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

		var response = produtoRepository.save(produto);
		var imagemAtual = produtoImagemRepository.findByProdutoId(id).orElse(null);
		if (dto.getImagem() != null && dto.getImagem().length > 0 && dto.getNomeArquivo() != null) {
			if (imagemAtual == null) {
				imagemAtual = new ProdutoImagem();
				imagemAtual.setProduto(response);
				imagemAtual.setCriadoPor(response.getCriadoPor());
				imagemAtual.setCriadoEm(LocalDateTime.now());
			}
			imagemAtual.setImagem(dto.getImagem());
			imagemAtual.setNomeArquivo(dto.getNomeArquivo());
			imagemAtual.setProduto(response);
			imagemAtual = produtoImagemRepository.save(imagemAtual);
		}
		return montarResposta(response, imagemAtual);
	}

	public void delete(UUID id) throws Exception {
		var produto = produtoRepository.findById(id)
				.orElseThrow(() -> new Exception("Produto não encontrado com o ID: " + id));

		produtoRepository.delete(produto);
	}

	public List<ProdutoResponseDto> findAll() {
		var produtos = produtoRepository.findAll();
		if (produtos.isEmpty()) {
			return List.of();
		}

		var idsProduto = produtos.stream().map(Produto::getId).toList();
		var imagensPorProduto = new HashMap<UUID, ProdutoImagem>();


		for (var imagem : produtoImagemRepository.findByProdutoIdInOrderByCriadoEmDesc(idsProduto)) {
			imagensPorProduto.putIfAbsent(imagem.getProduto().getId(), imagem);
		}

		return produtos.stream()
				.map(produto -> montarResposta(produto, imagensPorProduto.get(produto.getId())))
				.toList();
	}

	public ProdutoResponseDto updateStatus(UUID id, Boolean ativo) throws Exception {
		var produto = produtoRepository.findById(id)
				.orElseThrow(() -> new Exception("Produto não encontrado com o ID: " + id));

		if (ativo == null) {
			throw new IllegalArgumentException("O status do produto deve ser informado.");
		}

		produto.setAtivo(ativo);
		var response = produtoRepository.save(produto);
		return montarResposta(response, produtoImagemRepository.findByProdutoId(id).orElse(null));
	}

	private ProdutoResponseDto montarResposta(Produto produto, ProdutoImagem imagem) {
		var dto = produtoMapper.toDto(produto);
		if (imagem != null) {
			var imagemDto = new ProdutoImagemResponseDto();
			imagemDto.setId(imagem.getId());
			imagemDto.setImagem(imagem.getImagem());
			imagemDto.setNomeArquivo(imagem.getNomeArquivo());
			imagemDto.setCriadoPor(imagem.getCriadoPor());
			imagemDto.setCriadoEm(imagem.getCriadoEm());
			imagemDto.setAtualizadoEm(imagem.getAtualizadoEm());
			dto.setImagem(imagemDto);
		}
		return dto;
	}
}