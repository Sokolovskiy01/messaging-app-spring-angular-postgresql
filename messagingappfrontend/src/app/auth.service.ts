import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ControllerService } from './controller.service';
import { AppUser } from './model/models';

@Injectable({
  providedIn: 'root'
})
export class CurrentAppUser {

  isUserLoggedIn: boolean = false;
  userObject: AppUser | null;

  clearDataAfterLogout() {
    this.userObject = null;
    this.isUserLoggedIn = false;
  }

}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private conroller: ControllerService, private router: Router, private currentUser: CurrentAppUser) {
  }

  loginUser(res: HttpResponse<AppUser>) {
    this.currentUser.userObject = res.body;
    this.currentUser.isUserLoggedIn = true;
    this.router.navigate(['/chats']);
  }

  setUser(appUser: AppUser) {
    this.currentUser.userObject = appUser;
    this.currentUser.isUserLoggedIn = true;
  }

  logoutUser() {
    localStorage.removeItem('token');
    this.currentUser.clearDataAfterLogout();
    this.router.navigate(['/login']);
  }

}