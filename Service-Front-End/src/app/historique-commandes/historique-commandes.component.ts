import { Component, OnInit } from '@angular/core';
import { CommandeService } from '../services/commande.service';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-historique-commandes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './historique-commandes.component.html',
  styleUrl: './historique-commandes.component.css'
})
export class HistoriqueCommandesComponent implements OnInit {
 orders: any[] = [];

  constructor(private commandeService: CommandeService) {}

  ngOnInit(): void {
    this.fetchOrders();
  }
  fetchOrders(): void {
    this.commandeService.getCustomerOrders().subscribe(
      (data) => {
        console.log('Commandes récupérées:', data);
        this.orders = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des commandes:', error);
      }
    );
  }
}
