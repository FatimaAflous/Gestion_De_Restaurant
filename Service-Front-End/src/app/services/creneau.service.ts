import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators'; // Import correct pour `tap` et `catchError`
import { Creneau } from '../models/creneau.model';
import { Table } from '../models/table.model';

@Injectable({
  providedIn: 'root'
})
export class CreneauService {

  private apiUrl = 'http://localhost:8091/api/v1/creneaux'; // URL de ton backend
  private api1Url = 'http://localhost:8091/api/v1/tables'; // URL de ton backend

  constructor(private http: HttpClient) {}

  // Méthode pour créer un créneau avec début et fin
  creerCreneau(debut: string, fin: string): Observable<string> {
    console.log('Débogage: Appel de creerCreneau avec debut =', debut, 'fin =', fin);

    // Envoi des deux paramètres début et fin dans la requête POST
    return this.http.post(`${this.apiUrl}/create`, null, {
      params: { debut, fin },
      responseType: 'text'
    }).pipe(
      tap(response => console.log('Débogage: Réponse reçue du serveur =', response)),
      catchError(error => {
        console.error('Erreur détectée lors de l’appel à creerCreneau :', error);
        return throwError(() => error);
      })
    );
  }

  // Récupérer tous les créneaux
// Récupérer tous les créneaux
getAllCreneaux(): Observable<Creneau[]> {
  console.log('Envoi de la requête pour récupérer les créneaux...');
  console.log('URL de l\'API:', `${this.apiUrl}`);

  return this.http.get<Creneau[]>(`${this.apiUrl}`).pipe(
    tap({
      next: (data) => {
        console.log('Réponse de l\'API reçue :', data);  // Log les données reçues
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des créneaux:', err);  // Log les erreurs
      },
      complete: () => {
        console.log('Requête pour récupérer les créneaux terminée.');  // Log de la fin de la requête
      },
    })
  );
}

  createTable(nom: string, capacite: number, creneauId: number): Observable<Table> {
    // Affichage des données envoyées à l'API pour débogage
    console.log('Données envoyées à l\'API:', { nom, capacite, creneauId });

    return this.http.post<Table>(`${this.api1Url}/create`, null, {
      params: { nom, capacite: capacite.toString(), creneauId: creneauId.toString() }
    }).pipe(
      tap(response => console.log('Table créée avec succès :', response)),
      catchError(error => {
        console.error('Erreur lors de la création de la table :', error);
        return throwError(() => error);
      })
    );
  }

  getAllTables(): Observable<Table[]> {
    return this.http.get<Table[]>(`${this.api1Url}`).pipe(
      tap(tables => console.log('Tables récupérées :', tables)),
      catchError(error => {
        console.error('Erreur lors de la récupération des tables :', error);
        return throwError(() => error);
      })
    );
  }


  getTablesForCreneau(creneauId: string): Observable<any> {
    return this.http.get(`/api/tables/disponibles/${creneauId}`);
  }

}
