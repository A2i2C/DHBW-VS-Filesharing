import { Component } from '@angular/core';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {FileUploadComponent} from '../file-upload/file-upload.component';

@Component({
  selector: 'app-file-card',
  standalone: true,
  imports: [
    MatCard,
    MatCardContent,
    MatIcon,
    MatIconButton,
    FileUploadComponent
  ],
  templateUrl: './file-card.component.html',
  styleUrl: './file-card.component.scss'
})
export class FileCardComponent {
  files = [
    { id: 1, name: 'Document.pdf' },
    { id: 2, name: 'Image.jpg' },
    { id: 3, name: 'Presentation.pptx' },
    { id: 4, name: 'Document.pdf' },
    { id: 5, name: 'Image.jpg' },
    { id: 6, name: 'Presentation.pptx' },
    { id: 7, name: 'Document.pdf' },
    { id: 8, name: 'Image.jpg' },
    { id: 9, name: 'Presentation.pptx' }
  ];

  downloadFile(file: { id: number; name: string }): void {
    console.log('Download file:', file.name);
    // Implement file download logic here
  }

  deleteFile(file: { id: number; name: string }): void {
    console.log('Delete file:', file.name);
    // Implement file deletion logic here
    this.files = this.files.filter(f => f.id !== file.id);
  }
}
