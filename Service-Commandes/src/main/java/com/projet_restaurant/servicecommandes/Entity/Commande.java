package com.projet_restaurant.servicecommandes.Entity;

import com.projet_restaurant.servicecommandes.Dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false) // La colonne en base reste `client_id`
    private Long clientId; // Nom de la propriété en camelCas

    @Column(nullable = false)
    @NotBlank(message = "La date est obligatoire")
    private LocalDateTime date;

    @Column(nullable = false)
    @NotBlank(message = "Le statut est obligatoire")
    private String statut;

    @Column(nullable = false)
    @NotBlank(message = "Le prix total a payer est obligatoire")
    private Double total;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandPlat> plats = new ArrayList<>();
    // Getters
    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getStatut() {
        return statut;
    }

    public Double getTotal() {
        return total;
    }

    public List<CommandPlat> getPlats() {
        return plats;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setPlats(List<CommandPlat> plats) {
        this.plats = plats;
    }
}
