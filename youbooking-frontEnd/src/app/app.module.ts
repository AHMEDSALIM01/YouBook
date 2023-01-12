import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HotelComponent } from './components/hotel/hotel.component';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import { RoomsComponent } from './components/client/rooms/rooms.component';
import {FormsModule} from "@angular/forms";
import { HomeComponent } from './components/home/home.component';
import { NavBarComponent } from './components/nav-bar/nav-bar.component';
import { AuthenticationComponent } from './components/authentication/authentication.component';
import {NgxPaginationModule} from "ngx-pagination";
import {JWT_OPTIONS, JwtHelperService, JwtModule} from "@auth0/angular-jwt";
import {AuthService} from "./services/auth.service";

@NgModule({
  declarations: [
    AppComponent,
    HotelComponent,
    RoomsComponent,
    HomeComponent,
    NavBarComponent,
    AuthenticationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgxPaginationModule,
    JwtModule.forRoot({
      jwtOptionsProvider: {
        provide: JWT_OPTIONS,
        useValue: {
          tokenGetter: () => {
            // return the access token from storage
            return localStorage.getItem('access_token');
          },
          // other options you want to pass to the JWT module
        }
      }
    })
  ],
  providers: [AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
