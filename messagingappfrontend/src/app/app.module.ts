import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { SearchComponent } from './main/search/search.component';
import { ChatComponent } from './main/chat/chat.component';
import { LeftBarNavigationComponent } from './navigation/left-bar-navigation/left-bar-navigation.component';
import { UserInfoComponent } from './pages/user-info/user-info.component';
import { ChatContentComponent } from './main/chat-content/chat-content.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    SearchComponent,
    ChatComponent,
    LeftBarNavigationComponent,
    UserInfoComponent,
    ChatContentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
