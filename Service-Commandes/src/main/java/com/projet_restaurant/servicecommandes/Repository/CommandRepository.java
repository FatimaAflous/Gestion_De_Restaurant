package com.projet_restaurant.servicecommandes.Repository;

import com.projet_restaurant.servicecommandes.Entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommandRepository extends JpaRepository<Commande,Long> {

    @Query("SELECT c FROM Commande c WHERE c.clientId = :clientId")
    List<Commande> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT c FROM Commande c ORDER BY c.id DESC")
    List<Commande> findAllByOrderByIdDesc();
}
