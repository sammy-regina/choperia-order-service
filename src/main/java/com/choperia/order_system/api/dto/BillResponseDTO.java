package com.choperia.order_system.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BillResponseDTO(
        Integer tableNumber,
        LocalDateTime openedAt,
        List<ItemConsolidadoDTO> items,
        BigDecimal totalAmount
) {}