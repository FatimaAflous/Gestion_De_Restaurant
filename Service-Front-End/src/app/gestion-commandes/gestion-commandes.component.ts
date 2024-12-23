import { Component, OnInit } from '@angular/core';
import { CommandeService } from '../services/commande.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-gestion-commandes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gestion-commandes.component.html',
  styleUrl: './gestion-commandes.component.css'
})
export class GestionCommandesComponent implements OnInit {
  orders: any[] = [];

  constructor(private commandService: CommandeService) {}

  ngOnInit(): void {
    this.commandService.getAllOrders().subscribe((data) => {
      this.orders = data;
    });
  }
}
