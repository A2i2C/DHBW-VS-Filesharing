import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {MatButton} from '@angular/material/button';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatFormField} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {UserService} from '../services/user.service';
import {UserStateService} from '../services/user-state.service';
import {FileService} from '../services/file.service';
import {AllFilesService} from '../services/all-files.service';

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
  users: { username: string; bucketname: string }[] = []; // List of users to display
  protected errorMessage = signal<string>('');

  protected searched_user: FormGroup = new FormGroup({
    username: new FormControl(''),
  });

  constructor(private userService: UserService, private userStateService: UserStateService, private fileService: FileService, private allFilesService: AllFilesService) {}

  ngOnInit() {
    this.errorMessage.set("");
    this.userService.getUserPartner(localStorage.getItem('username')!).subscribe({
      next: (response) => {
        const users = response.body;
        users.forEach((user: { username: string; bucketname: string }, index: number) => {
          this.users.push({ username: user.username, bucketname: user.bucketname });

          // Check if this is the last user to then select it to remove the bug of still displaying the last user's files
          if (index === users.length - 1) {
            this.setSelectedUser(user.username, user.bucketname);
          }
        });
      },
      error: (err) => {
        this.errorMessage.set("Errors when fetching partners");
        console.error('Errors when fetching partners: ', err);
      }
    });
  }

  addUser() {
    // add the partner and create the bucket
    this.errorMessage.set("");
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
                this.users.push({ username: username, bucketname: bucketName });
                // Clear the input field
                this.searched_user.reset();
              },
              error: (err: any) => {
                this.errorMessage.set("Couldn't start file converstaion with user " + username);
                console.error('Error creating bucket:', err);
              }
            });
          }
        },
        error: (err: any) => {
          this.errorMessage.set("Couldn't find user with username: " + username);
          console.error('Error adding partner:', err);
        }
      });
    }
  }

  setSelectedUser(user: string, bucketname: string): void {
    // Set the selected user and the bucket name and refresh the files
    this.errorMessage.set("");
    this.userStateService.selectedUser.set(user);
    this.fileService.setBucketName(bucketname);
    this.allFilesService.triggerRefreshFiles();
  }

  getSelectedUser(): WritableSignal<string> {
    return this.userStateService.getSelectedUser();
  }
}
