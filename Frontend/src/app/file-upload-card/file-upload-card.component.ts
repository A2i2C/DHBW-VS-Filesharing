import {Component, ElementRef, ViewChild} from '@angular/core';
import {MatCard} from "@angular/material/card";
import {MatIcon} from "@angular/material/icon";
import {FileCardComponent} from '../file-card/file-card.component';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [
    MatCard,
    MatIcon
  ],
  templateUrl: './file-upload-card.component.html',
  styleUrl: './file-upload-card.component.scss'
})
export class FileUploadCardComponent {
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;

  selectedFile: File | null = null;

  constructor(private fileCard: FileCardComponent) {}

  triggerFileUpload(): void {
    this.fileInput.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      this.selectedFile = target.files[0];
      console.log('Selected file:', this.selectedFile.name);
      this.fileCard.uploadFile(this.selectedFile);
      this.selectedFile = null; // Reset file after uploading
    }
  }
}
