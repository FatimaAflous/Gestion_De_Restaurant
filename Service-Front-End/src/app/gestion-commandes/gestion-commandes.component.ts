import { Component, OnInit } from '@angular/core';
import { CommandeService } from '../services/commande.service';
import { CommonModule } from '@angular/common';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-gestion-commandes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gestion-commandes.component.html',
  styleUrl: './gestion-commandes.component.css'
})
export class GestionCommandesComponent implements OnInit {
  orders: any[] = [];

  constructor(private commandService: CommandeService , private modalService: NgbModal) {}
  selectedOrder: any = null;

  ngOnInit(): void {
    this.commandService.getAllOrders().subscribe((data) => {
      this.orders = data;
    });
  }
// Ouvrir la modale
openStatusModal(order: any) {
  this.selectedOrder = { ...order }; // Copie de la commande pour modification
  this.modalService.open('#statusModal');
}

// Mettre à jour le statut
updateOrderStatus() {
  const index = this.orders.findIndex(
    (order) => order.id === this.selectedOrder.id
  );
  if (index !== -1) {
    this.orders[index].status = this.selectedOrder.status;
  }
  // Envoyer la mise à jour au backend
  // Exemple :
  // this.http.put('/api/orders/' + this.selectedOrder.id, this.selectedOrder).subscribe();

  this.modalService.dismissAll();
}

}
