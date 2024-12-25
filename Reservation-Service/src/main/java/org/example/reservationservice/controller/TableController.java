package org.example.reservationservice.controller;

import org.example.reservationservice.entite.Creneau;
import org.example.reservationservice.entite.Table;
import org.example.reservationservice.repository.CreneauRepository;
import org.example.reservationservice.repository.TableRepository;
import org.example.reservationservice.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
public class TableController {

    @Autowired
    private TableService tableService;
    @Autowired
    private CreneauRepository creneauRepository;
    @Autowired
    private TableRepository tableRepository;
    // Afficher tous les créneaux existants
    @GetMapping("/creneaux")
    public List<Creneau> getAllCreneaux() {
        return tableService.getAllCreneaux();
    }

    // Créer une table et l'associer à un créneau
    @PostMapping("/create")
    public ResponseEntity<Table> createTable(
            @RequestParam String nom,
            @RequestParam int capacite,
            @RequestParam Long creneauId) {

        // Affichage des données reçues dans le serveur
        System.out.println("Données reçues - nom: " + nom + ", capacite: " + capacite + ", creneauId: " + creneauId);

        Creneau creneau = creneauRepository.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Creneau non trouvé"));

        Table newTable = new Table();
        newTable.setNom(nom);
        newTable.setCapacite(capacite);
        newTable.setCreneau(creneau);  // Associer la table au créneau
        return ResponseEntity.status(HttpStatus.CREATED).body(tableRepository.save(newTable));
    }
    @GetMapping("")
    public ResponseEntity<List<Table>> getAllTables() {
        List<Table> tables = tableRepository.findAll();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/disponibles/{creneauId}")
    public ResponseEntity<List<Table>> getTablesForCreneau(@PathVariable Long creneauId) {
        List<Table> tablesDisponibles = tableService.getTablesForCreneau(creneauId);
        return ResponseEntity.ok(tablesDisponibles);
    }
}
