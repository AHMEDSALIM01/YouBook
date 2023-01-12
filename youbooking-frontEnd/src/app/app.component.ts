import { Component } from '@angular/core';
import {AuthService} from "./services/auth.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'youbooking-frontEnd';
  isLogedIn=false;
  constructor(private authService:AuthService) { }

  ngOnInit(): void {
    this.isLogedIn=this.authService.isLogedIn();
  }
}
