package org.example.reservationservice.controller;

import org.example.reservationservice.Dto.ReservationDTO;
import org.example.reservationservice.entite.Reservation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.reservationservice.entite.StatutReservation;
import org.example.reservationservice.service.EmailNotificationService;
import org.example.reservationservice.service.ReservationService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private EmailNotificationService emailNotificationService;

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
    @GetMapping("")
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/reservations-by-client")
    public ResponseEntity<List<Reservation>> getReservationsByClient(@RequestHeader("X-User-Nom") String clientNom) {
        List<Reservation> reservations = reservationService.getReservationsByClient(clientNom);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody Map<String, Long> request) {
        Long reservationId = request.get("reservationId");
        Reservation reservation = reservationService.cancelReservation(reservationId);

        if (reservation != null) {
            emailNotificationService.sendCancellationNotification(reservation.getClient().getNom(), reservationId.toString());
            return ResponseEntity.ok("Réservation annulée et notification envoyée.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réservation non trouvée.");
        }
    }

}
