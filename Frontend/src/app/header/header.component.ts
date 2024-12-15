import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {UserStateService} from '../services/user-state.service';
import {FileService} from '../services/file.service';
import {AllFilesService} from '../services/all-files.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

  constructor(private authService: AuthService, private userStateService: UserStateService, private fileService: FileService, private allFilesService: AllFilesService) {
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  logout(): void{
    this.userStateService.selectedUser.set(''); // Clear selected user
    this.fileService.setBucketName(''); // Reset global bucket name
    this.allFilesService.triggerRefreshFiles(); // Clear file view
    this.authService.logout();
  }
}
