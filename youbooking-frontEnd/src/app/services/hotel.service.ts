import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Hotel} from "../models/hotel";
import {Observable} from "rxjs";
import {FilterCriteria} from "../models/filter-criteria";
import {Page} from "../models/page";

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
}
