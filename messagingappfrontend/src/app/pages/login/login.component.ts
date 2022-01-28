import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser } from 'src/app/model/models';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginCredentials = {
    email: {
      value: "",
      err: false,
      errMessage: ""
    },
    password: {
      value: "",
      err: false,
      errMessage: ""
    }
  }

  loginFail: boolean = false;
  loginFailMessage: string = "";
  loading: boolean = false;

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router, public currentUser: CurrentAppUser) { }

  ngOnInit(): void {
    if (this.currentUser.isUserLoggedIn) {
      this.router.navigate(['/chats']);
    }
    else {
      this.conroller.userGetLogin().then(res => {
        this.auth.loginUser(res);
        this.router.navigate(['/chats']);
      }, err => {
        this.router.navigate(['/login']);
      })
    }
  }

  /**
   * Clears error red outline of input
   * @param field must be 1-st level field of newUserBody
   */
   clearInputErrors(field): void {
    field.err = false;
    field.errMessage = ""
  }

  checkFiedls() : boolean {
    let isErr: boolean = false;
    this.loginCredentials.password.err = false;
    this.loginCredentials.email.err = false;
    this.loginFail = false;
    if (this.loginCredentials.password.value == "") {
      this.loginCredentials.password.err = true;
      this.loginCredentials.password.errMessage = "Password can't be empty";
      isErr = true;
    }
    if (this.loginCredentials.email.value == "") {
      this.loginCredentials.email.err = true;
      this.loginCredentials.email.errMessage = "Email can't be empty";
      isErr = true;
    }
    return !isErr;
  }

  onLoginClick() : void {
    if (!this.loading && this.checkFiedls()) {
      this.loading = true;
      this.conroller.userLogin(this.loginCredentials.email.value, this.loginCredentials.password.value).subscribe((res: HttpResponse<AppUser>) => {
        this.loading = false;
        this.currentUser.userObject = res.body;
        this.currentUser.isUserLoggedIn = true;
        this.router.navigate(['/chats']);
        localStorage.setItem('token', btoa(this.loginCredentials.email.value + ':' + this.loginCredentials.password.value));
      }, (err: HttpErrorResponse) => {
        this.loginCredentials.password.err = true;
        this.loginCredentials.email.err = true;
        this.loginFail = true;
        this.loginFailMessage = err.error;
        this.loading = false;
      });
    }
  }

}
