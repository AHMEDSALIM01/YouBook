import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Hotel} from "../models/hotel";
import {catchError, Observable, of, throwError} from "rxjs";
import {FilterCriteria} from "../models/filter-criteria";
import {Page} from "../models/page";
import {Users} from "../models/users";

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  public host:string="http://localhost:8080";
  constructor(private http: HttpClient) {
  }
  public getHotels():Observable<Set<Hotel>>{

    return this.http.get<Set<Hotel>>(this.host+"/hotel/");
  }
  getAllHotels(pageNumber: number): Observable<Page<Hotel>> {
    const params = new HttpParams()
      .set('page', pageNumber);
    return this.http.get<Page<Hotel>>(this.host+"/hotel/hotels",{params});
  }
  public getHotelById(id:BigInt):Observable<Hotel>{
    return this.http.get<Hotel>(this.host+"/hotel/"+id);
  }
  filter(criteria: FilterCriteria):Observable<Set<Hotel>>{
    return this.http.post<Set<Hotel>>(this.host+"/hotel/filter", criteria);
  }
  public getHotelByOwner(owner:Users):Observable<Hotel[] | HttpErrorResponse>{
    return this.http.post<Hotel[]>(this.host+"/hotel/hotelOwner",owner).pipe(
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
  public updateHotel(hotel:Hotel):Observable<Hotel | HttpErrorResponse>{
    return this.http.post<Hotel>(this.host+"/hotel/updateHotel",hotel).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.error("Error: ", error.error);
          return of(error);
        }else if(error.status === 403){
          return of(error);
        } else {
          return throwError(error);
        }
      })
    );
  }
  public deleteHotel(hotel:Hotel):Observable<Hotel | HttpErrorResponse>{
    return this.http.delete<Hotel>(this.host+"/hotel/deleteHotel",{body:hotel}).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          console.error("Error: ", error.error);
          return of(error);
        }else if(error.status === 403){
          return of(error);
        } else {
          return throwError(error);
        }
      })
    );
  }
}
