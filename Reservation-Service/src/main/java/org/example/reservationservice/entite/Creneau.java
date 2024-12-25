package org.example.reservationservice.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Creneau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime debut; // Heure de début du créneau
    private LocalDateTime fin;   // Heure de fin du créneau
    @OneToMany(mappedBy = "creneau")
    @JsonIgnore  // Ignorer la sérialisation de cette relation
    private List<Table> tablesDisponibles;

    // Relation avec l'entité Reservation pour indiquer les réservations faites pour ce créneau
    @OneToMany(mappedBy = "creneau")
    @JsonIgnore
    private List<Reservation> reservations;

    // Relation avec l'entité Table pour indiquer les tables disponibles pour ce créneau

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDebut() {
        return debut;
    }

    public void setDebut(LocalDateTime debut) {
        this.debut = debut;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Table> getTablesDisponibles() {
        return tablesDisponibles;
    }

    public void setTablesDisponibles(List<Table> tablesDisponibles) {
        this.tablesDisponibles = tablesDisponibles;
    }
}
