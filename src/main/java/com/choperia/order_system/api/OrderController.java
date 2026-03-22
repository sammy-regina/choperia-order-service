package com.choperia.order_system.api;

import com.choperia.order_system.api.dto.BillResponseDTO;
import com.choperia.order_system.api.dto.OrderRequest;
import com.choperia.order_system.domain.model.Order;
import com.choperia.order_system.domain.model.OrderItem;
import com.choperia.order_system.service.OrderService;
import com.choperia.order_system.service.TableService;
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
        // 1. Lógica de entrada: Busca a mesa ou lança IllegalArgumentException (404)
        var table = tableService.findByNumber(request.tableNumber())
                .orElseThrow(() -> new IllegalArgumentException("Mesa " + request.tableNumber() + " não encontrada"));

        // 2. Mapeamento manual: DTO -> Entity
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

        // 3. Execução: O Service valida e lança IllegalStateException (409) se ocupada
        Order savedOrder = orderService.createOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @GetMapping("/bill/{tableNumber}")
    public ResponseEntity<BillResponseDTO> getBill(@PathVariable Integer tableNumber) {
        BillResponseDTO bill = orderService.calculateBill(tableNumber);
        return ResponseEntity.ok(bill);
    }
}