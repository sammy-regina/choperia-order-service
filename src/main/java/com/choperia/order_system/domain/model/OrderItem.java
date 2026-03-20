package com.choperia.order_system.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    // Lógica de negócio: Calcula o subtotal automaticamente
    public void calculateSubtotal() {
        this.subtotal = this.unitPrice.multiply(new BigDecimal(this.quantity));
    }
}