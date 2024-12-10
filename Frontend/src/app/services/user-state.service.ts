import {Injectable, signal, WritableSignal} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserStateService {
  selectedUser: WritableSignal<string> = signal('');

  getSelectedUser(): WritableSignal<string> {
    return this.selectedUser;
  }
}
