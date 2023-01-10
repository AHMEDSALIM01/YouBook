import { Component, OnInit } from '@angular/core';
import {Room} from "../../../models/room";
import {Hotel} from "../../../models/hotel";
import {HotelService} from "../../../services/hotel.service";
import {ActivatedRoute} from "@angular/router";
import {Reservation} from "../../../models/reservation";
import {Users} from "../../../models/users";

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css'],
})
export class RoomsComponent implements OnInit {
  public rooms!:Room[];
  public idHotel!:number;
  public reservationForm:boolean=false;
  public idRoom!:BigInt;
  public roomPrice!:number;
  public reservation!:Reservation;
  public user!:Users;
  public room!:Room;
  constructor(private hotelService:HotelService,private route:ActivatedRoute,) {
    // @ts-ignore
    this.idHotel = +this.route.snapshot.queryParamMap.get('hotel_id');
    this.reservation=new Reservation();
    this.room=new Room();
    this.user=new Users();
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
  reserver(idRoom:BigInt,roomPrice:number){
    this.idRoom=idRoom;
    this.roomPrice=roomPrice;
    this.reservationForm=true;
  }
  Annuler():void{
    window.location.reload();
  }
  confirmer(){
    console.log(this.reservation)
    this.reservation.user.id=BigInt(1);
  }
}
