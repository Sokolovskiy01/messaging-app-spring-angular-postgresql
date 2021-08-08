import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterEvent } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy  {

  noChildRoute: boolean = true;
  navigationSubscription: Subscription;

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
    this.navigationSubscription = this.router.events.subscribe((e: any) => {
      if (e instanceof NavigationEnd) {
        this.checkRouteChange();
      }
    });
  }

  ngOnInit(): void {
    
  }

  checkRouteChange() : void {
    let activeChildren = this.activatedRoute.children.length;
    if (activeChildren != 0) this.noChildRoute = false;
    else this.noChildRoute = true;
  }

  ngOnDestroy(): void {
    this.navigationSubscription.unsubscribe();
  }

}
