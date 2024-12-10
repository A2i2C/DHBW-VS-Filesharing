import {Component, WritableSignal} from '@angular/core';
import {FileUploadComponent} from '../file-upload/file-upload.component';
import {UserStateService} from '../services/user-state.service';

@Component({
  selector: 'app-file-panel',
  standalone: true,
  imports: [
    FileUploadComponent
  ],
  templateUrl: './file-panel.component.html',
  styleUrl: './file-panel.component.scss'
})
export class FilePanelComponent {
  constructor(private userStateService: UserStateService) {}

  getSelectedUser(): WritableSignal<string> {
    return this.userStateService.getSelectedUser();
  }
}
