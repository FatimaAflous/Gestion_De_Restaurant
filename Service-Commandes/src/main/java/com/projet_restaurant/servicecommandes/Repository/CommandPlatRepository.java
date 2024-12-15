package com.projet_restaurant.servicecommandes.Repository;

import com.projet_restaurant.servicecommandes.Entity.CommandPlat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandPlatRepository extends JpaRepository<CommandPlat,Long> {
    List<CommandPlat> findByCommandeId(Long commandeId);

}
