import {Component} from '@angular/core';
import * as moment from 'moment';
import {AuthService} from './security/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';

  constructor(private authService: AuthService,
              private router: Router) {
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['login']);
  }

  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  getLoggedInUsername() {
    return this.authService.getUsername();
  }
}
