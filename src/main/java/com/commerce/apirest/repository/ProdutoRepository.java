package com.commerce.apirest.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.apirest.models.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID>{

}
