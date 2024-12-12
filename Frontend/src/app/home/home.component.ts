import { Component } from '@angular/core';
import {UserPanelComponent} from '../user-panel/user-panel.component';
import {FilePanelComponent} from '../file-panel/file-panel.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    UserPanelComponent,
    FilePanelComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
