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
import {Reservation} from "../models/reservation";
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLoggedInSubject = new BehaviorSubject<boolean>(false);
  isLoggedIn = this.isLoggedInSubject.asObservable();
  userNameSubject = new BehaviorSubject<String>("");
  userLogged = this.userNameSubject.asObservable();
  private refreshTokenSubject = new BehaviorSubject<any>(null);
  //refreshToken$ = this.refreshTokenSubject.asObservable();
  private refreshTokenIntervalId: any;
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
    this.userNameSubject.next("")
    this.stopRefreshToken();
    this.route.navigate(['/login']);
  }

  startRefreshTokenInterval() {
    // Start the refresh token interval with a delay of 2 minutes
    this.refreshTokenSubject.pipe(
      startWith(null),
      switchMap(() => timer(1 * 60 * 1000)),
      switchMap(() => this.refreshToken()),
      retry()
    ).subscribe();
  }

  stopRefreshToken(): void {
    clearInterval(this.refreshTokenIntervalId);
  }

  refreshToken(): Observable<boolean | IdToken> {
    const refreshToken = localStorage.getItem("refresh_token");
    if (!this.jwtHelper.isTokenExpired(refreshToken)) {
      const headers = new HttpHeaders().set("Authorization", "Bearer " + refreshToken);
      return this.http.get<IdToken>("http://localhost:8080/refreshToken", { headers }).pipe(
        tap(response => {
          if (response.accessToken && response.refreshToken) {
            localStorage.setItem("access_token", response.accessToken.toString());
            localStorage.setItem("refresh_token", response.refreshToken.toString());
            this.isLoggedInSubject.next(true);
            // Emit a value to start the refresh token interval
            this.refreshTokenSubject.next(true);
          } else {
            this.logout();
          }
        }),
        catchError((error: HttpErrorResponse) => {
          if (error.status === 400) {
            console.error("Error: ", error.error);
            return of(false);
          } else {
            return throwError(error);
          }
        })
      );
    } else {
      this.logout();
      return of(false);
    }
  }
  getAccessToken(): string {
    // @ts-ignore
    return localStorage.getItem("access_token");
  }
  getRefreshToken(): string {
    // @ts-ignore
    return localStorage.getItem("refresh_token");
  }
  redirectToLogin(){
    this.route.navigate(['/login']);
  }
}
