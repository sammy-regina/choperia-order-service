package com.choperia.order_system.service;

import com.choperia.order_system.domain.model.DiningTable;
import com.choperia.order_system.domain.model.TableStatus;
import com.choperia.order_system.domain.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<DiningTable> listAllTables() {
        return tableRepository.findAll();
    }

    @Transactional
    public void changeTableStatus(Integer tableNumber, TableStatus newStatus) {
        tableRepository.findByNumber(tableNumber)
                .ifPresent(table -> {
                    table.setStatus(newStatus);
                    tableRepository.save(table);
                });
    }
}
