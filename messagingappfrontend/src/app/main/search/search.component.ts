import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser, Chat, UserStatus, AppUserColorsArray } from 'src/app/model/models';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchQuery: string = "";
  usersList = [];

  loading: boolean = false;

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router, public currentUser: CurrentAppUser) {
    console.log(currentUser.isUserLoggedIn);
  }

  ngOnInit(): void {
    
  }

  getRandomColor() {
    return AppUserColorsArray[Math.floor(Math.random() * AppUserColorsArray.length)];
  }

  // to assign existing color scheme to AppUser without saving color sheme to database
  getColorByUserId(appUserId: number) {
    return AppUserColorsArray[appUserId % AppUserColorsArray.length];
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
          user.colors = this.getColorByUserId(user.id);
        });
        this.usersList = res.body
        this.loading = false;
      }, (err: HttpErrorResponse) => console.error(err));
    }
  }

  onSendMessageClicked(user: AppUser): void {
    let sendBody = {
      user1Id: this.currentUser.userObject.id,
      user2Id: user.id
    }
    this.conroller.post('/chats/create', sendBody).subscribe((res: HttpResponse<Chat>) => {
      this.router.navigate(['/chats/messages/' + res.body.id]);
    }, (err: HttpErrorResponse) => console.error(err) );
  }

  getServerUrl(): string {
    return this.conroller.backendUrl;
  }

}
