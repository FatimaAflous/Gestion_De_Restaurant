package org.example.reservationservice.repository;

import org.example.reservationservice.entite.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByNom(String nom);

}
