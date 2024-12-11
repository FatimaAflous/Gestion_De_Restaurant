import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8082/api/v1/users'; // URL de backend

  constructor(private http: HttpClient) { }
  createUser(user: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, user);
  }
}
