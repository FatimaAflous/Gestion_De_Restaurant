import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from "./footer/footer.component";
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';
//import { WebSocketServiceService } from './services/web-socket-service.service';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent, ReactiveFormsModule ,CommonModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Service-Front-End';

  constructor(private router: Router, private authService:AuthService /*,private webSocketService: WebSocketServiceService*/) {}

  shouldDisplayHeader(): boolean {
    return !this.router.url.includes('/admin-dashboard') && !this.router.url.includes('/client-dashboard');
  }

  shouldDisplayFooter(): boolean {
    return !this.router.url.includes('/admin-dashboard') && !this.router.url.includes('/client-dashboard');
  }

  ngOnInit() {
    //this.webSocketService.connect();
    //this.webSocketService.connect(); // Connexion WebSocket au démarrage
  }
}
