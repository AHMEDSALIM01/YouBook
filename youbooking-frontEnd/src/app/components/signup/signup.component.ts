import { Component, OnInit } from '@angular/core';
import {Users} from "../../models/users";
import {HttpErrorResponse} from "@angular/common/http";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  user:Users;
  confirmPasswor!:String;
  role:String="CLIENT";
  errorMessage:String="";
  successMessage:String="";
  private patternPassword:RegExp= new RegExp("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
  private patternEmail:RegExp=new RegExp("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
  private patternTele:RegExp=new RegExp("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$");
  constructor(private authService:AuthService,private router:Router) {
    this.user = new Users();
  }

  ngOnInit(): void {
  }

  onSubmit(){
    console.log(this.role)
    if(this.user.name == null){
      this.errorMessage = "le nom est obligatoire";
    }
    else if(this.user.email==null || this.user.password == null){
      this.errorMessage = this.user.email==null ? "L'adresse email ne doit pas être vide":
        (this.user.password==null ? "le mot de passe ne doit pas être vide" : "");
    }
    else if(!this.patternPassword.test(this.user.password.toString()) || !this.patternEmail.test(this.user.email.toString())){
      this.errorMessage= !this.patternEmail.test(this.user.email.toString()) ? "L'adresse email est invalide (example@gmail.com)":
        (!this.patternPassword.test(this.user.password.toString()) ? "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre" : "");
    }
    else if(this.confirmPasswor == null){
      this.errorMessage = "il faut confirmer le mot de passe";
    }
    else if (this.user.phoneNumber == null){
      this.errorMessage = "le numéro de téléphone est obligatoire";
    }
    else if (!this.patternTele.test(this.user.phoneNumber.toString())){
      this.errorMessage = "numéro de télephone invalid";
    }
    else if(this.user.address == null){
      this.errorMessage = "l'adresse est obligatoire";
    }
    else if(this.user.password !== this.confirmPasswor){
      this.errorMessage = "les mot de passes ne corresponds pas";
    }
    else{
      this.authService.role=this.role;
      this.authService.signUp(this.user).subscribe(
        (response) => {
          if (response instanceof HttpErrorResponse) {
            this.errorMessage = response.error;
          } else {
            this.errorMessage = "";
            this.successMessage = "votre compte est enregistré avec succès";
            setTimeout(() => {
              this.successMessage = '';
              this.router.navigate(['/login']);
            }, 2500);
          }
        },
        (error) => {
          this.errorMessage = error;
        }
      );
    }
  }

}
