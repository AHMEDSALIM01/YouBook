import { Injectable } from '@angular/core';
import {Reservation} from "../models/reservation";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, of, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  public host:string="http://localhost:8080";
  constructor(private http:HttpClient) { }

  addReservation(reservation:Reservation):Observable<Reservation|HttpErrorResponse>{
    return this.http.post<Reservation>(this.host + "/reservation/addReservation", reservation).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.error("Error: ", error.error);
          return of(error);
        } else {
          return throwError(error);
        }
      })
    );
  }
}
