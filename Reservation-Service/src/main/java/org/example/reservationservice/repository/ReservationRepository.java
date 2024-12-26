package org.example.reservationservice.repository;

import org.example.reservationservice.entite.Client;
import org.example.reservationservice.entite.Creneau;
import org.example.reservationservice.entite.Reservation;
import org.example.reservationservice.entite.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository  extends JpaRepository<Reservation, Long> {

    List<Reservation> findByCreneauId(Long creneauId);
    boolean existsByCreneauAndTable(Creneau creneau, Table table);

    // Méthode avec JOIN FETCH pour récupérer les réservations avec toutes les relations
    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.client " +  // Joindre les informations du client
            "JOIN FETCH r.creneau " +  // Joindre les informations du créneau
            "JOIN FETCH r.table")     // Joindre les informations de la table
    List<Reservation> findAllWithDetails();

    // Trouver les réservations par client
    List<Reservation> findByClient(Client client);

}
