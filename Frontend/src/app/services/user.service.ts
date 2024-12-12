import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost/api/user';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getUserPartner(username: String): Observable<any> {
    const payload = { username: username };
    const headers = this.authService.getAuthHeaders().headers;
    return this.http.post(this.apiUrl + "/partner", payload, { headers, observe: 'response' } )
  }

  addPartner(username_2: String): Observable<any> {
    const username_1 = localStorage.getItem('username');
    const payload = { username_1: username_1,  username_2: username_2 };
    const headers = this.authService.getAuthHeaders().headers;
    return this.http.post(this.apiUrl + "/partner/add", payload, { headers, observe: 'response' } )
  }
}
