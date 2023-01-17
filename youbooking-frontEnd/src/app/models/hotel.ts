import {StatusHotel} from "./status-hotel";
import {Users} from "./users";
import {Room} from "./room";

export class Hotel {
  id!:BigInt;
  name!:String;
  address!:String;
  city!:String;
  numberOfRooms!:number;
  startNonAvailable!:Date;
  endNonAvailable!:Date;
  status!:StatusHotel;
  rooms!:Room[];
  owner!:Users;
}
