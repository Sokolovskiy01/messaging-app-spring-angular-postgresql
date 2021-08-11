import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth.service';
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

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router) {
    if (this.auth.isUserLoggedIn) {
      this.router.navigate(['/chats']);
    }
  }

  ngOnInit(): void {

  }

  onLoginClick() : void {
    if (this.checkFiedls()) {
      this.conroller.userLogin(this.loginCredentials.email.value, this.loginCredentials.password.value).subscribe((res: HttpResponse<AppUser>) => {
        this.auth.currentUser = res.body;
        this.auth.isUserLoggedIn = true;
        this.router.navigate(['/chats']);
      }, (err: HttpErrorResponse) => {
        this.loginCredentials.password.err = true;
        this.loginCredentials.email.err = true;
        this.loginFail = true;
        this.loginFailMessage = "Email or password incorrect";
      });
    }
  }

  clearEmailErrors(): void {
    this.loginCredentials.email.err = false;
  }

  clearPasswordErrors(): void {
    this.loginCredentials.password.err = false;
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

}
