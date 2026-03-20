package com.choperia.order_system.service;

import com.choperia.order_system.domain.model.Order;
import com.choperia.order_system.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Order order) {
        // 1. Calcular subtotais de cada item
        order.getItems().forEach(item -> {
            item.setOrder(order); // Vincula o item ao pedido pai
            item.calculateSubtotal();
        });

        // 2. Calcular o total do pedido somando os subtotais
        BigDecimal total = order.getItems().stream()
                .map(item -> item.getSubtotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);

        // 3. Salvar (O CascadeType.ALL na entidade salvará os itens automaticamente)
        return orderRepository.save(order);
    }
}
