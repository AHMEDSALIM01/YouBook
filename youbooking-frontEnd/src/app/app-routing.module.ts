import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RoomsComponent} from "./components/client/rooms/rooms.component";
import {HomeComponent} from "./components/home/home.component";
import {AuthenticationComponent} from "./components/authentication/authentication.component";
import {SignupComponent} from "./components/signup/signup.component";
import {ListreservationComponent} from "./components/client/listreservation/listreservation.component";


const routes: Routes = [
  {
    path:"rooms",
    component:RoomsComponent
  },
  {
    path:"home",
    component:HomeComponent
  },
  {
    path:"",
    component:HomeComponent
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
    path:'MesReservation',
    component:ListreservationComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
