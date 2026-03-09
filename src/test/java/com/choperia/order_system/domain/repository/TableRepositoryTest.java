package com.choperia.order_system.domain.repository;

import com.choperia.order_system.domain.model.DiningTable;
import com.choperia.order_system.domain.model.TableStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("dev") // Usa o H2 para os testes
class TableRepositoryTest {

    @Autowired
    private TableRepository tableRepository;

    @Test
    @DisplayName("Deve persistir uma mesa com sucesso")
    void shouldSaveTable() {
        DiningTable table = DiningTable.builder()
                .number(10)
                .status(TableStatus.FREE)
                .build();

        DiningTable savedTable = tableRepository.save(table);

        assertThat(savedTable.getId()).isNotNull();
        assertThat(savedTable.getNumber()).isEqualTo(10);
        assertThat(savedTable.getStatus()).isEqualTo(TableStatus.FREE);
    }
}
