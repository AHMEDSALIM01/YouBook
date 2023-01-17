import {Component, OnInit} from '@angular/core';
import {ReservationService} from "../../../services/reservation.service";
import {Reservation} from "../../../models/reservation";
import {JwtHelperService} from "@auth0/angular-jwt";
import {Users} from "../../../models/users";
import {HttpErrorResponse} from "@angular/common/http";
import {AuthService} from "../../../services/auth.service";
import {Router} from "@angular/router";
import {StatusReservation} from "../../../models/status-reservation";
import {Room} from "../../../models/room";

@Component({
  selector: 'app-listreservation',
  templateUrl: './listreservation.component.html',
  styleUrls: ['./listreservation.component.css']
})
export class ListreservationComponent implements OnInit {
  private jwt:any;
  private token!:String;
  reservations!:Reservation[];
  reservationSub:Reservation;
  updateForm:boolean = false;
  user:Users;
  room:Room;
  user_id!:number;
  show!:any[]
  errorMessage:String="";
  successMessage:String="";
  constructor(private reservationService:ReservationService,private jwtHelper:JwtHelperService,private authService:AuthService,private router:Router) {
    this.user = new Users();
    this.room = new Room();
    this.reservationSub=new Reservation();
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
        if(this.reservations.length) this.show = Array(this.reservations.length).fill(false);
      }
    })
  }
  annulerReservation(index:number){
      let reservation = this.reservations[index];
      let status = this.reservations[index].status;
      if(status.toString() === "Annulée"){
        this.errorMessage ="Réservation déja annulée";
        setTimeout(()=>{
          this.errorMessage ="";
        },2500)
      }else if(status.toString() === "En_cours"){
        this.room.id=reservation.room.id;
        this.user.id=reservation.user.id;
        reservation.user=this.user;
        reservation.room=this.room;
        this.reservationService.annulerReservation(reservation).subscribe((response)=>{
          if(response instanceof HttpErrorResponse){
            this.errorMessage = response.error;
          }else {
            this.successMessage = "Réservation annulée avec succès";
            setTimeout(()=>{
              this.successMessage = "";
              window.location.reload();
            },2500);
          }
        })
      }else{
        this.errorMessage ="Réservation déja annulée";
        setTimeout(()=>{
          this.errorMessage ="";
        },2500);
      }
  }

  updateReservation(index:number,reservation:Reservation){
    if (this.authService.isLogedIn() && this.jwt.enabled){
      if(reservation.startDate==null || reservation.endDate==null){
        this.errorMessage = reservation.startDate == null ? "la date de début ne doit pas être vide" :
          (reservation.endDate == null ? "la date de fin ne doit pas être vide" : "");
      }else {
        let startDate = new Date(reservation.startDate);
        let endDate = new Date(reservation.endDate);
        if(startDate.getTime()-Date.now()<0 || endDate.getTime()-Date.now()<0){
          this.errorMessage = startDate.getTime()-Date.now()<0 ? "la date de début ne doit pas être avant la date d'aujourd'huit" :
            (endDate.getTime()-Date.now()<0 ? "la date de fin ne doit pas être avant la date d'aujourd'huit" : "");
        }
        else if(endDate.getTime()-startDate.getTime()<0){
          this.errorMessage="la date de fin ne doit pas être avant la date de début";
        }else {
          let difference = (endDate.getTime()-startDate.getTime())
          if(difference==0){
            reservation.totalPrice=reservation.room.price;
          }else {
            reservation.totalPrice=difference/86400000 * reservation.room.price;
          }
          this.room.id=reservation.room.id;
          this.user.id=reservation.user.id;
          this.room.number=reservation.room.number;
          reservation.user=this.user;
          reservation.room=this.room;
          this.reservationService.updateReservation(this.reservations[index])
            .subscribe(
              (response) => {
                if(response instanceof HttpErrorResponse){
                  this.errorMessage = response.error
                }else{
                  this.show[index]=false;
                  this.successMessage = "reservation modifiée avec succès";
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
}
