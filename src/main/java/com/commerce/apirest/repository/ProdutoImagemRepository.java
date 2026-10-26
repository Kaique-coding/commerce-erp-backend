package com.commerce.apirest.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.apirest.models.ProdutoImagem;

@Repository
public interface ProdutoImagemRepository extends JpaRepository<ProdutoImagem, UUID> {

	Optional<ProdutoImagem> findByProdutoId(UUID idProduto);

	List<ProdutoImagem> findByProdutoIdInOrderByCriadoEmDesc(List<UUID> idsProduto);
}