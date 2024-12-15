package com.projet_restaurant.servicecommandes.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private String image;
    private Boolean promotion;
}
