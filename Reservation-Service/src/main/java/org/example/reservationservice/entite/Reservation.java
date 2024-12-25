package org.example.reservationservice.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec l'entité Client
    @ManyToOne
    @JoinColumn(name = "clientId")
    private Client client;

    // Relation avec l'entité Creneau
    @ManyToOne
    @JoinColumn(name = "creneauId")
    private Creneau creneau;

    // Relation avec l'entité Table
    @ManyToOne
    @JoinColumn(name = "tableId")
    private Table table;

    private int nombreDePersonnes;
    private LocalDateTime createdAt = LocalDateTime.now();
    private StatutReservation statut = StatutReservation.EN_ATTENTE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }


    public int getNombreDePersonnes() {
        return nombreDePersonnes;
    }

    public void setNombreDePersonnes(int nombreDePersonnes) {
        this.nombreDePersonnes = nombreDePersonnes;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
