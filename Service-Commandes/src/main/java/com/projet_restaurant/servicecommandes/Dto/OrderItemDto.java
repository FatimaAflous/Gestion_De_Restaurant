package com.projet_restaurant.servicecommandes.Dto;

import com.projet_restaurant.servicecommandes.Entity.OrderItem;

import java.util.Base64;

public class OrderItemDto {

    private Long id;
    private Long idProduct;
    private String productName;
    private Double price;
    private int quantity;
    private String imageBase64;  // Image convertie en Base64

    // Constructeur
    public OrderItemDto(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.idProduct = orderItem.getIdProduct();
        this.productName = orderItem.getProductName();
        this.price = orderItem.getPrice();
        this.quantity = orderItem.getQuantity();
        if (orderItem.getImage() != null) {
            // Conversion de l'image en Base64
            this.imageBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(orderItem.getImage());
            System.out.println("deboggage :Image Base64 pour l'item " + this.id + " : " + this.imageBase64);  // Debug

        }
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}
