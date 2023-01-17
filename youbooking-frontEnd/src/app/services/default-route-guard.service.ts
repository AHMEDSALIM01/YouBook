import { Injectable } from '@angular/core';
import {AuthService} from "./auth.service";
import {ActivatedRouteSnapshot, CanActivate, Router, UrlTree} from "@angular/router";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DefaultRouteGuardService implements CanActivate{

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const userRoles = this.authService.getUserRole();
        if (!this.authService.isLogedIn()) {
          this.router.navigate(['/home']);
          return true;
        }else{
          if (userRoles=="CLIENT") {
            this.router.navigate(['/home']);
            return true;
          } else if(userRoles=="OWNER"){
            this.router.navigate(['/owner/hotels']);
            return true;
          }
        }

      return true;
  }
}
