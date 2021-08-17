import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser, UserStatus } from 'src/app/model/models';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchQuery: string = "";
  usersList = []; /*[
    {
      id: 2,
      name: "Jan Kowalski",
      dateOfBirth: new Date("2001-06-13"),
      gender: "Male",
      imageUrl: "",
      comment: "",
      email: "newuser@gmail.com",
      password: null,
      userStatus: UserStatus.Active,
      banMessage: "",
      lastLogin: new Date("2021-08-13T15:47:24.814488"),
      colors: { background: '#d8edff', text: '#3797ec' }
    },
    {
      id: 1,
      name: "Dmytro Sokolovskyi",
      dateOfBirth: new Date("2001-06-13"),
      gender: "Male",
      imageUrl: "",
      comment: "",
      email: "newuser@gmail.com",
      password: null,
      userStatus: UserStatus.Active,
      banMessage: "",
      lastLogin: new Date("2021-08-13T15:47:24.814488"),
      colors: { background: '#ddf6d9', text: '#43c52d' }
    }
  ]*/

  randomCollorArray = [
    { background: '#d8edff', text: '#3797ec' },
    { background: '#ddf6d9', text: '#43c52d' },
    { background: '#fff0de', text: '#f69c2f' },
    { background: '#ffd9d9', text: '#f83835' }
  ]

  loading: boolean = false;

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router, public currentUser: CurrentAppUser) {
    console.log(currentUser.isUserLoggedIn);
  }

  ngOnInit(): void {
    
  }

  getRandomColor() {
    return this.randomCollorArray[Math.floor(Math.random() * this.randomCollorArray.length)];
  }

  getUserInitials(userName: string): string {
    let names = userName.split(' ');
    let initials = names[0].charAt(0).toUpperCase() + ((names[1]) ? names[1].charAt(0).toUpperCase() : '');
    return initials;
  }

  getUsersByQuery(): void {
    if (this.searchQuery === "") {
      this.usersList = [];
      return;
    }
    else {
      this.loading = true;
      this.usersList = [];
      this.conroller.get("/users/search?q=" + this.searchQuery + "&uid=" + this.currentUser.userObject.id).subscribe((res: HttpResponse<AppUser[]>) => {
        res.body.forEach((user: any) => {
          user.colors = this.getRandomColor();
        });
        this.usersList = res.body
        this.loading = false;
      }, (err: HttpErrorResponse) => console.error(err));
    }
  }

}
