import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sign-in',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule ],
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  loginForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private authService: AuthService, private userService: UserService) {
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
          // Stocker les tokens dans le local storage
          localStorage.setItem('accessToken', tokens.Access_Token);
          localStorage.setItem('refreshToken', tokens.Refresh_Token);
          console.log('Connexion réussie, tokens reçus:', tokens);
          // Rediriger l'utilisateur ou gérer une autre action après la connexion
        },
        (error) => {
          console.error('Échec de la connexion', error);
        }
      );
    } else {
      console.warn('Le formulaire n\'est pas valide.');
    }
  }
}
