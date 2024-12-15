package com.projet_restaurant.servicecommandes.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommandPlat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_plat", nullable = false)
    @NotNull(message = "L'identifiant du plat est obligatoire")
    private Long menuId;

    @NotNull(message = "La quantité est obligatoire")
    private Integer quantite;

    @NotNull(message = "Le prix est obligatoire")
    private Double prix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    @JsonIgnore
    private Commande commande;

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public Double getPrix() {
        return prix;
    }

    public Commande getCommande() {
        return commande;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

}
