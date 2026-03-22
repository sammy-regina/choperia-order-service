package com.choperia.order_system.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "dining_tables")
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status;

    // --- Construtores ---

    public DiningTable() {
        // Garantimos que toda mesa nova nasça LIVRE por padrão
        this.status = TableStatus.FREE;
    }

    public DiningTable(Integer number, TableStatus status) {
        this.number = number;
        this.status = status;
    }

    // --- Métodos de Domínio (Business Logic) ---

    public void occupy() {
        if (this.status == TableStatus.OCCUPIED) {
            throw new IllegalStateException("Conflito: A mesa " + this.number + " já está ocupada!");
        }
        this.status = TableStatus.OCCUPIED;
    }

    public void release() {
        this.status = TableStatus.FREE;
    }

    // --- Getters e Setters Manuais ---

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
}