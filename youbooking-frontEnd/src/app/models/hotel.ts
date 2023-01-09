import {StatusHotel} from "./status-hotel";
import {Users} from "./users";
import {Room} from "./room";

export class Hotel {
  private _id!:BigInt;
  private _name!:String;
  private _address!:String;
  private _city!:String;
  private _numberOfRooms!:number;
  private _startNonAvailable!:Date;
  private _endNonAvailable!:Date;
  private _status!:StatusHotel;
  private _rooms!:Room[];
  private _owner!:Users;

  constructor(name: String, address: String, city: String, numberOfRooms: number, startNonAvailable: Date, endNonAvailable: Date, status: StatusHotel, rooms: Room[], owner: Users) {
    this._name = name;
    this._address = address;
    this._city = city;
    this._numberOfRooms = numberOfRooms;
    this._startNonAvailable = startNonAvailable;
    this._endNonAvailable = endNonAvailable;
    this._status = status;
    this._rooms = rooms;
    this._owner = owner;
  }


  get name(): String {
    return this._name;
  }

  set name(value: String) {
    this._name = value;
  }

  get address(): String {
    return this._address;
  }

  set address(value: String) {
    this._address = value;
  }

  get city(): String {
    return this._city;
  }

  set city(value: String) {
    this._city = value;
  }

  get numberOfRooms(): number {
    return this._numberOfRooms;
  }

  set numberOfRooms(value: number) {
    this._numberOfRooms = value;
  }

  get startNonAvailable(): Date {
    return this._startNonAvailable;
  }

  set startNonAvailable(value: Date) {
    this._startNonAvailable = value;
  }

  get endNonAvailable(): Date {
    return this._endNonAvailable;
  }

  set endNonAvailable(value: Date) {
    this._endNonAvailable = value;
  }

  get status(): StatusHotel {
    return this._status;
  }

  set status(value: StatusHotel) {
    this._status = value;
  }

  get rooms(): Room[] {
    return this._rooms;
  }

  set rooms(value: Room[]) {
    this._rooms = value;
  }

  get owner(): Users {
    return this._owner;
  }

  set owner(value: Users) {
    this._owner = value;
  }

  get id(): BigInt {
    return this._id;
  }

}
