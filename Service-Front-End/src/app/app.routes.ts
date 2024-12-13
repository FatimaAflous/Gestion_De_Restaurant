import { Routes } from '@angular/router';
import { SignUpComponent } from './sign-up/sign-up.component';
import {MainComponent} from './main/main.component';
import { SignInComponent } from './sign-in/sign-in.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { ClientDashboardComponent } from './client-dashboard/client-dashboard.component';
import { AuthGuard } from './services/auth-guard.service'; // Import du garde

export const routes: Routes = [
  { path: 'home', component: MainComponent } ,// Route pour l'inscription
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'sign-up', component: SignUpComponent }, // Route pour l'inscription
  { path: 'sign-in', component: SignInComponent }, // Route pour l'inscription
  { path: 'admin-dashboard', component:AdminDashboardComponent , canActivate: [AuthGuard], data: { roles: ['ADMIN'] }  },// Route pour l'inscription
  { path: 'client-dashboard', component:ClientDashboardComponent , canActivate: [AuthGuard], data: { roles: ['CLIENT'] } } // Route pour l'inscription

];
