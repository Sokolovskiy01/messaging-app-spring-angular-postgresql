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
    // try to login
  }

  loginUser(email: string, password: string) {
    this.conroller.userLogin(email, password).subscribe((res: HttpResponse<AppUser>) => {
      this.currentUser.userObject = res.body;
      this.currentUser.isUserLoggedIn = true;
      this.router.navigate(['/chats'])
      //console.log(res, this.currentUser);
    }, (err: HttpErrorResponse) => console.error(err.error) );
  }

  setUser(appUser: AppUser) {
    this.currentUser.userObject = appUser;
    this.currentUser.isUserLoggedIn = true;
  }

  logoutUser() {
    //let userId = this.currentUser.id;
    //this.conroller.post('/logout', { userId }).subscribe((res: HttpResponse<any>) => {
      this.currentUser.clearDataAfterLogout();
      this.router.navigate(['/login']);
    //}, (err: HttpErrorResponse) => console.error(err.error) );
  }

}