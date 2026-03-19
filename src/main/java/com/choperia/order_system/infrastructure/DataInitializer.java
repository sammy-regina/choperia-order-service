package com.choperia.order_system.infrastructure;

import com.choperia.order_system.domain.model.DiningTable;
import com.choperia.order_system.domain.model.TableStatus;
import com.choperia.order_system.domain.repository.TableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TableRepository tableRepository;

    // Construtor manual para evitar problemas com o Lombok por enquanto
    public DataInitializer(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Só cria se o banco estiver vazio (evita duplicar mesas se reiniciado)
        if (tableRepository.count() == 0) {
            for (int i = 1; i <= 60; i++) {
                DiningTable table = new DiningTable();
                table.setNumber(i);
                table.setStatus(TableStatus.FREE);
                tableRepository.save(table);
            }
            System.out.println("--------------------------------------");
            System.out.println(">> SUCESSO: 60 mesas inicializadas!");
            System.out.println("--------------------------------------");
        }
    }
}
