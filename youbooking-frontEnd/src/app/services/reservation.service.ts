import { Injectable } from '@angular/core';
import {Reservation} from "../models/reservation";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, catchError, Observable, of, tap, throwError} from "rxjs";
import {JwtHelperService} from "@auth0/angular-jwt";
import {Users} from "../models/users";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  public host:string="http://localhost:8080";
  constructor(private http:HttpClient,private jwt:JwtHelperService,private authService:AuthService) {

  }

  // @ts-ignore
  addReservation(reservation:Reservation):Observable<Reservation|HttpErrorResponse>{
    const access_token = localStorage.getItem("access_token");
    if (access_token!=null && !this.jwt.isTokenExpired(access_token)){
        const headers = new HttpHeaders().set("Authorization","Bearer "+access_token);
        return this.http.post<Reservation>(this.host + "/reservation/addReservation", reservation,{headers}).pipe(
          catchError((error: HttpErrorResponse) => {
            if (error.status === 401) {
              console.error("Error: ", error.error);
              return of(error);
            }else {
            if (error.status === 403){
                console.log(error)
              }
              return throwError(error);
            }
          })
        );
    }
  }
  // @ts-ignore
  getUserReservation(user:Users):Observable<Reservation[]|HttpErrorResponse>{
    const access_token = localStorage.getItem("access_token");
    if (access_token!=null && !this.jwt.isTokenExpired(access_token)){
      const headers = new HttpHeaders().set("Authorization","Bearer "+access_token);
      return this.http.post<Reservation[]>(this.host + "/reservation/reservations",user,{headers}).pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401) {
            console.error("Error: ", error.error);
            return of(error);
          }else {
            if (error.status === 403){
              console.log(error)
              return of(error);
            }
            return throwError(error);
          }
        })
      );
    }
  }

  // @ts-ignore
  annulerReservation(reservation:Reservation):Observable<Reservation | HttpErrorResponse>{
    if(this.authService.isLogedIn()){
      console.log(reservation)
      return this.http.put<Reservation>(this.host + "/reservation/cancelReservation",reservation).pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401) {
            console.error("Error: ", error.error);
            return of(error);
          }else {
            if (error.status === 400){
              console.log(error)
              return of(error);
            }
            return throwError(error);
          }
        })
      );
    }
  }
}
