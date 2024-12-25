package org.example.reservationservice.repository;

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


}
