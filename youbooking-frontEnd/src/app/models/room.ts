import {Reservation} from "./reservation";
import {Hotel} from "./hotel";

export class Room {
  private _id!:BigInt;
  private _number!:number;
  private _numberOfBeds!:number;
  private _price!:number;
  private _hotel!:Hotel;
  private _reservations!:Reservation[];

  constructor(number: number, numberOfBeds: number, price: number, hotel: Hotel, reservations: Reservation[]) {
    this._number = number;
    this._numberOfBeds = numberOfBeds;
    this._price = price;
    this._hotel = hotel;
    this._reservations = reservations;
  }

  get number(): number {
    return this._number;
  }

  set number(value: number) {
    this._number = value;
  }

  get numberOfBeds(): number {
    return this._numberOfBeds;
  }

  set numberOfBeds(value: number) {
    this._numberOfBeds = value;
  }

  get price(): number {
    return this._price;
  }

  set price(value: number) {
    this._price = value;
  }

  get hotel(): Hotel {
    return this._hotel;
  }

  set hotel(value: Hotel) {
    this._hotel = value;
  }

  get reservations(): Reservation[] {
    return this._reservations;
  }

  set reservations(value: Reservation[]) {
    this._reservations = value;
  }

  get id(): BigInt {
    return this._id;
  }
}
