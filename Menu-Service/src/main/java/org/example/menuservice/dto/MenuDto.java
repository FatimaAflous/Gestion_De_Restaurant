package org.example.menuservice.dto;

import org.example.menuservice.entite.Menu;

import java.util.Base64;

public class MenuDto {
    private long id ;
    private String description ;
    private Double price;
    private String name;
    private String category;
    private String image;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPromotion() {
        return isPromotion;
    }

    public void setPromotion(Boolean promotion) {
        isPromotion = promotion;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private Boolean isPromotion;

    public MenuDto(Menu menu) {
        this.name = menu.getName();
        this.category = menu.getCategory();
        this.image = Base64.getEncoder().encodeToString(menu.getImage());
        this.id= menu.getId();
        this.description = menu.getDescription();
        this.price=menu.getPrice();
        this.isPromotion= menu.getPromotion();
    }
}
