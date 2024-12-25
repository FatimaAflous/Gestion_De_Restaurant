package org.example.reservationservice.service;

import org.example.reservationservice.entite.Creneau;
import org.example.reservationservice.entite.Reservation;
import org.example.reservationservice.entite.Table;
import org.example.reservationservice.repository.CreneauRepository;
import org.example.reservationservice.repository.ReservationRepository;
import org.example.reservationservice.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    @Autowired
    private CreneauRepository creneauRepository;  // Repository pour Creneau

    @Autowired
    private TableRepository tableRepository;      // Repository pour Table
@Autowired
private ReservationRepository reservationRepository;
    // Récupérer tous les créneaux

    public TableService(TableRepository tableRepository, ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }
    public List<Creneau> getAllCreneaux() {
        return creneauRepository.findAll();
    }

    // Créer une table et l'associer à un créneau
    public Table createTable(String nom, int capacite, Long creneauId) {
        Creneau creneau = creneauRepository.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Creneau non trouvé"));

        Table table = new Table();
        table.setNom(nom);
        table.setCapacite(capacite);
        table.setCreneau(creneau);  // Associer la table au créneau
        return tableRepository.save(table);
    }

    public List<Table> getTablesForCreneau(Long creneauId) {
        // Récupérer toutes les tables
        List<Table> toutesLesTables = tableRepository.findAll();

        // Récupérer les tables déjà réservées pour ce créneau
        List<Table> tablesReservees = reservationRepository.findByCreneauId(creneauId).stream()
                .map(Reservation::getTable)
                .collect(Collectors.toList());

        // Filtrer les tables disponibles
        return toutesLesTables.stream()
                .filter(table -> !tablesReservees.contains(table))
                .collect(Collectors.toList());
    }
}
