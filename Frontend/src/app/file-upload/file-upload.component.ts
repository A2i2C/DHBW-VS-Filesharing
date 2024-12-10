import {Component, ElementRef, ViewChild} from '@angular/core';
import {FileUploadService} from '../services/file-upload.service';
import {MatCard} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [
    MatCard,
    MatIcon
  ],
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.scss'
})
export class FileUploadComponent {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  selectedFile: File | null = null;

  constructor(private fileUploadService: FileUploadService) {}

  triggerFileUpload(): void {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      this.selectedFile = target.files[0];
      console.log('Selected file:', this.selectedFile.name);
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
