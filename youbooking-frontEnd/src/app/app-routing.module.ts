import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HotelComponent} from "./components/hotel/hotel.component";
import {RoomsComponent} from "./components/rooms/rooms.component";

const routes: Routes = [
  {
    path:"hotel",
    component: HotelComponent
  },
  {
    path:"rooms",
    component:RoomsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
