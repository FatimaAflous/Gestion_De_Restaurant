import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const rolesAllowed = route.data['roles'] as Array<string>;
    const isLoggedIn = this.authService.isUserLoggedIn();
    const userRole = this.authService.getUserRoleFromToken();

    console.log('isLoggedIn:', isLoggedIn);
    console.log('userRole:', userRole);
    console.log('rolesAllowed:', rolesAllowed);

    if (isLoggedIn && userRole && rolesAllowed.includes(userRole)) {
      return true;
    }

    // Affichez un message d'alerte
    alert('Vous n\'êtes pas autorisé à accéder à cette page.');

    // Redirigez l'utilisateur non autorisé
    this.router.navigate(['/home']);
    return false;
  }
}
