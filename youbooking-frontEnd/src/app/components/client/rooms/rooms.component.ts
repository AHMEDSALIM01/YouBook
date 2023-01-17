import { Component, OnInit } from '@angular/core';
import {Room} from "../../../models/room";
import {Hotel} from "../../../models/hotel";
import {HotelService} from "../../../services/hotel.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Reservation} from "../../../models/reservation";
import {Users} from "../../../models/users";
import {ReservationService} from "../../../services/reservation.service";
import {catchError, map, throwError} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {AuthService} from "../../../services/auth.service";
import {JwtHelperService} from "@auth0/angular-jwt";

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css'],
})
export class RoomsComponent implements OnInit {
  public rooms!:Room[];
  public idHotel!:number;
  public reservationForm:boolean=false;
  public idRoom!:number;
  public roomPrice!:number;
  public reservation!:Reservation;
  public user!:Users;
  public errorMessage:String='';
  public successMessage:String='';
  public now!:String;
  private jwt:any;
  private token!:String;
  constructor(private hotelService:HotelService,private route:ActivatedRoute,private reservationService:ReservationService,private router:Router,private authService:AuthService,private jwtHelper:JwtHelperService) {
    // @ts-ignore
    this.idHotel = +this.route.snapshot.queryParamMap.get('hotel_id');
    this.reservation=new Reservation();
    this.reservation.room=new Room();
    this.reservation.user=new Users();
  }

  ngOnInit(): void {
    this.getAllRooms();
    this.minDate();
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
    }
  }

  public getAllRooms(){
    this.hotelService.getHotelById(BigInt(this.idHotel)).subscribe(
      (response:Hotel)=>{
        this.rooms = response.rooms;
      }
    )
  }
  reserver(idRoom:number,roomPrice:number){
    this.idRoom=idRoom;
    this.roomPrice=roomPrice;
    this.reservationForm=true;
  }
  Annuler():void{
    this.errorMessage ="";
    this.successMessage="";
    window.location.reload();
  }
  confirmer(){
    if (this.authService.isLogedIn() && this.jwt.enabled){
      this.reservation.user.id=this.jwt.user_id;
      this.reservation.room.id=this.idRoom;
      if(this.reservation.startDate==null ||this.reservation.endDate==null){
        this.errorMessage = this.reservation.startDate == null ? "la date de début ne doit pas être vide" :
          (this.reservation.endDate == null ? "la date de fin ne doit pas être vide" : "");
      }else {
        let startDate = new Date(this.reservation.startDate);
        let endDate = new Date(this.reservation.endDate);
        if (endDate.getTime()-startDate.getTime()<0){
          this.errorMessage="la date de fin ne doit pas être avant la date de début";
        }else {
          let difference = (endDate.getTime()-startDate.getTime())
          if(difference==0){
            this.reservation.totalPrice=this.roomPrice;
          }else {
            this.reservation.totalPrice=difference/86400000 * this.roomPrice;
          }
          this.reservationService.addReservation(this.reservation)
            .subscribe(
              (response) => {
                if(response instanceof HttpErrorResponse){
                  this.errorMessage = response.error
                }else{
                  this.reservationForm=false;
                  this.successMessage = "reservation ajouté avec succès";
                  this.errorMessage="";
                  setTimeout(()=>{
                    this.successMessage ='';
                    window.location.reload();
                  },2500);
                }
              },
              (error) => {
                this.errorMessage = error;
              }
            );
        }
      }
    }else {
      this.router.navigate(['/login']);
    }

  }
  minDate(){
    var today = new Date();
    var dd = today.getDate().toString();
    var mm = (today.getMonth()+1).toString(); //January is 0!
    var yyyy = today.getFullYear().toString();
    if(Number(dd)<10){
      dd='0'+dd;
    }
    if(Number(mm)<10){
      mm='0'+mm
    }

    let todayUpdated = yyyy+'-'+mm+'-'+dd;
    this.now=todayUpdated;
  }
}
