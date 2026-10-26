package com.commerce.apirest.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.commerce.apirest.models.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

	List<Pedido> findAllByOrderByCriadoEmDesc();

	List<Pedido> findByClienteIdOrderByCriadoEmDesc(UUID clienteId);

	Optional<Pedido> findByIdAndClienteId(UUID id, UUID clienteId);
}