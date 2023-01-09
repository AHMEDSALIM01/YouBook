import {Role} from "./role";
import {Reservation} from "./reservation";
import {Hotel} from "./hotel";

export class Users {
  private _id!:BigInt;
  private _name!:String;
  private _address!:String;
  private _phoneNumber!:String;
  private _email!:String;
  private _password!:String;
  private _is_active!:Boolean;
  private _roles!:Set<Role>;
  private _reservations!:Reservation;
  private _hotels!:Set<Hotel>;

  constructor(name: String, address: String, phoneNumber: String, email: String, password: String, is_active: Boolean, roles: Set<Role>, reservations: Reservation, hotels: Set<Hotel>) {
    this._name = name;
    this._address = address;
    this._phoneNumber = phoneNumber;
    this._email = email;
    this._password = password;
    this._is_active = is_active;
    this._roles = roles;
    this._reservations = reservations;
    this._hotels = hotels;
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

  get phoneNumber(): String {
    return this._phoneNumber;
  }

  set phoneNumber(value: String) {
    this._phoneNumber = value;
  }

  get email(): String {
    return this._email;
  }

  set email(value: String) {
    this._email = value;
  }

  get password(): String {
    return this._password;
  }

  set password(value: String) {
    this._password = value;
  }

  get is_active(): Boolean {
    return this._is_active;
  }

  set is_active(value: Boolean) {
    this._is_active = value;
  }

  get roles(): Set<Role> {
    return this._roles;
  }

  set roles(value: Set<Role>) {
    this._roles = value;
  }

  get reservations(): Reservation {
    return this._reservations;
  }

  set reservations(value: Reservation) {
    this._reservations = value;
  }

  get hotels(): Set<Hotel> {
    return this._hotels;
  }

  set hotels(value: Set<Hotel>) {
    this._hotels = value;
  }

  get id(): BigInt {
    return this._id;
  }
}
