import { Component } from '@angular/core';
import {AuthService} from "./services/auth.service";
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'youbooking-frontEnd';
  isLogedIn=false;
  user_name!:String;
  constructor(private authService:AuthService) {

  }

  ngOnInit(): void {
    this.isLogedIn=this.authService.isLogedIn();
    //this.authService.startRefreshTokenInterval();
  }
}
