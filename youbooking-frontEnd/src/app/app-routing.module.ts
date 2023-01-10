import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {RoomsComponent} from "./components/client/rooms/rooms.component";
import {HomeComponent} from "./components/home/home.component";


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
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
