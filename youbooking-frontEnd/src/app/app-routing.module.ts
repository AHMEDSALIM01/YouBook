import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RoomsComponent} from "./components/client/rooms/rooms.component";
import {HomeComponent} from "./components/home/home.component";
import {AuthenticationComponent} from "./components/authentication/authentication.component";
import {SignupComponent} from "./components/signup/signup.component";
import {ListreservationComponent} from "./components/client/listreservation/listreservation.component";
import {HotelComponent} from "./components/owner/hotel/hotel.component";
import {ForbiddenComponent} from "./components/forbidden/forbidden.component";
import {RoleGuard} from "./services/role-guard.service";
import {DefaultRouteGuardService} from "./services/default-route-guard.service";


const routes: Routes = [
  {
    path:"public-rooms",
    component:RoomsComponent,
  },
  {
    path:"home",
    component:HomeComponent,
  },
  {
    path:"",
    component:HomeComponent,
    canActivate:[DefaultRouteGuardService]
  },
  {
    path:"login",
    component:AuthenticationComponent
  },

  {
    path:"signup",
    component:SignupComponent
  },
  {
    path:'client/reservations',
    component:ListreservationComponent,
    canActivate: [RoleGuard],
    data :{expectedRoles:['CLIENT']}
  },
  {
    path:'owner/hotels',
    component:HotelComponent,
    canActivate: [RoleGuard],
    data :{expectedRoles:['OWNER']}
  },
  {
    path:'forbidden',
    component:ForbiddenComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
