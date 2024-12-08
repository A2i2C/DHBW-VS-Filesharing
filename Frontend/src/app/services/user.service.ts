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

  getUserPartner(userName: String): Observable<any> {
    const payload = { username: userName };
    return this.http.post(this.apiUrl + "/partner", payload, this.authService.getAuthHeaders() )
  }
}
