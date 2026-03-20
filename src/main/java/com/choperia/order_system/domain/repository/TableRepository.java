package com.choperia.order_system.domain.repository;

import com.choperia.order_system.domain.model.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface TableRepository extends JpaRepository<DiningTable, UUID> {
    // Esta assinatura permite que o Spring crie a query SQL automaticamente
    Optional<DiningTable> findByNumber(Integer number);
}
