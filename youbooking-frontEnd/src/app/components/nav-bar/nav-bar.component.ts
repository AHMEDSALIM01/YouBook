import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Subscription} from "rxjs";
import * as http from "http";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  isLoggedIn = false;
  user_name ="";
  private role!:String;
  private isLoggedInSubscription!: Subscription;
  private userName!: Subscription;
  private isClientSubscription!:Subscription;
  private isOwnerSubscription!:Subscription;
  isClient = false;
  isOwner = false;
  url:any;
  constructor(private authService: AuthService,private route:ActivatedRoute) {
    this.isLoggedInSubscription = this.authService.isLoggedIn.subscribe(
      (isLoggedIn) => {
        this.isLoggedIn = isLoggedIn;
      }
    );
    this.isOwnerSubscription = this.authService.isOwner.subscribe(
      (response)=>{
        this.isOwner=response;
      }
    );
    this.isClientSubscription = this.authService.isClient.subscribe(
      (response)=>{
        this.isClient=response;
      }
    )
    this.userName = this.authService.userLogged.subscribe(
      (user) => {
        this.user_name = user.toString();
      }
    );

  }

  ngOnInit(): void {
  }

  logout() {
    this.authService.logout();
  }

}

