import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterEvent } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser, AppUserColorsArray, Chat } from 'src/app/model/models';


interface ComponentDisplayChat {
  chatId: number,
  recipient: AppUser,
  currentAppUserSeen: boolean,
  chatColors: any,
  display: boolean;
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
  filterString: string = "";

  chatsSubscriprtion: Subscription;
  readonly responseDestionation: string = "/user/userMessages/chats";
  readonly responseSubscription: string = "/chatsCheck";
  readonly rejectSubscriprion: string = "/chatsUnsubscribe";
  tmpAppUserId = null;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private conroller: ControllerService, private auth: AuthService, public currentUser: CurrentAppUser) {
    this.navigationSubscription = this.router.events.subscribe((e: any) => {
      if (e instanceof NavigationEnd) {
        this.checkRouteChange();
      }
    });
  }
  
  ngOnInit(): void {
    if (this.currentUser.isUserLoggedIn) {
      //this.loadAppUserChats();
      this.tmpAppUserId = this.currentUser.userObject.id;
      this.initChatsCheck();
    }
    else {
      this.conroller.userGetLogin().then((res: HttpResponse<AppUser> | any) => {
        this.auth.loginUser(res);
        //this.loadAppUserChats();
        this.tmpAppUserId = this.currentUser.userObject.id;
        this.initChatsCheck();
      }, err => {
        this.router.navigate(['/login']);
      });
    }
  }

  loadAppUserChats(): void {
    if (this.currentUser.isUserLoggedIn) {
      this.chatsLoading = true;
      this.conroller.get('/chats/user/' + this.currentUser.userObject.id).subscribe((res: HttpResponse<Chat[]>) => {
        this.buildChatList(res.body);
        this.chatsLoading = false;
      }, (err: HttpErrorResponse) => console.error(err) );
    }
  }

  initChatsCheck(): void {
    //console.log(this.responceDestionation + this.conroller.getSocketSessionId());
    const _this = this;
    this.chatsSubscriprtion = this.conroller.stompClient.subscribe(this.responseDestionation, function(sdkEvent) {
      let responseBody = JSON.parse(sdkEvent.body);
      console.log("Chat response : " + sdkEvent.body);
      //if (responseBody.answer == true || _this.userChats.length == 0) {
        _this.loadAppUserChats();
      //}
    });
    console.log(this.chatsSubscriprtion);
    this.checkChats();
  }

  checkChats(): void {
    this.conroller._send({userId: this.currentUser.userObject.id}, this.responseSubscription);
  }

  unsubscribeChats(): void{
    if (this.chatsSubscriprtion) {
      this.conroller._send({userId: this.tmpAppUserId }, this.rejectSubscriprion);
      this.chatsSubscriprtion.unsubscribe();
    }
    this.chatsSubscriprtion = null;
  }

  buildChatList(chats: Chat[]): void {
    this.userChats = [];
    chats.forEach((chat) => {
      let recipient = (chat.user1.id == this.currentUser.userObject.id) ? chat.user2 : chat.user1; 
      let newComponentChatElement: ComponentDisplayChat = {
        chatId: chat.id,
        recipient: recipient,
        currentAppUserSeen: (chat.user1.id == this.currentUser.userObject.id) ? chat.user1Seen : chat.user2Seen,
        chatColors: this.getColorByUserId(recipient.id),
        display: true
      }
      this.userChats.push(newComponentChatElement);
    })
  }

  onChatClicked(): void {
    this.clearFilter();
  }

  filterAppUserChats(): void {
    this.userChats.forEach((chat: ComponentDisplayChat) => {
      if (chat.recipient.name.toLowerCase().includes(this.filterString.toLowerCase())) chat.display = true;
      else chat.display = false;
    });
  }

  clearFilter(): void {
    this.filterString = "";
    this.filterAppUserChats();
  }

  checkRouteChange() : void {
    let activeChildren = this.activatedRoute.children.length;
    if (activeChildren != 0) this.noChildRoute = false;
    else this.noChildRoute = true;
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

  getServerUrl(): string {
    return this.conroller.backendUrl;
  }

  ngOnDestroy(): void {
    this.navigationSubscription.unsubscribe();
    this.unsubscribeChats();
  }

}
