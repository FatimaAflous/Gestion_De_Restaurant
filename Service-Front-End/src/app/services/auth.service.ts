import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable , catchError, throwError, tap  } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8082';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    const payload = { email, password }; // Données à envoyer
    console.log('Payload envoyé au backend:', payload); // Log des données
    return this.http.post(`${this.apiUrl}/login`, payload).pipe(
      tap(response => {
        console.log('Login successful, response:', response);
      }),
      catchError(error => {
        console.error('Login failed, error:', error);
        return throwError(error);
      })
    );
  }

  refreshToken(): Observable<any> {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post(`${this.apiUrl}/refresh`, { refreshToken });
  }

  // Optionnel: Méthode pour récupérer l'authentification actuelle (tokens)
  getTokens() {
    return {
      accessToken: localStorage.getItem('accessToken'),
      refreshToken: localStorage.getItem('refreshToken')
    };
  }

  // Optionnel: Méthode pour supprimer les tokens (déconnexion)
  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }
}
