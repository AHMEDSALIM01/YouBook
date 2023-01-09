import {Room} from "./room";
import {Users} from "./users";
import {StatusReservation} from "./status-reservation";

export class Reservation {
  private _id!:BigInt;
  private _ref!:String;
  private _startDate!:Date;
  private _endDate!:Date;
  private _totalPrice!:number;
  private _status!:StatusReservation;
  private _room!:Room;
  private _user!:Users;

  constructor(ref: String, startDate: Date, endDate: Date, totalPrice: number, status: StatusReservation, room: Room, user: Users) {
    this._ref = ref;
    this._startDate = startDate;
    this._endDate = endDate;
    this._totalPrice = totalPrice;
    this._status = status;
    this._room = room;
    this._user = user;
  }

  get ref(): String {
    return this._ref;
  }

  set ref(value: String) {
    this._ref = value;
  }

  get startDate(): Date {
    return this._startDate;
  }

  set startDate(value: Date) {
    this._startDate = value;
  }

  get endDate(): Date {
    return this._endDate;
  }

  set endDate(value: Date) {
    this._endDate = value;
  }

  get totalPrice(): number {
    return this._totalPrice;
  }

  set totalPrice(value: number) {
    this._totalPrice = value;
  }

  get status(): StatusReservation {
    return this._status;
  }

  set status(value: StatusReservation) {
    this._status = value;
  }

  get room(): Room {
    return this._room;
  }

  set room(value: Room) {
    this._room = value;
  }

  get user(): Users {
    return this._user;
  }

  set user(value: Users) {
    this._user = value;
  }

  get id(): BigInt {
    return this._id;
  }
}
