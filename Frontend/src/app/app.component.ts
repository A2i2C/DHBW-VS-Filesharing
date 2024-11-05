import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FileUploadComponent} from './file-upload/file-upload.component';
import {HttpClientModule} from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FileUploadComponent, HttpClientModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
}
