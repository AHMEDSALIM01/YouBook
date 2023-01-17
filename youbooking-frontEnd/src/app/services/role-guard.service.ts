import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate{

  constructor(private authService: AuthService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const expectedRoles = route.data['expectedRoles'];
    const userRole = this.authService.getUserRole();

    if (!this.authService.isLogedIn()) {
      this.router.navigate(['/login']);
      return false;
    }
    if (!expectedRoles.some((r: string) => userRole==r)) {
      this.router.navigate(['/forbidden']);
      return false;
    }
    return true;
  }
}
