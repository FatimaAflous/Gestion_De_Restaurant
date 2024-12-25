import { Component , OnInit } from '@angular/core';
import { CreneauService } from '../services/creneau.service';
import { FormsModule } from '@angular/forms';
import { Creneau } from '../models/creneau.model';
import { CommonModule } from '@angular/common';
import { ReservationService } from '../services/reservation.service';
@Component({
  selector: 'app-reservation',
  standalone: true,
  imports: [FormsModule , CommonModule],
  templateUrl: './reservation.component.html',
  styleUrl: './reservation.component.css',
})
export class ReservationComponent  implements OnInit{
  creneaux: any[] = [];
  tables: any[] = [];
  selectedCreneau: string | null = null;
  selectedTable: string | null = null;

  prenom: string = '';
  nom: string = '';
  ville: string = '';
  pays: string = '';
  telephone: string = '';
  email: string = '';
  confirmEmail: string = '';

  constructor(private creneauService: CreneauService ,private reservationService:ReservationService ) {}

  ngOnInit(): void {
    this.loadCreneaux();
    this.loadTables();
  }

  loadCreneaux(): void {
    this.creneauService.getAllCreneaux().subscribe({
      next: (data) => (this.creneaux = data),
      error: (err) => console.error('Erreur lors de la récupération des créneaux:', err),
    });
  }

  loadTables(): void {
    this.creneauService.getAllTables().subscribe({
      next: (data) => (this.tables = data),
      error: (err) => console.error('Erreur lors de la récupération des tables:', err),
    });
  }

  onSubmit() {
  if (this.email !== this.confirmEmail) {
    alert('Les emails ne correspondent pas');
    return;
  }

  const reservationData = {
    prenom: this.prenom,
    nom: this.nom,
    ville: this.ville,
    pays: this.pays,
    telephone: this.telephone,
    email: this.email,
    creneauId: this.selectedCreneau,  // Utiliser directement les ID
    tableId: this.selectedTable       // Utiliser directement les ID
  };

  this.reservationService.createReservation(reservationData).subscribe(
    response => {
      alert('Réservation réussie');
      console.log(response);
    },
    error => {
      alert('Une erreur est survenue');
      console.error(error);
    }
  );
}

}
