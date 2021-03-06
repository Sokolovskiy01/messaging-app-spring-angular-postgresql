import { Injectable } from '@angular/core';
import { HttpClient, HttpEvent, HttpHeaders, HttpRequest, HttpResponse } from '@angular/common/http';
import { AppUser } from './model/models';
import { Observable } from 'rxjs';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class ControllerService {

  readonly backendWebsocketEndPoint = "http://localhost:8080/ws";
  readonly backendUrl = "http://localhost:8080";

  stompClient: any;
  stompSessionId: string;
  //isStompConnected: boolean = false;

  constructor(private http: HttpClient) {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.backendWebsocketEndPoint);
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = () => {}; // disable debug messages in console
    this._connect();
  }

  get(uri: string) {
    return this.http.get(this.backendUrl + uri, { observe: 'response' });
  }

  post(uri: string, payload: Object) {
    return this.http.post(this.backendUrl + uri, payload, { observe: 'response' });
  }

  put(uri: string, payload: Object ) {
    return this.http.put(this.backendUrl + uri, payload, { observe: 'response' });
  }

  delete(uri: string) {
    return this.http.delete(this.backendUrl + uri, { observe: 'response' });
  }

  userLogin(email: string, password: string) {
    return this.http.post<AppUser>(this.backendUrl + '/users/login', { email: email, password: password }, { observe: 'response' });
  }

  userCreate(newAppUser: AppUser) {
    return this.post(this.backendUrl + '/users/register', newAppUser);
  }

  userLogout(userId: number) {
    return this.post('/logout', { userId });
  }

  /**
   * Get saved user login from cookies in backend
   */
  userGetLogin() { 
    let credentials = localStorage.getItem('token');
    if (credentials != null) {
      let separateCredentials = atob(credentials).split(':');
      let email = separateCredentials[0];
      let password = separateCredentials[1];
      return this.userLogin(email, password).toPromise();
    }
    else {
      return new Promise((resolve, reject) => {
        reject('No data stored');
      });
    }
  }

  validateEmail(email: string) : boolean {
    if (/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(email)) return true;
    else return false;
  }

  uploadFile(file: File, folder: string): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('folder', folder); // should be "foldername/"
    const req = new HttpRequest('POST', this.backendUrl + '/files/upload', formData, {reportProgress: true, responseType: 'json'});
    return this.http.request(req);
  }

  uploadAvatar(file: File, userid: number): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('folder', 'avatars/');
    formData.append('userid', userid.toString());
    const req = new HttpRequest('POST', this.backendUrl + '/files/avatarUpload', formData, {reportProgress: true, responseType: 'json'});
    return this.http.request(req);
  }

  _connect(): void {
    const _this = this;
    _this.stompClient.connect({}, function (frame) {
      _this.setSocketSessionId(_this.stompClient.ws._transport.url);
      //_this.isStompConnected = true;
      //_this.stompClient.reconnect_delay = 2000;
    }, this.errorCallBack);
  }

  _disconnect(): void {
    if (this.stompClient !== null) this.stompClient.disconnect();
    console.log("Stomp disconnected");
  }

  _send(message, destination) {
    this.stompClient.send('/app' + destination, {}, JSON.stringify(message));
    //if (this.isStompConnected) 
    //else console.error("Stomp client is not connected");
  }

  errorCallBack(error) {
    //this.isStompConnected = false;
    console.log("errorCallBack -> " + error);
    // try to reconnect
    const _this = this;
    setTimeout(() => { _this._connect(); }, 5000);
  }

  setSocketSessionId(url: string): void {
    let c = url.split('/');
    this.stompSessionId = c[c.length - 2];
  }

  getSocketSessionId(): string {
    return this.stompSessionId;
  }

}
