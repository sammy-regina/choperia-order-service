package com.choperia.order_system.domain.model;

public enum OrderStatus {
    PENDING,   // Pedido lançado, ainda não pago
    PAID,      // Conta fechada e paga
    CANCELED   // Pedido cancelado (estorno)
}