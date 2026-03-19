package com.choperia.order_system.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private Integer number;

    @Enumerated(EnumType.STRING)
    private TableStatus status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    // Método de negócio para ocupar a mesa
    public void occupy() {
        if (this.status == TableStatus.FREE) {
            this.status = TableStatus.OCCUPIED;
        }
    }
}