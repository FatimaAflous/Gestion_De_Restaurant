import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable  , tap , catchError, throwError} from 'rxjs';
import { Reservation } from '../models/reservation.model';
@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private apiUrl = 'http://localhost:8091/api/v1/reservations';  // L'URL de votre API backend

  constructor(private http: HttpClient) { }

  createReservation(reservationData: any): Observable<any> {
    // Log avant l'envoi de la requête
    console.log('Données de la réservation envoyées :', reservationData);

    return this.http.post<any>(`${this.apiUrl}/create`, reservationData).pipe(
      tap(response => {
        // Log de la réponse de l'API
        console.log('Réponse de l\'API :', response);
      }),
      catchError(error => {
        // Log de l'erreur
        console.error('Erreur lors de la réservation :', error);
        return throwError(error); // Rejette l'erreur pour être géré plus loin
      })
    );
  }

}
