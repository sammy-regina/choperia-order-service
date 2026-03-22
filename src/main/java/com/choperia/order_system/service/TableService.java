package com.choperia.order_system.service;

import com.choperia.order_system.domain.model.DiningTable;
import com.choperia.order_system.domain.model.TableStatus;
import com.choperia.order_system.domain.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    // Método necessário para o OrderService persistir mudanças de estado
    @Transactional
    public DiningTable save(DiningTable table) {
        return tableRepository.save(table);
    }

    public List<DiningTable> listAllTables() {
        return tableRepository.findAll();
    }

    public Optional<DiningTable> findByNumber(Integer number) {
        return tableRepository.findByNumber(number);
    }

    @Transactional
    public void changeTableStatus(Integer tableNumber, TableStatus newStatus) {
        tableRepository.findByNumber(tableNumber)
                .ifPresent(table -> {
                    table.setStatus(newStatus);
                    tableRepository.save(table); // Aqui usamos o repositório diretamente ou o método save acima
                });
    }
}