import { Routes } from '@angular/router';
import { SignUpComponent } from './sign-up/sign-up.component';
import {MainComponent} from './main/main.component';

export const routes: Routes = [
  { path: 'home', component: MainComponent } ,// Route pour l'inscription
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'sign-up', component: SignUpComponent } // Route pour l'inscription

];
