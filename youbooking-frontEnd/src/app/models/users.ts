import {Role} from "./role";
import {Reservation} from "./reservation";
import {Hotel} from "./hotel";

export class Users {
  id!:BigInt;
  name!:String;
  address!:String;
  phoneNumber!:String;
  email!:String;
  password!:String;
  is_active!:Boolean;
  roles!:Set<Role>;
  reservations!:Reservation;
  hotels!:Set<Hotel>;
}
