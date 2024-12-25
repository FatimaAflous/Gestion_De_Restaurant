package org.example.reservationservice.service;

import org.example.reservationservice.entite.Creneau;
import org.example.reservationservice.repository.CreneauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CreneauService {
    @Autowired
    private CreneauRepository creneauRepository;

    public Creneau creerCreneau(LocalDateTime debut, LocalDateTime fin) {
        // Vérification que la date de fin est après la date de début
        if (fin.isBefore(debut)) {
            throw new IllegalArgumentException("L'heure de fin doit être après l'heure de début.");
        }
        Creneau creneau = new Creneau();
        creneau.setDebut(debut);
        creneau.setFin(fin);
        // Sauvegarde dans la base de données
        return creneauRepository.save(creneau);

    }

    // Méthode pour récupérer tous les créneaux
    public List<Creneau> getAllCreneaux() {
        return creneauRepository.findAll();
    }
}
