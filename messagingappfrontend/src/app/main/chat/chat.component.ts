import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterEvent } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser, Chat } from 'src/app/model/models';

interface ComponentDisplayChat {
  chatId: number,
  recipient: AppUser,
  currentAppUserSeen: boolean,
  chatColors: any
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy  {

  noChildRoute: boolean = true;
  navigationSubscription: Subscription;

  userChats: ComponentDisplayChat[] = [];
  chatsLoading: boolean = false;

  randomCollorArray = [
    { background: '#d8edff', text: '#3797ec' },
    { background: '#ddf6d9', text: '#43c52d' },
    { background: '#fff0de', text: '#f69c2f' },
    { background: '#ffd9d9', text: '#f83835' }
  ]

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private conroller: ControllerService, private auth: AuthService, public currentUser: CurrentAppUser) {
    this.navigationSubscription = this.router.events.subscribe((e: any) => {
      if (e instanceof NavigationEnd) {
        this.checkRouteChange();
      }
    });
  }

  ngOnInit(): void {
    this.loadAppUserChats();
  }

  loadAppUserChats() : void {
    if (this.currentUser.isUserLoggedIn) {
      this.chatsLoading = true;
      this.conroller.get('/chats/user/' + this.currentUser.userObject.id).subscribe((res: HttpResponse<Chat[]>) => {
        this.buildChatList(res.body);
        this.chatsLoading = false;
      }, (err: HttpErrorResponse) => console.error(err) );
    }
  }

  buildChatList(chats: Chat[]): void {
    chats.forEach((chat) => {
      let recipient = (chat.user1.id == this.currentUser.userObject.id) ? chat.user2 : chat.user1; 
      let newComponentChatElement: ComponentDisplayChat = {
        chatId: chat.id,
        recipient: recipient,
        currentAppUserSeen: (chat.user1.id == this.currentUser.userObject.id) ? chat.user1Seen : chat.user2Seen,
        chatColors: this.getColorByUserId(recipient.id)
      }
      this.userChats.push(newComponentChatElement);
    })
  }

  checkRouteChange() : void {
    let activeChildren = this.activatedRoute.children.length;
    if (activeChildren != 0) this.noChildRoute = false;
    else this.noChildRoute = true;
  }

  getRandomColor() {
    return this.randomCollorArray[Math.floor(Math.random() * this.randomCollorArray.length)];
  }

  // to assign existing color scheme to AppUser without saving color sheme to database
  getColorByUserId(appUserId: number) { 
    return this.randomCollorArray[appUserId % this.randomCollorArray.length];
  }

  getUserInitials(userName: string): string {
    let names = userName.split(' ');
    let initials = names[0].charAt(0).toUpperCase() + ((names[1]) ? names[1].charAt(0).toUpperCase() : '');
    return initials;
  }

  getServerUrl(): string {
    return this.conroller.backendUrl;
  }

  ngOnDestroy(): void {
    this.navigationSubscription.unsubscribe();
  }

}
