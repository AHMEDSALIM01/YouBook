import { Component, OnInit } from '@angular/core';
import {ReservationService} from "../../../services/reservation.service";
import {Reservation} from "../../../models/reservation";
import {JwtHelperService} from "@auth0/angular-jwt";
import {Users} from "../../../models/users";
import {HttpErrorResponse} from "@angular/common/http";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-listreservation',
  templateUrl: './listreservation.component.html',
  styleUrls: ['./listreservation.component.css']
})
export class ListreservationComponent implements OnInit {
  private jwt:any;
  private token!:String;
  reservations!:Reservation[];
  user:Users;
  user_id!:number;
  constructor(private reservationService:ReservationService,private jwtHelper:JwtHelperService,private authService:AuthService,private router:Router) {
    this.user = new Users();
    if(!this.authService.isLogedIn()){
      this.router.navigate(['/login']);
    }
    if(localStorage!=null){
      // @ts-ignore
      if(localStorage.getItem("access_token").toString()!==null){
        // @ts-ignore
        this.token=localStorage.getItem("access_token").toString();
      }
    }

    if(this.token!=null){
      // @ts-ignore
      this.jwt = this.jwtHelper.decodeToken(this.token);
      this.user_id=this.jwt.user_id;
      console.log(this.user_id)
    }
  }

  ngOnInit(): void {
    this.getReservations();
  }

  getReservations(){
    this.user.id=this.user_id;
    this.reservationService.getUserReservation(this.user).subscribe((response)=>{
      if(response instanceof HttpErrorResponse){
        console.log(response.error)
      }else{
        console.log(response)
        this.reservations = response
      }
    })
  }
}
