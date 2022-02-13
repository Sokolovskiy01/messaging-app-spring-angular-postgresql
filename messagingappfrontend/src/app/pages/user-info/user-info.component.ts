import { formatDate } from '@angular/common';
import { HttpErrorResponse, HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser, AppUserColorsArray, UserStatus } from 'src/app/model/models';

@Component({
  selector: 'app-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.css']
})
export class UserInfoComponent implements OnInit {

  uploadImageText: string = "Set profile photo";
  imageSelected: boolean = false;
  userNewImage: File;
  isNewImageLoading: boolean = false;
  loadingProgress: number = 0;

  editUserBody = {
    firstName: { value: "", err: false, errMessage: "" },
    lastName: { value: "", err: false, errMessage: "" },
    comment: { value: "", err: false, errMessage: "" },
    dateOfBirth: { value: "2010-01-01", err: false, errMessage: "" },
    gender: { value: "", err: false, errMessage: "" },
    email: { value: "" },
    colors: { background: 'transparent', text: '#000000' }
  }
  anyChanges: boolean = false;

  isErrorMessage: boolean = false;
  isSuccessMessage: boolean = false;

  public setErrorMessageFalse = () : TimerHandler => {
    this.isErrorMessage = false;
    return "s";
  }

  public setSuccessMessageFalse = () : TimerHandler => {
    this.isSuccessMessage = false;
    return "s";
  }

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router, private route: ActivatedRoute, public currentUser: CurrentAppUser) { }

  ngOnInit(): void {
    if (this.currentUser.isUserLoggedIn) {
      this.refreshUserInfo();
    }
    else {
      this.conroller.userGetLogin().then((res: HttpResponse<AppUser> | any) => {
        this.auth.loginUser(res);
        this.refreshUserInfo();
      }, err => {
        this.router.navigate(['/login']);
      })
    }
  }

  refreshUserInfo(): void {
    this.splitCurrentAppUserNames();
    this.editUserBody.comment.value = this.currentUser.userObject.comment;
    this.editUserBody.dateOfBirth.value = formatDate(this.currentUser.userObject.dateOfBirth, 'yyyy-MM-dd', 'en');
    this.editUserBody.gender.value = this.currentUser.userObject.gender;
    this.editUserBody.email.value = this.currentUser.userObject.email;
    this.editUserBody.colors = this.getColorByUserId(this.currentUser.userObject.id);
  }

  splitCurrentAppUserNames(): void {
    if (this.currentUser.isUserLoggedIn) {
      let names = this.currentUser.userObject.name.split(" ");
      let name = names[0];
      let lastname = "";
      for (let i = 1; i < names.length; i++) lastname += names[i];
      this.editUserBody.firstName.value = name;
      this.editUserBody.lastName.value = lastname;
    }
  }

  getColorByUserId(appUserId: number) {
    return AppUserColorsArray[appUserId % AppUserColorsArray.length];
  }

  clearInputErrors(field): void {
    field.err = false;
    field.errMessage = "";
    this.anyChanges = true;
  }

  onSaveChangesClicked(): void {
    if (this.anyChanges) {
      let newUserBody: any = {};
      Object.assign(newUserBody, this.currentUser.userObject);
      newUserBody.name = this.editUserBody.firstName.value + " " + this.editUserBody.lastName.value;
      newUserBody.dateOfBirth = this.editUserBody.dateOfBirth.value;
      newUserBody.comment = this.editUserBody.comment.value;
      newUserBody.gender = this.editUserBody.gender.value;
      this.conroller.put('/users/update', newUserBody).subscribe((res: HttpResponse<AppUser>) => {
        this.auth.setUser(res.body);
        this.refreshUserInfo();
        this.anyChanges = false;
        this.isSuccessMessage = true;
        setTimeout(() => { this.setSuccessMessageFalse() }, 5000);
      }, (err: HttpErrorResponse) => {
        console.error(err);
        this.refreshUserInfo();
        this.isErrorMessage = true;
        setTimeout(() => { this.setErrorMessageFalse() }, 5000);
      });
    }
  }

  selectFile(event: any): void {
    let selectedFiles = event.target.files;
    if (selectedFiles.length == 0) {
      //alert("no file selected");
      return;
    }
    else {
      this.userNewImage = selectedFiles[0];
      //alert(this.userNewImage.name);
      this.conroller.uploadAvatar(this.userNewImage, this.currentUser.userObject.id).subscribe(
        (event: any) => {
          if (event.type === HttpEventType.UploadProgress) {
            this.loadingProgress = Math.round(100 * event.loaded / event.total);
            console.log(this.loadingProgress);
          }
          else if (event instanceof HttpResponse) {
            this.auth.setUser(event.body);
            this.userNewImage = undefined;
            this.refreshUserInfo();
          } 
        },
        (err : any) => {
          console.log(err);
          this.loadingProgress = 0;
          if (err.error && err.error.message) {
            alert(err.error.message);
          } else {
            alert('Could not upload the file!');
          }
          this.userNewImage = undefined;
        }
      )
    }
  }

  getUserInitials(userName: string): string {
    let names = userName.split(' ');
    let initials = names[0].charAt(0).toUpperCase() + ((names[1]) ? names[1].charAt(0).toUpperCase() : '');
    return initials;
  }

  getServerUrl(): string {
    return this.conroller.backendUrl;
  }

}
