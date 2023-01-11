import { Component, OnInit } from '@angular/core';
import {Hotel} from "../../models/hotel";
import {FilterCriteria} from "../../models/filter-criteria";
import {HotelService} from "../../services/hotel.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public hotels!:Hotel[];
  public filterCriteria !: FilterCriteria;
  constructor(private hotelService:HotelService ) {
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
