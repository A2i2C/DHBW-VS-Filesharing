import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, catchError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {
  private apiUrl = 'http://localhost:8080/api/filehandler/upload'; // API-URL des Spring Boot-Backends

  constructor(private http: HttpClient) {}

  uploadFile(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post(this.apiUrl, formData, {
      headers: new HttpHeaders({
        'Content-Type': 'multipart/form-data'})
    }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('File upload error:', error);
    return throwError(() => new Error('File upload failed, please try again.'));
  }
}
