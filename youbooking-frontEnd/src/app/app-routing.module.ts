import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RoomsComponent} from "./components/client/rooms/rooms.component";
import {HomeComponent} from "./components/home/home.component";
import {AuthenticationComponent} from "./components/authentication/authentication.component";


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
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
