import {Injectable, signal} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  bucketName = signal<string>('');
  private apiUrl = 'http://localhost/api/file';

  constructor(private http: HttpClient, private authService: AuthService) {}

  createBucket(bucketName: string): Observable<any> {
    const params = new HttpParams()
      .set('bucketName', bucketName);

    const headers = this.authService.getAuthHeaders().headers;

    console.log("Creating bucket: " + bucketName);
    return this.http.post(this.apiUrl + "/createBucket", null, { headers, params, observe: 'response' });
  }

  uploadFile(file: File): Observable<any> {
    if(localStorage.getItem('username') == null) {
      return throwError(() => new Error('Username not found, please log in.'));
    }

    // add the data to the body
    const payload = new FormData();
    payload.append('bucketName', this.bucketName());
    payload.append('file', file);
    payload.append('userID', localStorage.getItem('userID')!);

    const headers = this.authService.getAuthHeaders().headers;

    console.log('Uploading file:', file.name);
    console.log('Uploading to bucket:', this.bucketName());
    console.log('Uploading as user:', localStorage.getItem('username'));

    return this.http.post(this.apiUrl + "/upload", payload, { headers, observe: 'response' });
  }

  deleteFile(objectName: string): Observable<any> {
    const bucketName = this.bucketName();
    const params = new HttpParams()
        .set('bucketName', bucketName)
        .set('filename', objectName);

    const headers = this.authService.getAuthHeaders().headers;

    return this.http.delete(this.apiUrl + "/delete", { headers, params, observe: 'response' });
  }

  downloadFile(objectName: string) {
    const bucketName = this.bucketName();
    const params = new HttpParams()
      .set('bucketName', bucketName)
      .set('filename', objectName);

    const headers = this.authService.getAuthHeaders().headers;

    return this.http.post(this.apiUrl + "/download", null, { headers, params, responseType: 'blob' });
  }

  setBucketName(bucketName: string): void {
    console.log('Setting bucket name to: ' + bucketName);
    this.bucketName.set(bucketName);
  }

  getFiles() {
    const bucketName = this.bucketName();
    const params = new HttpParams().set('bucketName', bucketName);

    const headers = this.authService.getAuthHeaders().headers;

    return this.http.post(this.apiUrl + "/getAllFilesFromBucket", null, { headers, params, observe: 'response' });
  }
}
