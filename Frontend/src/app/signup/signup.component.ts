import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../services/auth.service';
import { Router } from '@angular/router';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatFormField} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatCard,
    MatCardContent,
    MatCardTitle,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
  ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  protected errorMessage: string = '';

  protected user: FormGroup = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(8),
      Validators.pattern('^[A-Za-z][A-Za-z0-9]*$') // Needs to begin with a letter
    ]),
    password: new FormControl('',
      [
        Validators.required,
        Validators.minLength(3)
      ])
  });

  constructor(private authService: AuthService, private router: Router) { }

  signup() {
    if (this.user.invalid) {
      console.error('Form is invalid');
      return;
    }

    this.authService.signup(this.user.value).subscribe({
      next: () => {
        // Redirect to login after successful signup
        this.router.navigate(['/login'], {state: { message: 'Signup successful! Please log in.' }});
      },
      error: (err) => {
        console.error('Error during signup:', err);
        this.errorMessage = 'Signup failed. Please try again.';
      }
    });
  }
}
