import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost/api/user';

  constructor(private http: HttpClient) { }

  signup(user: any): Observable<any> {
    return this.http.post(this.apiUrl + "/signup", user);
  }

  login(user: any): Observable<any> {
    return this.http.post(this.apiUrl + "/login", user, { observe: 'response' })
  }

  getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
      headers: new HttpHeaders().set('Authorization', `Bearer ${token}`),
    };
  }

  isTokenValid(token: string): boolean {
    try {
      const decodedToken: any = jwtDecode(token);
      const currentTime = Math.floor(Date.now() / 1000); // Current time in seconds
      return decodedToken.exp > currentTime; // Check if token has expired
    } catch (error) {
      console.error('Invalid token:', error);
      return false; // Invalid token
    }
  }
}
