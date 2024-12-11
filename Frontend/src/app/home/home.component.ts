import { Component } from '@angular/core';
import {UserPanelComponent} from '../user-panel/user-panel.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    UserPanelComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
