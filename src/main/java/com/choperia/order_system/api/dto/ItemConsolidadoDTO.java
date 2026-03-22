package com.choperia.order_system.api.dto;

import java.math.BigDecimal;

public record ItemConsolidadoDTO(
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {}