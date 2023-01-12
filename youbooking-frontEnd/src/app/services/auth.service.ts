import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {Users} from "../models/users";
import {BehaviorSubject, catchError, Observable, of, tap, throwError} from "rxjs";
import {IdToken} from "../models/id-token";
import {Router} from "@angular/router";
import {JwtHelperService} from "@auth0/angular-jwt";
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLoggedInSubject = new BehaviorSubject<boolean>(false);
  isLoggedIn = this.isLoggedInSubject.asObservable();
  constructor(private http:HttpClient,private route:Router,private jwtHelper:JwtHelperService) { }

  signIn(user:Users):Observable<IdToken|HttpErrorResponse>{
    const params = new HttpParams()
      .set('email', user.email.toString()).set('password',user.password.toString());
    return this.http.post<IdToken>("http://localhost:8080/login",params).pipe(
      tap(response => {
        localStorage.setItem("access_token", response.accessToken.toString());
        localStorage.setItem("refresh_token", response.refreshToken.toString());
        this.isLoggedInSubject.next(true);
      }),
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
  isLogedIn(): boolean {
    const token = localStorage.getItem('access_token');
    const isLoggedIn = !this.jwtHelper.isTokenExpired(token);
    this.isLoggedInSubject.next(isLoggedIn);
    return isLoggedIn;
  }
  logout(){
    localStorage.removeItem("access_token");
    localStorage.removeItem("refresh_token");
    this.isLoggedInSubject.next(false);
    this.route.navigate(['/login']);
  }
}