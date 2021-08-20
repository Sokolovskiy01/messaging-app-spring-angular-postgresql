import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService, CurrentAppUser } from 'src/app/auth.service';
import { ControllerService } from 'src/app/controller.service';
import { Chat, Message } from 'src/app/model/models';

@Component({
  selector: 'app-chat-content',
  templateUrl: './chat-content.component.html',
  styleUrls: ['./chat-content.component.css']
})
export class ChatContentComponent implements OnInit, OnDestroy  {

  navigationSubscription: Subscription;

  /* Vataibles for messageField */
  messageText: string = "";
  messageRowsCount: number = 1;
  maxRows: number = 5;
  minRows: number = 1;

  chatId: string;

  currentChat: Chat;
  chatMessages: Message[] = [];

  constructor(private conroller: ControllerService, private auth: AuthService, private router: Router, private route: ActivatedRoute, public currentUser: CurrentAppUser) {
    this.route.params.subscribe((params: Params) => {
      this.chatId = params.chatId;
    });
    this.navigationSubscription = this.router.events.subscribe((event: any) => {
      if (event instanceof NavigationEnd) {
        this.loadMessages();
      }
    })
  }

  ngOnInit(): void {
    console.log(this.route.parent);
  }

  loadMessages() {
    if (this.chatId) {
      this.conroller.get('/chats/get/' + this.chatId).subscribe((res: HttpResponse<Chat>) => {
        this.currentChat = res.body;
        this.conroller.get('/chats/messages/' + this.chatId).subscribe((res2: HttpResponse<Message[]>) => {
          this.chatMessages = res2.body;
        }, (err2: HttpErrorResponse) => console.error(err2) );
      }, (err: HttpErrorResponse) => console.error(err) );
    }
  }

  messageSizeCheck(): void {
    let lineBreaks = (this.messageText.match(/\n/g) || []).length + 1;
    if (lineBreaks <= this.maxRows && lineBreaks >= this.minRows ) this.messageRowsCount = lineBreaks;
  }

  ngOnDestroy(): void {
    if (this.navigationSubscription) this.navigationSubscription.unsubscribe();
  }

}
