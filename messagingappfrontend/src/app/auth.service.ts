import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ControllerService } from './controller.service';
import { AppUser } from './model/models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  currentUser: AppUser;
  isUserLoggedIn: boolean = false;

  constructor(private conroller: ControllerService, private router: Router) {
    // try to login
  }

  loginUser(email: string, password: string) {
    this.conroller.userLogin(email, password).subscribe((res: HttpResponse<AppUser>) => {
      this.currentUser = res.body;
      this.isUserLoggedIn = true;
      this.router.navigate(['/chats'])
      //console.log(res, this.currentUser);
    }, (err: HttpErrorResponse) => console.error(err.error) );
  }

  setUser(appUser: AppUser) {
    this.currentUser = appUser;
    this.isUserLoggedIn = true;
  }

  logoutUser() {
    //let userId = this.currentUser.id;
    //this.conroller.post('/logout', { userId }).subscribe((res: HttpResponse<any>) => {
      this.currentUser = null;
      this.isUserLoggedIn = false;
      this.router.navigate(['/login']);
    //}, (err: HttpErrorResponse) => console.error(err.error) );
  }

}