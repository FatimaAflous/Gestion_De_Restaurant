import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { CommonModule } from '@angular/common';
import { CookieService } from 'ngx-cookie-service'; // Importez un service pour gérer les cookies
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule ],
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  loginForm: FormGroup;
  passwordVisible: boolean = false; // initialisation de la propriété

  private isLoggedInSubject = new BehaviorSubject<boolean>(false); // Gère l'état de connexion
  isLoggedIn$ = this.isLoggedInSubject.asObservable(); // Observable pour les composants

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private cookieService: CookieService // Injectez le service pour gérer les cookies
  ) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { email, password } = this.loginForm.value;

      this.authService.login(email, password).subscribe(
        (tokens) => {
          // Les options de cookie sans utilisation de CookieOptions
          const cookieOptions = {
            secure: true,
            httpOnly: true,
            path: '/',

          };

          this.cookieService.set('accessToken', tokens.Access_Token, cookieOptions);
          this.cookieService.set('refreshToken', tokens.Refresh_Token, cookieOptions);
          console.log('Connexion réussie, tokens reçus:', tokens);
           // Mettez à jour l'état de connexion
 // Mettre à jour l'état de connexion dans le service
 this.authService.isLoggedInSubject.next(true);          // Rediriger l'utilisateur ou gérer une autre action après la connexion
        },
        (error) => {
          console.error('Échec de la connexion', error);
        }
      );
    } else {
      console.warn('Le formulaire n\'est pas valide.');
    }
  }

  togglePassword(): void {
    this.passwordVisible = !this.passwordVisible; // inverser la visibilité du mot de passe
  }
}
