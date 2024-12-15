import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = localStorage.getItem('token');
  if (token && authService.isTokenValid(token)) {
    return true; // Allow access if token is valid
  }

  router.navigate(['/login']); // Redirect to login if not authenticated
  return false; // Deny access
};
