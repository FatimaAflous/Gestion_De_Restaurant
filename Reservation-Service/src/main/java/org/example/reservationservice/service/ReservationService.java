package org.example.reservationservice.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.reservationservice.Dto.ReservationDTO;
import org.example.reservationservice.controller.ReservationController;
import org.example.reservationservice.entite.*;
import org.example.reservationservice.repository.ClientRepository;
import org.example.reservationservice.repository.CreneauRepository;
import org.example.reservationservice.repository.ReservationRepository;
import org.example.reservationservice.repository.TableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CreneauRepository creneauRepository;

    @Autowired
    private TableRepository tableRepository;

    public Reservation createReservation(ReservationDTO reservationDTO) {
        // Vérifiez si le créneau existe
        Creneau creneau = creneauRepository.findById(reservationDTO.getCreneauId())
                .orElseThrow(() -> new RuntimeException("Créneau introuvable"));

        // Vérifiez si la table existe
        Table table = tableRepository.findById(reservationDTO.getTableId())
                .orElseThrow(() -> new RuntimeException("Table introuvable"));

        // Vérifiez si la table est déjà réservée pour ce créneau
        boolean tableReservee = reservationRepository.existsByCreneauAndTable(creneau, table);
        if (tableReservee) {
            throw new RuntimeException("La table est déjà réservée pour ce créneau");
        }
        // Trouver ou créer le client
        Client client = clientRepository.findByEmail(reservationDTO.getEmail())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setPrenom(reservationDTO.getPrenom());
                    newClient.setNom(reservationDTO.getNom());
                    newClient.setEmail(reservationDTO.getEmail());
                    newClient.setTelephone(reservationDTO.getTelephone());
                    return clientRepository.save(newClient);
                });

        // Créer la réservation
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setCreneau(creneau);
        reservation.setTable(table);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setStatut(StatutReservation.EN_ATTENTE);
        return reservationRepository.save(reservation);
    }


    // Méthode pour récupérer toutes les réservations avec les détails
    public List<Reservation> getAllReservations() {
        // Récupérer toutes les réservations avec leurs relations associées
        return reservationRepository.findAllWithDetails();
    }

    public List<Reservation> getReservationsByClient(String clientNom) {
        // Chercher le client avec le nom donné
        Client client = clientRepository.findByNom(clientNom)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        // Retourner les réservations associées à ce client
        return reservationRepository.findByClient(client);
    }

    public Reservation cancelReservation(Long reservationId) {
        // Récupérer la réservation par son ID
        return reservationRepository.findById(reservationId).map(reservation -> {
            // Changer le statut de la réservation
            reservation.setStatut(StatutReservation.ANNULEE);
            // Sauvegarder les modifications
            return reservationRepository.save(reservation);
        }).orElse(null); // Retourne null si la réservation n'est pas trouvée
    }
}



