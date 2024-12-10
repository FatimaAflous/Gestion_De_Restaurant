package com.projet_restaurant.serviceutilisateurs.Dto;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
