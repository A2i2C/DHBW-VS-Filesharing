import {Component, OnInit, WritableSignal} from '@angular/core';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatFormField} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {UserService} from '../services/user.service';
import {UserStateService} from '../services/user-state.service';

@Component({
  selector: 'app-user-panel',
  standalone: true,
  imports: [
    MatButton,
    MatCard,
    MatCardContent,
    MatFormField,
    MatInput,
    ReactiveFormsModule
  ],
  templateUrl: './user-panel.component.html',
  styleUrl: './user-panel.component.scss'
})
export class UserPanelComponent implements OnInit{
  users: String[] = [];

  protected searched_user: FormGroup = new FormGroup({
    username: new FormControl(''),
  });

  constructor(private userService: UserService, private userStateService: UserStateService) {}

  ngOnInit() {
    this.userService.getUserPartner(localStorage.getItem('username')!).subscribe({
      next: (response) => {
        for(const user of response) {
          this.users.push(user.username);
        }
      },
      error: (err) => {
        console.error('Fehler beim holen der Partner', err);
      }
    });
  }

  addUser() {
    const username = this.searched_user.value.username;
    if (username) {
      this.userService.addPartner(username).subscribe({
        next: (res) => {
          if (res.status === 200) {
            this.users.push(username);
          }
        },
        error: (err) => {
          console.error('Fehler beim hinzufügen des Partners', err);
      }
    });
    }
  }

  setSelectedUser(user: string): void {
    this.userStateService.selectedUser.set(user);
  }

  getSelectedUser(): WritableSignal<string> {
    return this.userStateService.getSelectedUser();
  }
}
