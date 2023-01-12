import { Component, OnInit } from '@angular/core';
import {Users} from "../../models/users";
import {AuthService} from "../../services/auth.service";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-authentication',
  templateUrl: './authentication.component.html',
  styleUrls: ['./authentication.component.css']
})
export class AuthenticationComponent implements OnInit {
  public errorMessage:String="";
  public successMessage:String="";
  public user:Users
  private patternPassword:RegExp= new RegExp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
  private patternEmail:RegExp=new RegExp("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
  constructor(private authService:AuthService, private router:Router) {
    this.user=new Users();
  }

  ngOnInit(): void {
    if(this.authService.isLogedIn()){
      this.router.navigate(['/']);
    }
  }

  onSubmit(){
    if(this.user.email==null || this.user.password == null){
      this.errorMessage = this.user.email==null ? "L'adresse email ne doit pas être vide":
        (this.user.password==null ? "le mot de passe ne doit pas être vide" : "");
    }if(!this.patternPassword.test(this.user.password.toString()) || !this.patternEmail.test(this.user.email.toString())){
      this.errorMessage= !this.patternEmail.test(this.user.email.toString()) ? "L'adresse email est invalide (example@gmail.com)":
        (!this.patternPassword.test(this.user.password.toString()) ? "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre" : "");
    }
    else{
      this.authService.signIn(this.user).subscribe(
        (response) => {
          if (response instanceof HttpErrorResponse) {
            this.errorMessage = response.error
          } else {
            this.errorMessage = "";
            this.successMessage = "vous êtes connecté avec succès";
            setTimeout(() => {
              this.successMessage = '';
              this.router.navigate(['/']);
            }, 4000);
          }
        },
        (error) => {
          this.errorMessage = error;
        }
      );
    }
  }
}
