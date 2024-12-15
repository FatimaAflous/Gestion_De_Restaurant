package com.projet_restaurant.servicecommandes.Repository;

import com.projet_restaurant.servicecommandes.Entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandRepository extends JpaRepository<Commande,Long> {
    List<Commande> findByClientId(Long clientId); // Utilise camelCase aligné avec l'entité
}
