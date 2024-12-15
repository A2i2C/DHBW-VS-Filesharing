import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AllFilesService {
  private refreshFilesSubject = new Subject<void>();

  refreshFiles$ = this.refreshFilesSubject.asObservable();

  triggerRefreshFiles() {
    this.refreshFilesSubject.next(); // Emit an event to all subscribers so that the files list can be refreshed
  }
}
