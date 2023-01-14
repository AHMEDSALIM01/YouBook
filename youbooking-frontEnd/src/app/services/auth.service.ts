import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from "@angular/common/http";
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
  userNameSubject = new BehaviorSubject<String>("");
  userLogged = this.userNameSubject.asObservable();
  private refreshTokenInterval:any;
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
        this.startRefreshTokenInterval();
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
    this.stopRefreshTokenInterval();
    this.route.navigate(['/login']);
  }

  startRefreshTokenInterval(){
    this.refreshTokenInterval = setInterval(() => {
      this.refreshToken();
    }, 100000);
  }

  stopRefreshTokenInterval(){
    clearInterval(this.refreshTokenInterval);
  }

  refreshToken(){
      const token = localStorage.getItem("access_token");
      const refreshToken = localStorage.getItem("refresh_token");
      if (token!=null && this.jwtHelper.isTokenExpired(token)){
        if(!this.jwtHelper.isTokenExpired(refreshToken)) {
          const headers = new HttpHeaders().set("Authorization", "Bearer " + refreshToken)
          console.log(headers)
          this.http.get<IdToken>("http://localhost:8080/refreshToken", {headers}).subscribe(
            response => {
              localStorage.setItem("access_token", response.accessToken.toString());
              localStorage.setItem("refresh_token", response.refreshToken.toString());
              this.isLoggedInSubject.next(true);
              window.location.reload();
            },
            error => {
              if (error.status === 400) {
                console.error("Error: ", error.error);
              } else {
                throw error;
              }
            }
          );
        }else{
          this.logout();
        }
      }
  }

}
