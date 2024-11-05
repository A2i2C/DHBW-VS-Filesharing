import { Component } from '@angular/core';
import {FileUploadService} from '../services/file-upload.service';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [],
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.scss'
})
export class FileUploadComponent {
  selectedFile: File | null = null;

  constructor(private fileUploadService: FileUploadService) {}

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  uploadFile(): void {
    if (this.selectedFile) {
      this.fileUploadService.uploadFile(this.selectedFile).subscribe({
        next: () => {
          console.log('Datei erfolgreich hochgeladen');
          this.selectedFile = null; // Datei nach dem Hochladen zurücksetzen
        },
        error: (err) => {
          console.error('Fehler beim Hochladen der Datei', err);
        }
      });
    }
  }
}
