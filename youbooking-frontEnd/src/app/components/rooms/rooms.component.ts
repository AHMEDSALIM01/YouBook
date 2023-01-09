import { Component, OnInit } from '@angular/core';
import {HotelComponent} from "../hotel/hotel.component";
import {Room} from "../../models/room";
import {Hotel} from "../../models/hotel";
import {HotelService} from "../../services/hotel.service";
import {HttpClient, HttpParams} from "@angular/common/http";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.css'],
})
export class RoomsComponent implements OnInit {
  public rooms!:Room[];
  public hotel!:Hotel;
  public idHotel!:number;
  constructor(private hotelService:HotelService,private route:ActivatedRoute) {
  }

  ngOnInit(): void {
    // @ts-ignore
    this.idHotel = +this.route.snapshot.queryParamMap.get('id');
    //this.getAllRooms();
  }
  /*public getAllRooms(){
    this.hotelService.getHotelById(BigInt(this.idHotel)).subscribe(
      (response:Hotel)=>{
        this.rooms = response.rooms;
      }
    )
  }*/
}
