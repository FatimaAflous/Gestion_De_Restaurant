import { Component, OnInit } from '@angular/core';
import { CreneauService } from '../services/creneau.service';
import { Creneau } from '../models/creneau.model';
import { CommonModule } from '@angular/common';  // Importer CommonModule ici
import { FormsModule } from '@angular/forms';
import { Table } from '../models/table.model';
@Component({
  selector: 'app-gestion-creneau',
  standalone: true,
  imports: [CommonModule, FormsModule],  // Ajouter CommonModule et FormsModule ici
  templateUrl: './gestion-creneau.component.html',
  styleUrls: ['./gestion-creneau.component.css']
})

export class GestionCreneauComponent implements OnInit {
  creneaux: Creneau[] = [];
  dateDebut: string | null = null;
  dateFin: string | null = null;
  tableNom: string = '';
  tableCapacite: number | null = null;
  isAddingCreneau: boolean = false;  // Initialisé à false pour afficher d'abord la liste
  tables: Table[] = [];
  selectedCreneauId: number = 0;

  constructor(
    private creneauService: CreneauService,
  ) {}

  ngOnInit(): void {
    this.loadCreneaux();
    this.creneauService.getAllTables().subscribe({
      next: (tables) => {
        this.tables = tables;
        console.log('Tables récupérées :', this.tables);
      },
      error: (err) => console.error('Erreur :', err),
    }); // Charger les créneaux au début
  }

  // Méthode pour charger la liste des créneaux
  loadCreneaux(): void {
    this.creneauService.getAllCreneaux().subscribe({
      next: (data) => (this.creneaux = data),
      error: (err) => console.error('Erreur lors de la récupération des créneaux:', err),
    });
  }

  // Méthode pour basculer entre le formulaire et la liste des créneaux
  toggleForm(): void {
    this.isAddingCreneau = !this.isAddingCreneau;
    if (!this.isAddingCreneau) {
      this.loadCreneaux(); // Charger les créneaux quand on revient sur la liste
    }
  }

  // Méthode pour créer un créneau
  onSubmitCreneau(): void {
    if (this.dateDebut && this.dateFin) {
      const formattedDebut = new Date(this.dateDebut).toISOString().slice(0, 19);
      const formattedFin = new Date(this.dateFin).toISOString().slice(0, 19);

      this.creneauService.creerCreneau(formattedDebut, formattedFin).subscribe({
        next: (creneau) => {
          alert('Créneau ajouté avec succès');
          this.loadCreneaux();  // Recharger les créneaux après ajout
          // Réinitialiser le formulaire de création de créneau
          this.dateDebut = null;
          this.dateFin = null;
        },
        error: (err) => {
          console.error('Erreur lors de la création du créneau:', err);
        }
      });
    } else {
      alert('Veuillez sélectionner une date et une heure de début et de fin.');
    }
  }

  // Méthode pour soumettre la création de la table
  onSubmitTable(): void {
    if (this.selectedCreneauId && this.tableNom && this.tableCapacite) {
      // Appel à la méthode de création de la table en envoyant les données
      this.creneauService.createTable(this.tableNom, this.tableCapacite, this.selectedCreneauId).subscribe({
        next: (response) => {
          alert('Table créée avec succès');
          // Réinitialiser le formulaire ou effectuer d'autres actions
        },
        error: (err) => {
          console.error('Erreur lors de la création de la table:', err);
        }
      });
    } else {
      alert('Veuillez remplir tous les champs, y compris la sélection du créneau.');
    }
  }
  }
