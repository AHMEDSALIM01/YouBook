import { Component, OnInit } from '@angular/core';
import {Room} from "../../../models/room";
import {Hotel} from "../../../models/hotel";
import {HotelService} from "../../../services/hotel.service";
import {ActivatedRoute} from "@angular/router";
import {Reservation} from "../../../models/reservation";
import {Users} from "../../../models/users";
import {ReservationService} from "../../../services/reservation.service";
import {catchError, map, throwError} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

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
  constructor(private hotelService:HotelService,private route:ActivatedRoute,private reservationService:ReservationService) {
    // @ts-ignore
    this.idHotel = +this.route.snapshot.queryParamMap.get('hotel_id');
    this.reservation=new Reservation();
    this.reservation.room=new Room();
    this.reservation.user=new Users();
  }

  ngOnInit(): void {

    this.getAllRooms();
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
    window.location.reload();
  }
  confirmer(){
    this.reservation.user.id=25;
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
        console.log(this.reservation.totalPrice)
        this.reservationService.addReservation(this.reservation)
          .subscribe(
            (response) => {
              if(response instanceof HttpErrorResponse){
                this.errorMessage = response.error
              }else{
                this.reservationForm=false;
                this.successMessage = "reservation ajouté avec succès";
                setTimeout(()=>{
                  this.successMessage ='';
                  window.location.reload();
                },4000);
              }
            },
            (error) => {
              this.errorMessage = error;
            }
          );
      }
    }
  }
}
