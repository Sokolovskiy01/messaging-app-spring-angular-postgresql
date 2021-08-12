import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AppUser } from './model/models';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ControllerService {

  backendUrl = "localhost:8080";

  constructor(private http: HttpClient) { }

  get(uri: string) {
    return this.http.get(this.backendUrl + uri, { observe: 'response' });
  }

  post(uri: string, payload: Object) {
    return this.http.post(this.backendUrl + uri, payload, { observe: 'response' });
  }

  put(uri: string, payload: Object ) {
    return this.http.put(this.backendUrl + uri, payload, { observe: 'response' })
  }

  delete(uri: string) {
    return this.http.delete(this.backendUrl + uri, { observe: 'response' });
  }

  userLogin(email: string, password: string) {
    return this.http.post<AppUser>(this.backendUrl + '/users/login', { email, password });
  }

  userLogout(userId: number) {
    return this.post('/logout', { userId });
  }

  /**
   * Get saved user login from cookies in backend
   */
  userGetLogin() { 
    return this.get('/savedlogin')
  }

  validateEmail(email: string) : boolean {
    if (/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(email)) return true;
    else return false;
  }


}
