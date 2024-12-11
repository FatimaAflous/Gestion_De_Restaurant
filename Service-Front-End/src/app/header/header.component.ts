import { Component } from '@angular/core';
import {SignUpComponent} from '../sign-up/sign-up.component';
import { Router } from '@angular/router'; // Importer Router

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  constructor(private router: Router) {} // Injecter le Router ici

  navigateToSignUp() {
    this.router.navigate(['/sign-up']);
  }
  navigateToSignIn() {
    this.router.navigate(['/sign-in']);
  }
}
