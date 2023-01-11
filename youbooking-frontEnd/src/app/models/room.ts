import {Reservation} from "./reservation";
import {Hotel} from "./hotel";

export class Room {
  id!:number;
  number!:number;
  numberOfBeds!:number;
  price!:number;
  description!:String;
  hotel!:Hotel;
  reservations!:Reservation[];
}
