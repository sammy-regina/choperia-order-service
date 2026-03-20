package com.choperia.order_system.api.dto;

import java.math.BigDecimal;

public record OrderItemRequest(
        String productName,
        Integer quantity,
        BigDecimal unitPrice
) {}
