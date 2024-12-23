package com.projet_restaurant.servicecommandes.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idProduct;
    private String productName;
    private Double price;
    private int quantity;

    @Lob
    private byte[] image;  // Image du produit

    public Order getOrder() {
        return order;
    }
    @ManyToOne
    @JoinColumn(name = "order_id") // Colonne de jointure
    @JsonIgnore  // Ignorer la sérialisation de la relation bidirectionnelle
    private Order order;

    // getters et setters



    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Long idProduct) {
        this.idProduct = idProduct;
    }
    // Getters et setters
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    // Getter pour l'image en Base64
    // Méthode pour décoder l'image de Base64 et la convertir en tableau de bytes

}
