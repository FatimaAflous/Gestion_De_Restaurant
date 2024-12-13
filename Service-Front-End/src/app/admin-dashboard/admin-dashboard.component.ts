import { Component , OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent implements OnInit {
  isUserLoggedIn: boolean = false;
  username: string | null = null;
  constructor(private authService: AuthService, private router: Router) {
    Chart.register(...registerables);
  }

  ngOnInit() {
    this.createRevenueChart();
    this.createPerformanceChart();
     // Abonnez-vous à l'état utilisateur
     this.authService.isLoggedIn$.subscribe(isLoggedIn => {

      this.isUserLoggedIn = isLoggedIn;

      if (isLoggedIn) {
        // Récupérez le nom d'utilisateur s'il est connecté
       // this.username = this.authService.getUsernameFromToken();

      } else {
        this.username = null;
      }
    });

    // Vérifiez l'état initial lors du chargement
   // this.authService.checkUserLoginStatus();
  }
  logout() {
    this.authService.logout();
    this.isUserLoggedIn = false;
    this.username = null;
    this.router.navigate(['/home']);
  }

  createRevenueChart() {
    new Chart('revenueChart', {
      type: 'line',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [
          {
            label: 'Revenue',
            data: [10, 20, 30, 40, 50, 60],
            borderColor: 'blue',
            borderWidth: 2,
            fill: false,
          },
        ],
      },
    });
  }

  createPerformanceChart() {
    new Chart('performanceChart', {
      type: 'bar',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        datasets: [
          {
            label: 'Performance',
            data: [15, 25, 35, 45, 55, 65],
            backgroundColor: 'green',
          },
        ],
      },
    });
  }

}
