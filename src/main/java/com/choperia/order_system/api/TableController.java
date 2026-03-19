package com.choperia.order_system.api;

import com.choperia.order_system.domain.model.DiningTable;
import com.choperia.order_system.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public ResponseEntity<List<DiningTable>> getAllTables() {
        List<DiningTable> tables = tableService.listAllTables();
        return ResponseEntity.ok(tables);
    }
}
