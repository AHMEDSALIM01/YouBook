import { Component } from '@angular/core';
import {Hotel} from "./models/hotel";
import {Room} from "./models/room";
import {HotelService} from "./services/hotel.service";
import {ActivatedRoute} from "@angular/router";
import { FilterCriteria } from './models/filter-criteria';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'youbooking-frontEnd';
  public hotels!:Hotel[];
  public rooms!:Room[];
  public idHotel!:BigInt;
  public showRoom=false;
  public showHotel=true;
  public filterCriteria !: FilterCriteria;
  constructor(private hotelService:HotelService) {
    this.filterCriteria = new FilterCriteria();
  }

  ngOnInit(): void{
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
    this.hotelService.getHotelById(this.idHotel).subscribe(
      (response:Hotel)=>{
        this.rooms = response.rooms;
      }
    )
  }
  onSubmit(){
    if((this.filterCriteria.prixMin!=null && this.filterCriteria.prixMax==null) || (this.filterCriteria.prixMin==null && this.filterCriteria.prixMax!=null)){
      console.log("il faut donner prixMin et prixMax");
      return;
    }
   this.hotelService.filter(this.filterCriteria).subscribe(
      (data:Set<Hotel>)=>{
        console.log(data)
        if(data.size==0){
          this.hotels=[];
        }
        this.hotels = Array.from(data);
      }
    );
    console.log(this.filterCriteria)
  }
}
