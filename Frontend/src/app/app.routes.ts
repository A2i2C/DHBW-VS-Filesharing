import { Routes } from '@angular/router';
import {LoginComponent} from './login/login.component';
import {SignupComponent} from './signup/signup.component';
import {HomeComponent} from './home/home.component';
import {authGuard} from './auth/auth.guard';
import {publicGuard} from './auth/public.guard';

export const routes: Routes = [
  { path: 'signup', component: SignupComponent, canActivate: [publicGuard] },
  { path: 'login', component: LoginComponent, canActivate: [publicGuard] },
  { path: 'home', component: HomeComponent, canActivate: [authGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' }, // Default route to login page
  { path: '**', redirectTo: '/login' } // Wildcard route for 404
];
