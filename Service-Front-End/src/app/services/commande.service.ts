import { Injectable } from '@angular/core';
import { HttpClient , HttpHeaders} from '@angular/common/http';
import { switchMap, tap, catchError } from 'rxjs/operators';
import { Observable, throwError, map } from 'rxjs';
import { Route, Router } from '@angular/router';
import { Order } from '../models/order.model';
import { AuthService } from './auth.service';
@Injectable({
  providedIn: 'root'
})
export class CommandeService {

  constructor(private http: HttpClient , private router:Router , private authService:AuthService) { }
  private apiUrl = 'http://localhost:8084/api/orders';
  private payUrl = 'http://localhost:5000';

  createOrder(orderData: any): void {
    console.log('Envoi de la commande au backend pour création', orderData);

    // Récupérer userId depuis orderData
    const userId = orderData.userId;

    // Préparer l'objet orderRequest à envoyer dans le corps de la requête
    const orderRequest = {
      items: orderData.items,
      price: orderData.price  // Si nécessaire, vous pouvez ajouter d'autres champs ici
    };

    // Envoyer la requête avec userId en paramètre et orderRequest dans le corps
    this.http.post<Order>(`${this.apiUrl}/create?userId=${userId}`, orderRequest)
      .subscribe(
        (order: Order) => {
          console.log('Commande créée avec succès:', order);

      //ici
  // Envoi de la commande à SERVICE-PAIEMENT pour obtenir le lien Stripe
  this.http.post<{ checkout_url: string }>(`${this.payUrl}/payment/create-checkout-session`, { commande_id: order.id })
  .subscribe(
    (paymentResponse) => {
      // Rediriger l'utilisateur vers le lien Stripe pour payer
      window.location.href = paymentResponse.checkout_url;
    },
    (error) => {
      console.error('Erreur lors de la récupération du lien de paiement:', error);
    }
  );
      //ici

        },
        error => {
          console.error('Erreur lors de la création de la commande:', error);
          alert('Une erreur est survenue lors de la création de la commande.');
        }
      );
  }

  getAllOrders() {
    return this.http.get<Order[]>(`${this.apiUrl}`).pipe(  // Utilisation du type 'Order[]'
      tap(orders => {
        // Vérifie si l'image Base64 est présente pour chaque commande et ses items
        orders.forEach(order => {
          console.log("Détails de la commande " + order.id);

          // Parcours des items de la commande pour récupérer l'image Base64
          order.items.forEach(item => {  // item a le type implicite 'any' grâce à 'Order'
            console.log("voici contenu de l'image pour l'item " + item.id, item.imageBase64);  // Débogage
          });
        });
      })
    );
  }

  getCustomerOrders(): Observable<any> {
    console.log('Début de la récupération des commandes clients');

    return this.authService.getCurrentUser().pipe(
      switchMap((user) => {
        const userId = user.id;
        console.log('ID de l\'utilisateur connecté:', userId);

        // Envoyer l'ID dans l'en-tête HTTP
        const headers = new HttpHeaders().set('X-User-ID', userId.toString());
        return this.http.get(`${this.apiUrl}/customer`, { headers });
      })
    );
  }

}
