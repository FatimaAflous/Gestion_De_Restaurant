package org.example.reservationservice.repository;

import org.example.reservationservice.entite.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableRepository extends JpaRepository<Table,Long> {
    List<Table> findAll();

}
