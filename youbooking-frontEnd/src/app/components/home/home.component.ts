import { Component, OnInit } from '@angular/core';
import {Hotel} from "../../models/hotel";
import {FilterCriteria} from "../../models/filter-criteria";
import {HotelService} from "../../services/hotel.service";
import {catchError, finalize, of} from "rxjs";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public hotels:Hotel[]=[];
  public filterCriteria !: FilterCriteria;
  currentPage = 0;
  itemsPerPage = 10;
  hasNext = true;
  hasPrevious = false;
  loading = false;
  next="suivant";
  previous = "précedent"
  constructor(private hotelService:HotelService,private authService:AuthService) {
    this.filterCriteria = new FilterCriteria();
  }

  ngOnInit(): void{
    this.getHotels();
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
        if(data.size==0){
          this.hotels=[];
        }
        this.hotels = Array.from(data);
      }
    );
  }

  getHotels() {
    this.loading = true;
    this.hotelService.getAllHotels(this.currentPage)
      .pipe(
        catchError(error => {
          this.loading = false;
          console.error(error);
          return of(null);
        }),
        finalize(() => this.loading = false)
      )
      .subscribe(pageData => {
        if (pageData) {
          this.hotels = pageData.content;
          this.hasNext = pageData.totalElements > (this.currentPage * this.itemsPerPage);
        }
      });
  }

  paginate(pageNum: number) {
    this.currentPage = pageNum-1;
    this.hasPrevious = this.currentPage > 0;
    this.getHotels();
  }
}
