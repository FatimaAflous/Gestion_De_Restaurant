package com.projet_restaurant.serviceutilisateurs.Service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private Long userId;

    // Constructeur qui prend username, password, authorities et userId
    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(username, password, authorities);  // Appel du constructeur de la classe User
        this.userId = userId;  // Ajout de l'ID utilisateur
    }

    // Getter pour userId
    public Long getUserId() {
        return userId;
    }
}
