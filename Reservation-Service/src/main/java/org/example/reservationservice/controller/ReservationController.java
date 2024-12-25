package org.example.reservationservice.controller;

import org.example.reservationservice.Dto.ReservationDTO;
import org.example.reservationservice.entite.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.reservationservice.entite.StatutReservation;
import org.example.reservationservice.service.ReservationService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

import java.util.List;



@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDTO reservationDTO) {
        System.out.println("Démarrage de la création de réservation...");
        System.out.println("Données reçues : " + reservationDTO);

        try {
            // Log avant de tenter de créer la réservation
            System.out.println("Tentative de création de la réservation pour le client : " + reservationDTO.getPrenom() + " " + reservationDTO.getNom());

            Reservation reservation = reservationService.createReservation(reservationDTO);

            // Log après la création de la réservation
            System.out.println("Réservation créée avec succès : " + reservation);

            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (RuntimeException e) {
            // Log en cas d'erreur
            System.out.println("Erreur lors de la création de la réservation : " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
