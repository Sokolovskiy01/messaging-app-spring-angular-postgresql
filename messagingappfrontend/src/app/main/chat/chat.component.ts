import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterEvent } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser, AppUserColorsArray, Chat } from 'src/app/model/models';
//import * as Stomp from 'stompjs';
//import * as SockJS from 'sockjs-client';

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

  topic: string = "/messagesWS/greetings";
  stompClient: any;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private conroller: ControllerService, private auth: AuthService, public currentUser: CurrentAppUser) {
    this.navigationSubscription = this.router.events.subscribe((e: any) => {
      if (e instanceof NavigationEnd) {
        this.checkRouteChange();
      }
    });
  }

  ngOnInit(): void {
    if (this.currentUser.isUserLoggedIn) {
      this.loadAppUserChats();
      //this.initWebSocket();
    }
    else {
      this.conroller.userGetLogin().then(res => {
        this.auth.loginUser(res);
        this.loadAppUserChats();
        //this.initWebSocket();
      }, err => {
        this.router.navigate(['/login']);
      })
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

  /*initWebSocket(): void {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.conroller.backendWebsocketEndPoint);
    //this.stompClient = Stomp.over(ws);
  }

  WSConnect() {
    const _this = this;
    _this.stompClient.connect({}, function (frame) {
        _this.stompClient.subscribe(_this.topic, function (sdkEvent) {
            _this.onMessageReceived(sdkEvent);
        });
        //_this.stompClient.reconnect_delay = 2000;
    }, this.errorCallBack);
  }

  WSDisconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  WSSendMessage(message) {
    this.stompClient.send("/app/hello", {}, message);
  }

  onMessageReceived(message) {
    console.log("Message Recieved from Server :: " + message);
    //this.appComponent.handleMessage(JSON.stringify(message.body));
  }

  errorCallBack(error) {
    console.log("errorCallBack -> " + error)
    //setTimeout(() => {
    //    this._connect();
    //}, 5000);
  }*/

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

  testRequest(): void {
    console.log(this.currentUser.userObject);
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
  }

}
