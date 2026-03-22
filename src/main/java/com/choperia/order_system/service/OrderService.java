package com.choperia.order_system.service;

import com.choperia.order_system.api.dto.BillResponseDTO;
import com.choperia.order_system.api.dto.ItemConsolidadoDTO;
import com.choperia.order_system.domain.model.DiningTable;
import com.choperia.order_system.domain.model.Order;
import com.choperia.order_system.domain.model.TableStatus;
import com.choperia.order_system.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.choperia.order_system.domain.model.OrderItem;
import java.math.BigDecimal;
import com.choperia.order_system.domain.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Order> getPendingOrdersByTable(Integer tableNumber) {
        // 1. Validamos se a mesa existe
        tableService.findByNumber(tableNumber)
                .orElseThrow(() -> new IllegalArgumentException("Mesa " + tableNumber + " não encontrada"));

        // 2. Buscamos apenas os pedidos que ainda não foram pagos (Status PENDING)
        return orderRepository.findByTableNumberAndStatus(tableNumber, OrderStatus.PENDING);
    }

    /**
     * Calcula o valor total acumulado de todos os pedidos pendentes de uma mesa.
     */
    public BigDecimal calculateTotalPending(Integer tableNumber) {
        List<Order> pendingOrders = getPendingOrdersByTable(tableNumber);

        return pendingOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public BillResponseDTO calculateBill(Integer tableNumber) {
        // 1. Busca os pedidos PENDING
        List<Order> pendingOrders = getPendingOrdersByTable(tableNumber);

        if (pendingOrders.isEmpty()) {
            throw new IllegalStateException("Não há pedidos pendentes para a mesa " + tableNumber);
        }

        // 2. Consolida os itens usando Stream API
        // Agrupamos por nome do produto e somamos quantidades e subtotais
        List<ItemConsolidadoDTO> consolidatedItems = pendingOrders.stream()
                .flatMap(order -> order.getItems().stream()) // Transforma lista de listas em uma lista única de itens
                .collect(Collectors.groupingBy(
                        OrderItem::getProductName,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    Integer totalQty = list.stream().mapToInt(OrderItem::getQuantity).sum();
                                    BigDecimal unitPrice = list.get(0).getUnitPrice(); // Assume que o preço é o mesmo
                                    return new ItemConsolidadoDTO(
                                            list.get(0).getProductName(),
                                            totalQty,
                                            unitPrice,
                                            unitPrice.multiply(BigDecimal.valueOf(totalQty))
                                    );
                                }
                        )
                ))
                .values().stream().toList();

        // 3. Calcula o total geral
        BigDecimal totalGeneral = consolidatedItems.stream()
                .map(ItemConsolidadoDTO::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Pega a data do primeiro pedido para o "openedAt"
        LocalDateTime openedAt = pendingOrders.stream()
                .map(Order::getCreatedAt)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        return new BillResponseDTO(tableNumber, openedAt, consolidatedItems, totalGeneral);
    }
}
