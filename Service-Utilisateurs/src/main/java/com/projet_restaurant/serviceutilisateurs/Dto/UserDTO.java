package com.projet_restaurant.serviceutilisateurs.Dto;

import com.projet_restaurant.serviceutilisateurs.Entity.Role;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    @NotBlank(message = "L'id est obligatoire")
    private Long id;
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;
    @NotBlank(message = "Le rôle est obligatoire")
    private Role role;
}

