import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatFormField} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {UserService} from '../services/user.service';
import {UserStateService} from '../services/user-state.service';
import {FileService} from '../services/file.service';
import {FileCardComponent} from '../file-card/file-card.component';

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
  protected users = signal<{ username: string; bucketname: string }[]>([]);

  protected searched_user: FormGroup = new FormGroup({
    username: new FormControl(''),
  });

  constructor(private userService: UserService, private userStateService: UserStateService, private fileService: FileService, private fileCardComponent: FileCardComponent) {}

  ngOnInit() {
    this.userService.getUserPartner(localStorage.getItem('username')!).subscribe({
      next: (response) => {
        for(const user of response.body) {
          this.users.update(values => [...values, {  username: user.username, bucketname: user.bucketname }]);
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
            console.log('Partner added successfully:', res);
            const bucketName = res.body.bucketName;
            this.fileService.createBucket(bucketName).subscribe({
              next: () => {
                console.log(`Bucket ${bucketName} created successfully.`);
                this.users.update(values => [...values, { username: username, bucketname: bucketName }]);
              },
              error: (err: any) => {
                console.error('Error creating bucket:', err);
              }
            });
          }
        },
        error: (err: any) => {
          console.error('Error adding partner:', err);
        }
      });
    }
  }

  setSelectedUser(user: string, bucketname: string): void {
    this.userStateService.selectedUser.set(user);
    this.fileService.setBucketName(bucketname);
    this.fileCardComponent.getFiles();
  }

  getSelectedUser(): WritableSignal<string> {
    return this.userStateService.getSelectedUser();
  }
}
