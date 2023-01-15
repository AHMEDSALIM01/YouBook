import { Injectable } from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {
  BehaviorSubject,
  catchError,
  delayWhen,
  filter,
  Observable,
  of,
  retryWhen,
  switchMap,
  take,
  throwError,
  timer
} from "rxjs";
import {AuthService} from "./auth.service";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor{

  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private auth: AuthService,private jwtHelper:JwtHelperService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.auth.isLogedIn()) {
        req = req.clone({
          setHeaders: {
            Authorization: `Bearer ${this.auth.getAccessToken()}`
          }
        });
        return next.handle(req);
    } else {
      if (!req.url.endsWith('/login') && !req.url.endsWith('/signup') && !req.url.endsWith('/public-rooms') && !req.url.endsWith('/home')) {
        this.auth.redirectToLogin();
      }
      return next.handle(req);
    }
  }
}
