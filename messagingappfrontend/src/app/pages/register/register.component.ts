import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser, UserStatus } from 'src/app/model/models';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  newUserBody = {
    email: { value: "", err: false, errMessage: "" },
    password1: { value: "", err: false, errMessage: "" },
    password2: { value: "", err: false, errMessage: "" },
    firstName: { value: "", err: false, errMessage: "" },
    lastName: { value: "", err: false, errMessage: "" },
    dateOfBirth: { value: "2010-01-01", err: false, errMessage: "" },
    gender: { value: "", err: false, errMessage: "" }
  }

  loading: boolean = false; // for load animation
  regFail: boolean = false;
  regFailMessage: string = "";

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router) {

  }

  ngOnInit(): void {
    
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
    let isErr = false;
    if (!this.conroller.validateEmail(this.newUserBody.email.value)) {
      this.newUserBody.email.err = true;
      this.newUserBody.email.errMessage = "Provided email is incorrect";
      isErr = true;
    }
    if (this.newUserBody.password1.value.length < 8) {
      this.newUserBody.password1.err = true;
      this.newUserBody.password1.errMessage = "Password must be longer than 8 symbols";
      isErr = true;
    }
    if (this.newUserBody.password1.value != this.newUserBody.password2.value) {
      this.newUserBody.password2.err = true;
      this.newUserBody.password2.errMessage = "Passwords doesn't match";
      isErr = true;
    }
    if (this.newUserBody.firstName.value.length < 2) {
      this.newUserBody.firstName.err = true;
      this.newUserBody.firstName.errMessage = "First name is too short";
      isErr = true;
    }
    if (this.newUserBody.lastName.value.length < 2) {
      this.newUserBody.lastName.err = true;
      this.newUserBody.lastName.errMessage = "Last name is too short";
      isErr = true;
    }
    if (this.newUserBody.gender.value != "Male" && this.newUserBody.gender.value != "Female") {
      this.newUserBody.gender.err = true;
      this.newUserBody.gender.errMessage = "Provide your gender";
      isErr = true;
    }
    let minDate = new Date();
    minDate.setFullYear(minDate.getFullYear() - 18);
    if (minDate.getTime() < new Date(this.newUserBody.dateOfBirth.value).getTime()) {
      this.newUserBody.dateOfBirth.err = true;
      this.newUserBody.dateOfBirth.errMessage = "You must be older than 18 years to continue";
      isErr = true;
    }
    return !isErr;
  }

  onCreateAccountClick(): void {
    if (this.loading) return;
    else {
      if (this.checkFiedls()) {
        this.loading = true;
        let newAppUser: AppUser = {
          id: undefined,
          name: this.newUserBody.firstName.value + " " + this.newUserBody.lastName.value,
          dateOfBirth: new Date(this.newUserBody.dateOfBirth.value),
          gender: this.newUserBody.gender.value,
          imageUrl: "",
          comment: "",
          email: this.newUserBody.email.value,
          password: this.newUserBody.password1.value,
          userStatus: UserStatus.Active,
          banMessage: "",
          lastLogin: new Date()
        }
        console.log(newAppUser);
        this.conroller.post('/users/register', newAppUser).subscribe((res: HttpResponse<AppUser>) => {
          this.auth.setUser(res.body);
          this.router.navigate(['/chats']);
          this.loading = false;
        }, (err: HttpErrorResponse) => {
          this.regFail = true;
          this.regFailMessage = err.error;
          this.loading = false;
        })
      }
    }
  }

}
