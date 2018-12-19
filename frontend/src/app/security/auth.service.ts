import {Injectable} from '@angular/core';
import {User} from './user';
import * as moment from 'moment';
import {HttpClient} from '@angular/common/http';
import {map, shareReplay, tap} from 'rxjs/operators';
import * as jwt_decode from 'jwt-decode';
import {UserInfo} from './user-info';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {

  }

  login(username: string, password: string) {
    return this.http.post<User>('/auth/login', {username, password}, {observe: 'response'})
      .pipe(
        map(response => response.headers.get('Authorization')),
        map(bearerToken => bearerToken.replace('Bearer ', '')),
        tap(token => localStorage.setItem('token', token)),
        shareReplay(1)
      );
  }

  register(username: string, password: string) {
    return this.http.post<User>('/auth/sign-up', {username, password}, {observe: 'response'})
      .pipe(
        shareReplay(1)
      );
  }

  getUsernameFromBackend(userId: string) {
    return this.http.get<UserInfo>('/auth/user/' + userId);
  }

  logout() {
    localStorage.removeItem('token');
  }

  getUsername() {
    const token = localStorage.getItem('token');
    if (token) {
      return (<any>jwt_decode(token)).sub;
    }
  }

  getUserId() {
    const token = localStorage.getItem('token');
    if (token) {
      return (<any>jwt_decode(token)).userId;
    }
  }

  public isLoggedIn() {
    return !!localStorage.getItem('token');
  }
}
