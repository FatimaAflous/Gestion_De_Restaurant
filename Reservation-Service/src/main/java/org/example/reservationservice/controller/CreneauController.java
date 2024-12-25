package org.example.reservationservice.controller;

import org.example.reservationservice.entite.Creneau;
import org.example.reservationservice.repository.CreneauRepository;
import org.example.reservationservice.service.CreneauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/creneaux")
public class CreneauController {

    @Autowired
    private CreneauService creneauService;
    @Autowired
    private CreneauRepository creneauRepository;

    // Créer un créneau
    @PostMapping("/create")
    public ResponseEntity<Creneau> creerCreneau(@RequestParam("debut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
                                                @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        try {
            Creneau creneau = creneauService.creerCreneau(debut, fin);
            return new ResponseEntity<>(creneau, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Méthode pour récupérer tous les créneaux
    @GetMapping
    public ResponseEntity<List<Creneau>> getAllCreneaux() {
        List<Creneau> creneaux = creneauService.getAllCreneaux();
        return ResponseEntity.ok(creneaux);
    }
}
