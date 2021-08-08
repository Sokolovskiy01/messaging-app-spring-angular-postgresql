import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-chat-content',
  templateUrl: './chat-content.component.html',
  styleUrls: ['./chat-content.component.css']
})
export class ChatContentComponent implements OnInit {

  messageText: string = "";
  messageRowsCount: number = 1;
  maxRows: number = 5;
  minRows: number = 1;

  constructor() { }

  ngOnInit(): void {
  }

  messageSizeCheck(): void {
    let lineBreaks = (this.messageText.match(/\n/g) || []).length + 1;
    if (lineBreaks <= this.maxRows && lineBreaks >= this.minRows ) this.messageRowsCount = lineBreaks;
  }

}
