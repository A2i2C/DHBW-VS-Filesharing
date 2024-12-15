import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth.service';

export const publicGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = localStorage.getItem('token');
  if (token && authService.isTokenValid(token)) {
    router.navigate(['/home']); // Redirect to home if authenticated
    return false; // Allow access to unauthenticated route (home)
  }

  return true; // Deny access to public routes (signup, login)
};
