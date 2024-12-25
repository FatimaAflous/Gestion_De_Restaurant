import { Component, OnInit } from '@angular/core';
import { CommandeService } from '../services/commande.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Importez FormsModule

@Component({
  selector: 'app-gestion-commandes',
  standalone: true,
  imports: [CommonModule ,FormsModule],
  templateUrl: './gestion-commandes.component.html',
  styleUrl: './gestion-commandes.component.css'
})
export class GestionCommandesComponent implements OnInit {
  orders: any[] = [];

  constructor(private commandService: CommandeService) {}
  selectedOrder: any = null;

  ngOnInit(): void {
    this.commandService.getAllOrders().subscribe((data) => {
      this.orders = data;
    });
  }

// Mettre à jour le statut
changer_status() {

}

}
