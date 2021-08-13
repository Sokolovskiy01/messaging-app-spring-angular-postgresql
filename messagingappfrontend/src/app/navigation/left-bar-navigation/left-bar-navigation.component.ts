import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/auth.service';

@Component({
  selector: 'app-left-bar-navigation',
  templateUrl: './left-bar-navigation.component.html',
  styleUrls: ['./left-bar-navigation.component.css']
})
export class LeftBarNavigationComponent implements OnInit {

  constructor(private auth: AuthService) { }

  ngOnInit(): void {
  }

  logoutUser(): void {
    this.auth.logoutUser();
  }

}
