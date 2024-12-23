package com.projet_restaurant.servicecommandes.Dto;

import com.projet_restaurant.servicecommandes.Entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {

    private Long id;
    private Long userId;
    private Double total;
    private String status;
    private List<OrderItemDto> items;

    // Constructeur
    public OrderDto(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.total = order.getTotal();
        this.status = order.getStatus().toString();  // En fonction de l'énumération de status
        this.items = order.getItems().stream()
                .map(OrderItemDto::new)  // Convertir chaque OrderItem en OrderItemDto
                .collect(Collectors.toList());
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }
}
