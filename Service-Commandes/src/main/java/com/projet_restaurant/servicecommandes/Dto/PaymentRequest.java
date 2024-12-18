package com.projet_restaurant.servicecommandes.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {
    private Long commandeId;
    private Long clientId;
    private Double totalAmount;
    private String token;
}
