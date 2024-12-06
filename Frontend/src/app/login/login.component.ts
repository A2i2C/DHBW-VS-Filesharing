import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatFormField} from '@angular/material/form-field';
import {MatButton} from '@angular/material/button';
import {MatInput} from '@angular/material/input';
import {NavbarComponent} from '../navbar/navbar.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatCard,
    MatCardTitle,
    MatCardContent,
    ReactiveFormsModule,
    MatFormField,
    MatButton,
    MatInput,
    NavbarComponent
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  user: FormGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl('')
  });

  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) { }

  login() {
    this.authService.login(this.user.value).subscribe({
      next: (response) => {
        console.log('Login successful:', response);
        const responseBody = response.body;
        if (responseBody && responseBody.token) {
          localStorage.setItem('username', responseBody.username);
          localStorage.setItem('token', responseBody.token);
          this.router.navigate(['/home']);
        } else {
          console.error('Invalid response structure');
          this.errorMessage = 'Login failed. Invalid response from the server.';
        }
      },
      error: (error) => {
        console.error('Login failed. Status:', error.status);
        this.errorMessage = 'Login failed. Please check your credentials.';
      }
    });
  }
}
