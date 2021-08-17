import { Component } from '@angular/core';
import { AuthService, CurrentAppUser } from './auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Messaging App';

  constructor (public currentUser: CurrentAppUser) {}

}
