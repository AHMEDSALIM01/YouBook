import { Component, OnInit } from '@angular/core';
import {HotelService} from "../../services/hotel.service";
import {Hotel} from "../../models/hotel";
import {Room} from "../../models/room";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-hotel',
  templateUrl: './hotel.component.html',
  styleUrls: ['./hotel.component.css']
})
export class HotelComponent implements OnInit {
  public hotels!:Hotel[];
  public rooms!:Room[];
  public idHotel!:number;
  public showRoom=false;
  public showHotel=true;
  constructor(private hotelService:HotelService,private route:ActivatedRoute) { }

  ngOnInit(): void{
    // @ts-ignore
    this.idHotel = +this.route.snapshot.queryParamMap.get('id');
    this.getAllHotels();
  }

  public getAllHotels(){
    this.hotelService.getHotels().subscribe(
      (response:Set<Hotel>)=>{
        this.hotels = Array.from(response);
      }
    )
  }
  public onGetRoom(){
      this.getAllRooms();
  }
  public getAllRooms(){
    this.hotelService.getHotelById(BigInt(this.idHotel)).subscribe(
      (response:Hotel)=>{
        this.rooms = response.rooms;
      }
    )
  }
  public addReserVation(){

  }
}
