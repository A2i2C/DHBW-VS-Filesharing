import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AllFilesService {
  private refreshFilesSubject = new Subject<void>();

  refreshFiles$ = this.refreshFilesSubject.asObservable();

  triggerRefreshFiles() {
    this.refreshFilesSubject.next();
  }
}
