import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth.service';

export const publicGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService); // Inject AuthService
  const router = inject(Router); // Inject Router

  const token = localStorage.getItem('token');
  if (token && authService.isTokenValid(token)) {
    router.navigate(['/home']); // Redirect to home if authenticated
    return false; // Deny access to public routes
  }
  return true; // Allow access to unauthenticated routes
};
