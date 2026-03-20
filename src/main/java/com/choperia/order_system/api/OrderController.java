package com.choperia.order_system.api;

import com.choperia.order_system.api.dto.OrderRequest;
import com.choperia.order_system.domain.model.Order;
import com.choperia.order_system.domain.model.OrderItem;
import com.choperia.order_system.service.OrderService;
import com.choperia.order_system.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final TableService tableService;

    public OrderController(OrderService orderService, TableService tableService) {
        this.orderService = orderService;
        this.tableService = tableService;
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody OrderRequest request) {
        // Buscamos a mesa correspondente
        var table = tableService.findByNumber(request.tableNumber())
                .orElseThrow(() -> new RuntimeException("Mesa não encontrada"));

        // Convertemos o DTO para Entidade (Mapeamento manual para aprender o fluxo)
        Order order = new Order();
        order.setTable(table);

        var items = request.items().stream().map(itemRequest -> {
            OrderItem item = new OrderItem();
            item.setProductName(itemRequest.productName());
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(itemRequest.unitPrice());
            return item;
        }).collect(Collectors.toList());

        order.setItems(items);

        // Salvamos através do Service (que possui a @Transactional)
        Order savedOrder = orderService.createOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }
}
