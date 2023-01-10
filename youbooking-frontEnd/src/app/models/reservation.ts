import {Room} from "./room";
import {Users} from "./users";
import {StatusReservation} from "./status-reservation";

export class Reservation {
  id!:BigInt;
  ref!:String;
  startDate!:Date;
  endDate!:Date;
  totalPrice!:number;
  status!:StatusReservation;
  room!:Room;
  user!:Users;
}
