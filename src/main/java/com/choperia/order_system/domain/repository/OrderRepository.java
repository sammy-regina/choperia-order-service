package com.choperia.order_system.domain.repository;

import com.choperia.order_system.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    // Aqui o Spring já nos dá: save, findById, findAll, delete, etc.
}