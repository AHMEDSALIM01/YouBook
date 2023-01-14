import { Injectable } from '@angular/core';
import {Reservation} from "../models/reservation";
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {catchError, Observable, of, throwError} from "rxjs";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  public host:string="http://localhost:8080";
  constructor(private http:HttpClient,private jwt:JwtHelperService) { }

  // @ts-ignore
  addReservation(reservation:Reservation):Observable<Reservation|HttpErrorResponse>{
    const access_token = localStorage.getItem("access_token");
    if (access_token!=null){
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
}
