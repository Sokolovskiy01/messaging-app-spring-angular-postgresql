import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { ChatContentComponent } from './main/chat-content/chat-content.component';
import { ChatComponent } from './main/chat/chat.component';
import { SearchComponent } from './main/search/search.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { UserInfoComponent } from './pages/user-info/user-info.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'chats',
    component: ChatComponent,
    children: [
      { path: 'messages/:chatId', component: ChatContentComponent }
    ]
  },
  { path: 'search', component: SearchComponent },
  { path: 'user', component: UserInfoComponent },
  { path: '' , redirectTo: '/chats', pathMatch: 'full' }, // default route for page load
  { path: '**', redirectTo: '/chats', pathMatch: 'full'} // route for 404 page
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
