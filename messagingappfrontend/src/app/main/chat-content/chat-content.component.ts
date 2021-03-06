import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { AppUser, AppUserColor, Chat, Message, AppUserColorsArray } from 'src/app/model/models';

@Component({
  selector: 'app-chat-content',
  templateUrl: './chat-content.component.html',
  styleUrls: ['./chat-content.component.css']
})
export class ChatContentComponent implements OnInit, OnDestroy  {

  navigationSubscription: Subscription;
  _window = window;

  /* Vataibles for messageField */
  messageText: string = "";
  messageRowsCount: number = 1;
  maxRows: number = 5;
  minRows: number = 1;

  chatId: string;

  currentChat: Chat;
  recipient: AppUser;
  recipientColor: AppUserColor;
  currentUserColor: AppUserColor;
  lastRecipientLoginDate: Date;
  chatMessages: Message[] = []; // must be sorted from newer to older
  loading: boolean = true;

  showInfo: boolean = false;

  messagesSubscription: Subscription;
  readonly responseDestionation: string = "/user/userMessages/messages";
  readonly responseSubscription: string = "/messagesCheck";
  readonly rejectSubscriprion: string = "/messagesUnsubscribe";
  tmpAppUserId = null;

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router, private route: ActivatedRoute, public currentUser: CurrentAppUser) {
    this.route.params.subscribe((params: Params) => {
      this.chatId = params.chatId;
    });
    this.navigationSubscription = this.router.events.subscribe((event: any) => {
      if (event instanceof NavigationEnd) {
        this.unsubscribeMessages();
        this.reloadComponent();
        //this.loadChat();
        // change subscription destination
      }
    });
  }

  ngOnInit(): void {
    this.tmpAppUserId = this.currentUser.userObject.id;
    this.currentUserColor = this.getColorByUserId(this.currentUser.userObject.id);
  }

  reloadComponent(): void {
    this.loadChat();
    this.initMessagesCheck();
  }

  loadChat() {
    if (this.chatId && this.currentUser.isUserLoggedIn) {
      this.loading = true;
      this.conroller.get('/chats/get/' + this.chatId).subscribe((res: HttpResponse<Chat>) => {
        this.currentChat = res.body;
        if (this.currentChat.user1.id == this.currentUser.userObject.id) {
          this.recipient = this.currentChat.user2
          this.lastRecipientLoginDate = this.currentChat.user2.lastLogin;
        }
        else {
          this.recipient = this.currentChat.user1;
          this.lastRecipientLoginDate = this.currentChat.user1.lastLogin;
        }
        this.loadMessages();
      }, (err: HttpErrorResponse) => {
        console.error(err)
        this.loading = false;
      } );
    }
  }

  loadMessages(): void {
    this.loading = true;
    this.conroller.get('/chats/messages/' + this.chatId + '?&userid=' + this.currentUser.userObject.id ).subscribe((res: HttpResponse<Message[]>) => {
      this.chatMessages = res.body.sort( this._compareMessages );
      this.recipientColor = this.getColorByUserId(this.recipient.id);
      this.loading = false;
    }, (err2: HttpErrorResponse) => {
      console.error(err2);
      this.loading = false;
    } );
  }

  addNewMessage(message: Message): void {
    this.chatMessages.unshift(message);
  }

  initMessagesCheck(): void {
    const _this = this;
    this.messagesSubscription = this.conroller.stompClient.subscribe(this.responseDestionation, function(sdkEvent) {
      let responseBody = JSON.parse(sdkEvent.body);
      console.log("Messages response : ", responseBody);
      let message: Message = responseBody.message;
      console.log(message);
      if (message != null) {
        _this.addNewMessage(message);
      }
    });
    this.checkMessages();
  }

  checkMessages(): void {
    if (this.chatId != null && this.chatId != "") {
      this.conroller._send({userId: this.currentUser.userObject.id, chatId: parseInt(this.chatId) }, this.responseSubscription);
    }
    else console.error("ChatId is null");
  }

  unsubscribeMessages(): void{
    if (this.messagesSubscription) {
      this.conroller._send({userId: this.currentUser.userObject.id, chatId: parseInt(this.chatId) }, this.rejectSubscriprion);
      this.messagesSubscription.unsubscribe();
    }
  }

  openInfo(): void {
    this.showInfo = true;
  }

  closeInfo(): void {
    this.showInfo = false;
  }

  getColorByUserId(appUserId: number): AppUserColor {
    return AppUserColorsArray[appUserId % AppUserColorsArray.length]
  }

  _compareMessages(a: Message, b: Message) {
    if (new Date(a.sentDate).getTime() > new Date(b.sentDate).getTime()) return -1;
    if (new Date(a.sentDate).getTime() < new Date(b.sentDate).getTime()) return 1;
    return 0;
  }

  // TODO: calculate max lenght of symbols in one line to add new row in textarea
  messageSizeCheck(): void {
    let lineBreaks = (this.messageText.match(/\n/g) || []).length + 1;
    if (lineBreaks <= this.maxRows && lineBreaks >= this.minRows ) this.messageRowsCount = lineBreaks;
  }

  sendMessage(): void {
    if (!this.loading || this.messageText.length > 1) {
      this.conroller.post('/chats/sendmessage', { userId: this.currentUser.userObject.id, chatId: parseInt(this.chatId), message: this.messageText } ).subscribe((res: HttpResponse<Message>) => {
        this.addNewMessage(res.body);
        this.messageRowsCount = 1;
        this.messageText = "";
      }, (err: HttpErrorResponse) => console.error(err) );
    }
  }

  onDeleteChatClicked(): void {
    if (window.confirm("Are you sure that you want to delete chat? This action cannot be undone.")) {
      // delete chat
      this.conroller.delete("/chats/delete/" + this.chatId).subscribe((res: any) => {
        console.log(res.body);
        if (res.body.ok) {
          console.log("navigate by body");
          this.router.navigate(['/chats']);
        }
        else {
          console.log("navigate by else");
          this.router.navigate(['/chats']);
        }
      }, (err: HttpErrorResponse) => {
        console.error(err);
      })
    }
    else {
      // cancel and do nothing
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

  closeChat(): void {
    this.router.navigate(['/chats']);
  }

  ngOnDestroy(): void {
    if (this.navigationSubscription) this.navigationSubscription.unsubscribe();
    this.unsubscribeMessages();
  }

}
