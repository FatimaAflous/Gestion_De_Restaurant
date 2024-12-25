package org.example.reservationservice.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@jakarta.persistence.Table(name = "table_reservation") // Remplacer "ma_table" par un nom valide
public class Table {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom; // Nom ou numéro de la table
    private int capacite; // Nombre de personnes que la table peut accueillir

    // Relation avec l'entité Creneau
    @ManyToOne(fetch = FetchType.EAGER) // Par défaut, cela charge le créneau associé.
    @JoinColumn(name = "creneauId")
    private Creneau creneau;

    // Relation avec l'entité Reservation
    @OneToMany(mappedBy = "table")
    @JsonIgnore
    private List<Reservation> reservations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
