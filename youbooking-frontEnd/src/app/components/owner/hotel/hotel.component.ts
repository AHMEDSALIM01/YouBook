import { Component, OnInit } from '@angular/core';
import {HotelService} from "../../../services/hotel.service";
import {Hotel} from "../../../models/hotel";
import {Room} from "../../../models/room";
import {ActivatedRoute, Route, Router} from "@angular/router";
import {JwtHelperService} from "@auth0/angular-jwt";
import {AuthService} from "../../../services/auth.service";
import {Users} from "../../../models/users";
import {HttpErrorResponse} from "@angular/common/http";
import {Role} from "../../../models/role";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {of} from "rxjs";
import {Collection} from "ngx-pagination";

@Component({
  selector: 'app-hotel',
  templateUrl: './hotel.component.html',
  styleUrls: ['./hotel.component.css']
})
export class HotelComponent implements OnInit {
  hotels!:Hotel[];
  hotel:Hotel;
  successMessage:String="";
  errorMessage:String="";
  show!:any[];
  owner:Users;
  page=1;
  constructor(private hotelService:HotelService,private route:Router,private jwtHelper:JwtHelperService,private authService:AuthService) {
    this.owner = new Users();
    this.hotel = new Hotel();
  }

  ngOnInit(): void{
   this.getMyHotels();
  }
  getMyHotels(){
    if(this.authService.isLogedIn() && this.authService.getUserRole()=="OWNER"){
      const token = this.authService.getAccessToken();
      const owner_id = this.jwtHelper.decodeToken(token).user_id;
      this.owner.id=owner_id;
      this.hotelService.getHotelByOwner(this.owner).subscribe((response)=>{
        if (response instanceof HttpErrorResponse){
          console.log(response.error);
        }else {
          this.hotels = response;
          if(this.hotels.length) this.show = Array(this.hotels.length).fill(false);
          console.log(this.hotels);
        }
      })
    }else{
      this.route.navigate(['/login']);
    }
  }
  back(index:number){
    this.show[index] = false;
  }
  showInput(index: number) {
    for(let i = 0; i < this.show.length; i++) {
      if(i !== index) {
        this.show[i] = false;
      }
    }
    this.show[index] = true;
  }

  updateHotel(i:number){
    if(this.authService.isLogedIn() && this.authService.getUserRole()=="OWNER"){
      this.hotels[i].owner.id = this.owner.id;
      this.hotels[i].rooms=[];
      this.hotelService.updateHotel(this.hotels[i]).subscribe((response)=>{
        if (response instanceof HttpErrorResponse){
          this.errorMessage = response.error;
        }else {
          this.errorMessage="";
          this.successMessage ="L'hotel est modifié avec succès";
          setTimeout(()=>{
            this.successMessage ="";
          },1500)
          this.show[i]=false;
        }
      });
    }else{
      this.route.navigate(['/login']);
    }
  }
  deleteHotel(i:number){
    if(this.authService.isLogedIn() && this.authService.getUserRole()=="OWNER"){
      //this.hotels[i].owner.id = this.owner.id;
      this.hotels[i].rooms=[];
      this.hotelService.deleteHotel(this.hotels[i]).subscribe((response)=>{
        if (response instanceof HttpErrorResponse){
          this.errorMessage = response.error;
        }else {
          this.errorMessage="";
          this.successMessage ="L'hotel est modifié avec succès";
          setTimeout(()=>{
            this.successMessage ="";
          },1500);
        }
      });
    }else{
      this.route.navigate(['/login']);
    }
  }
}
