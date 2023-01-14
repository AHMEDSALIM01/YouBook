import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  isLoggedIn = false;
  user_name ="";
  private isLoggedInSubscription!: Subscription;
  private userName!: Subscription;
  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
    this.isLoggedInSubscription = this.authService.isLoggedIn.subscribe(
      (isLoggedIn) => {
        this.isLoggedIn = isLoggedIn;
      }
    );
    this.userName = this.authService.userLogged.subscribe(
      (user) => {
        this.user_name = user.toString();
      }
    );
  }

  logout() {
    this.authService.logout();
  }



}

