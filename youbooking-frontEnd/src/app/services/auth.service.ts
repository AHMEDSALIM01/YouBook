import { Injectable } from '@angular/core';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
  HttpParams,
} from "@angular/common/http";
import {Users} from "../models/users";
import {BehaviorSubject, catchError, Observable, of, retry, startWith, switchMap, tap, throwError, timer} from "rxjs";
import {IdToken} from "../models/id-token";
import {Router} from "@angular/router";
import {JwtHelperService} from "@auth0/angular-jwt";
import {Collection} from "ngx-pagination";
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLoggedInSubject = new BehaviorSubject<boolean>(false);
  isLoggedIn = this.isLoggedInSubject.asObservable();
  userNameSubject = new BehaviorSubject<String>("");
  isClientSubjec = new BehaviorSubject<boolean>(false);
  isClient = this.isClientSubjec.asObservable();
  isOwnerSubjec = new BehaviorSubject<boolean>(false);
  isOwner = this.isOwnerSubjec.asObservable();
  userLogged = this.userNameSubject.asObservable();
  role!:String;
  endPoint!:String;
  constructor(private http:HttpClient,private route:Router,private jwtHelper:JwtHelperService) { }

  signIn(user:Users):Observable<IdToken|HttpErrorResponse>{
    const params = new HttpParams()
      .set('email', user.email.toString()).set('password',user.password.toString());
    return this.http.post<IdToken>("http://localhost:8080/login",params).pipe(
      tap(response => {
        localStorage.setItem("access_token", response.accessToken.toString());
        localStorage.setItem("refresh_token", response.refreshToken.toString());
        this.isLoggedInSubject.next(true);
        const accessToken = localStorage.getItem("access_token");
        // @ts-ignore
        let jwt = this.jwtHelper.decodeToken(accessToken.toString());
        this.userNameSubject.next(jwt.user_name);
        if(this.getUserRole()=="OWNER"){
          this.isOwnerSubjec.next(true);
        }else if(this.getUserRole()=="CLIENT"){
          this.isClientSubjec.next(true);
        }
        //this.startRefreshTokenInterval();
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

  signUp(user:Users):Observable<Users | HttpErrorResponse>{
    this.endPoint = this.role=="CLIENT" ? "signUpClient" : (this.role == "OWNER" ? "signUpOwner" : "");
    return this.http.post<Users>("http://localhost:8080/"+this.endPoint,user).pipe(
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
    if(token!=null){
      const user = this.jwtHelper.decodeToken(token).user_name
      this.userNameSubject.next(user)
    }
   const isLoggedIn = !this.jwtHelper.isTokenExpired(token);
    this.isLoggedInSubject.next(isLoggedIn);
    return isLoggedIn;
  }


  logout(){
    localStorage.removeItem("access_token");
    localStorage.removeItem("refresh_token");
    this.isLoggedInSubject.next(false);
    this.userNameSubject.next("");
    this.isClientSubjec.next(false);
    this.isClientSubjec.next(false);
    //this.stopRefreshToken();
    this.route.navigate(['/login']);
  }

  getAccessToken(): string {
    // @ts-ignore
    return localStorage.getItem("access_token");
  }
  getUserRole(): any {
    // @ts-ignore
    const roles = this.jwtHelper.decodeToken(localStorage.getItem("access_token"));
    if (roles){
      return roles.roles[0];
    }else {
      return "";
    }
  }
  redirectToLogin(){
    this.route.navigate(['/login']);
  }
}
