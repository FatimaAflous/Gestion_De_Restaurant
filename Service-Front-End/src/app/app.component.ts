import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {HeaderComponent} from './header/header.component';
import {MainComponent} from './main/main.component';
import {FooterComponent} from "./footer/footer.component";
import {SignUpComponent} from "./sign-up/sign-up.component";
import { ReactiveFormsModule, FormsModule } from '@angular/forms'; // Importation de ReactiveFormsModule et FormsModule

@Component({
  selector: 'app-root',
  standalone: true,
    imports: [RouterOutlet, HeaderComponent, MainComponent, FooterComponent, SignUpComponent ,ReactiveFormsModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Service-Front-End';
}
