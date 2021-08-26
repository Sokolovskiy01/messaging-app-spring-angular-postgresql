import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { UserStatus } from 'src/app/model/models';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {

  uploadImageText: string = "Set profile photo";
  imageSelected: boolean = false;

  userNewImage: File;
  loadingValue: number = 0;

  editUserBody = {
    firstName: { value: "", err: false, errMessage: "" },
    lastName: { value: "", err: false, errMessage: "" },
    comment: { value: "", err: false, errMessage: "" },
    dateOfBirth: { value: "2010-01-01", err: false, errMessage: "" },
    gender: { value: "", err: false, errMessage: "" },
    email: { value: "" }
  }

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router, private route: ActivatedRoute, public currentUser: CurrentAppUser) { }

  ngOnInit(): void {
    this.loadUserInfo();
  }

  loadUserInfo(): void {
    this.splitCurrentAppUserNames();
  }

  splitCurrentAppUserNames(): void {
    if (this.currentUser.isUserLoggedIn) {
      let names = this.currentUser.userObject.name.split(" ");
      console.log(names);
      let name = names[0];
      let lastname = "";
      for (let i = 1; i < names.length; i++) lastname += names[i];
      this.editUserBody.firstName.value = name;
      this.editUserBody.lastName.value = lastname;
    }
  }

  clearInputErrors(field): void {
    field.err = false;
    field.errMessage = ""
  }

  getUserStatus(number) {
    return UserStatus[number];
  }

  selectFile(event: any): void {

  }

}
