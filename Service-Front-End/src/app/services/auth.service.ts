import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable , catchError, throwError, tap  } from 'rxjs';
import { CookieService } from 'ngx-cookie-service'; // Importez un service pour gérer les cookies
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public isLoggedInSubject = new BehaviorSubject<boolean>(false); // Gère l'état de connexion
  public isLoggedIn$ = this.isLoggedInSubject.asObservable();

  username: string | null = null;
  private apiUrl = 'http://localhost:8082';

  constructor(private http: HttpClient ,  private cookieService: CookieService // Injectez le service pour gérer les cookies
  ) {}

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

  getTokens(): { accessToken: string | null; refreshToken: string | null } {
    const accessToken = this.cookieService.get('accessToken');
    const refreshToken = this.cookieService.get('refreshToken');

    return { accessToken, refreshToken };
  }
  // Optionnel: Méthode pour supprimer les tokens (déconnexion)
  logout() {
    // Supprimer les tokens des cookies
    this.cookieService.delete('accessToken', '/');
    this.cookieService.delete('refreshToken', '/');
     // Mettez à jour l'état de connexion
     this.isLoggedInSubject.next(false);
  }


  checkUserLoginStatus() {
    const tokens = this.getTokens();
    const isLoggedIn = !!tokens.accessToken; // Vérifie si un token d'accès existe
    this.isLoggedInSubject.next(isLoggedIn); // Met à jour l'état observable
  }
  decodeToken(token: string): any {
    try {
      // Split the token to get the payload
      const parts = token.split('.');
      if (parts.length !== 3) {
        throw new Error('Invalid token format');
      }

      // Decode the payload (second part)
      const payload = parts[1];
      const decodedPayload = JSON.parse(atob(payload.replace(/-/g, '+').replace(/_/g, '/')));
      return decodedPayload;
    } catch (error) {
      console.error('Erreur lors du décodage du token:', error);
      return null;
    }
  }

  getUsernameFromToken(): string | null {
    const tokens = this.getTokens();
    if (tokens.accessToken) {
      const decodedToken = this.decodeToken(tokens.accessToken);
      if (decodedToken && decodedToken.username) {
        return decodedToken.username;
      }
    }
    return null;
  }

}
