import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {FileUploadCardComponent} from '../file-upload-card/file-upload-card.component';
import {FileService} from '../services/file.service';
import {AllFilesService} from '../services/all-files.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-file-card',
  standalone: true,
  imports: [
    MatCard,
    MatCardContent,
    MatIcon,
    MatIconButton,
    FileUploadCardComponent
  ],
  templateUrl: './file-card.component.html',
  styleUrl: './file-card.component.scss'
})
export class FileCardComponent implements OnInit, OnDestroy {
  files: {id: number, name: string}[] = [];
  filesId = 0;
  private subscription: Subscription | undefined;

  constructor(private fileService: FileService, private allFilesService: AllFilesService) {}

  ngOnInit() {
    this.getFiles();
    this.subscription = this.allFilesService.refreshFiles$.subscribe(() => {
      this.getFiles();
    });
  }

  ngOnDestroy() {
    if(this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  uploadFile(selectedFile: File): void {
    this.fileService.uploadFile(selectedFile).subscribe({
      next: () => {
        console.log('Uploaded file successfully: ' + selectedFile.name );
        this.filesId++;
        this.files.push({ id: this.filesId, name: selectedFile.name });
      },
      error: (err) => {
        console.error('Fehler beim Hochladen der Datei', err);
      }
    });
  }

  downloadFile(file: { id: number; name: string }): void {
    console.log('Download file:', file.name);
    this.fileService.downloadFile(file.name).subscribe({
      next: (blob) => {
        console.log('File downloaded successfully: ', file.name);
        // Create a temporary URL for the Blob and trigger download
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = file.name;
        document.body.appendChild(a);
        a.click();

        // Clean up
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      },
      error: (err) => {
        console.error('Failed to download file:', err);
      }
    });
  }

  deleteFile(file: { id: number; name: string }): void {
    this.fileService.deleteFile(file.name).subscribe({
      next: () => {
        this.filesId--;
        this.files = this.files.filter(f => f.id !== file.id);
        console.log('File deleted successfully');
      },
      error: (err) => {
        console.error('Failed to delete file: ', err);
      }
    });
  }

  getFiles(): void {
    this.files = []; // Clear the current files
    this.filesId = 0;

    this.fileService.getFiles().subscribe({
      next: (response) => {
        const body = response.body as string[];
        console.log('Files loaded successfully' + body);

        if (Array.isArray(body)) {
          for (const file of body) {
            this.filesId++;
            this.files.push({ id: this.filesId, name: file });
          }
        } else {
          console.error('Unexpected response format:', body);
      }
    },
    error: (err) => {
      console.error('Failed to get files:', err);
    }
    });
  }
}
