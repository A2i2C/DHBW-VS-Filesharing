import {Component, WritableSignal} from '@angular/core';
import {UserStateService} from '../services/user-state.service';
import {FileCardComponent} from '../file-card/file-card.component';

@Component({
  selector: 'app-file-panel',
  standalone: true,
  imports: [
    FileCardComponent
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
