package com.choperia.order_system.domain.repository;

import com.choperia.order_system.domain.model.Order;
import com.choperia.order_system.domain.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    /**
     * Busca todos os pedidos de uma mesa específica que possuem um determinado status.
     * Útil para recuperar o consumo atual (PENDING) antes do fechamento.
     *
     * O Spring navegará de Order -> DiningTable -> number para realizar o filtro.
     */
    List<Order> findByTableNumberAndStatus(Integer tableNumber, OrderStatus status);
}