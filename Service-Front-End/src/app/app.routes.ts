import { Routes } from '@angular/router';
import { SignUpComponent } from './sign-up/sign-up.component';
import { MainComponent} from './main/main.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ClientDashboardComponent } from './client-dashboard/client-dashboard.component';
import { AuthGuard } from './services/auth-guard.service'; // Import du garde
import { MenuComponent } from './menu/menu.component';
import { GestionMenuComponent } from './gestion-menu/gestion-menu.component';
import { AcceuilAdminDashboardComponent } from './acceuil-admin-dashboard/acceuil-admin-dashboard.component';
import { GestionCommandesComponent } from './gestion-commandes/gestion-commandes.component';
import { AcceuilClientDashboardComponent } from './acceuil-client-dashboard/acceuil-client-dashboard.component';
import { CartComponent } from './cart/cart.component';
import { PaymentComponent } from './payment/payment.component';
import { HistoriqueCommandesComponent } from './historique-commandes/historique-commandes.component';
import { ReservationComponent } from './reservation/reservation.component';
import { GestionCreneauComponent } from './gestion-creneau/gestion-creneau.component';
export const routes: Routes = [
  { path: 'home', component: MainComponent } ,// Route pour l'inscription
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'sign-up', component: SignUpComponent }, // Route pour l'inscription
  { path: 'sign-in', component: SignInComponent }, // Route pour l'inscription
  { path: 'menu', component: MenuComponent }, // Route pour l'inscription
  { path: 'cart', component: CartComponent },  // Ajouter une route pour le panier
  { path:  'payment', component: PaymentComponent},
  { path: 'reservation',component:ReservationComponent},
  //ADMIN

  { path: 'admin-dashboard', component: AdminDashboardComponent,canActivate: [AuthGuard],data: { roles: ['ADMIN'] } , children: [
    { path: 'acceuil', component: AcceuilAdminDashboardComponent},
    { path: '', redirectTo: 'acceuil', pathMatch: 'full' },
    { path: 'gestion-menu', component: GestionMenuComponent},
    { path: 'gestion-commandes', component: GestionCommandesComponent},
    { path: 'gestion-creneaux', component: GestionCreneauComponent},


  //CLIENT

  ]},
  { path: 'client-dashboard', component: ClientDashboardComponent,canActivate: [AuthGuard],data: { roles: ['CLIENT'] } , children: [
    { path: 'acceuil', component: AcceuilClientDashboardComponent},
    { path: '', redirectTo: 'acceuil', pathMatch: 'full' },
    { path: 'historique-commandes', component: HistoriqueCommandesComponent},

  ]},


];
