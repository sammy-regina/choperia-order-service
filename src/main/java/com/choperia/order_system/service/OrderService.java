package com.choperia.order_system.service;

import com.choperia.order_system.domain.model.DiningTable;
import com.choperia.order_system.domain.model.Order;
import com.choperia.order_system.domain.model.TableStatus;
import com.choperia.order_system.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.choperia.order_system.domain.model.OrderItem;
import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final TableService tableService;

    public OrderService(OrderRepository orderRepository, TableService tableService) {
        this.orderRepository = orderRepository;
        this.tableService = tableService;
    }

    @Transactional
    public Order createOrder(Order order) {
        // 1. Buscamos o estado atual da mesa
        DiningTable table = tableService.findByNumber(order.getTable().getNumber())
                .orElseThrow(() -> new IllegalArgumentException("Mesa não encontrada"));

        // 2. NOVA REGRA: A mesa só não pode receber pedidos se estiver, por exemplo, INATIVA.
        // Se estiver FREE ou OCCUPIED, o pedido segue.

        // 3. Atualização de Estado: Se estava FREE, vira OCCUPIED.
        // Se já estava OCCUPIED, permanece OCCUPIED.
        if (table.getStatus() == TableStatus.FREE) {
            table.setStatus(TableStatus.OCCUPIED);
            tableService.save(table);
        }

        order.setTable(table);

        // 4. Processamento dos Itens e Cálculos (Mantemos o que já funciona)
        order.getItems().forEach(item -> {
            item.setOrder(order);
            item.calculateSubtotal();
        });

        BigDecimal total = order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

}
