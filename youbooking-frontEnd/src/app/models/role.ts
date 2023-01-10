export class Role {
  private _id!:BigInt;
  private _name!:String;

  constructor(name: String) {
    this._name = name;
  }

  get id(): BigInt {
    return this._id;
  }

  get name(): String {
    return this._name;
  }

  set name(value: String) {
    this._name = value;
  }
}
