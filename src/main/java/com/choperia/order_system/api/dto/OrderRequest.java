package com.choperia.order_system.api.dto;

import java.util.List;

public record OrderRequest(
        Integer tableNumber,
        List<OrderItemRequest> items
) {}
