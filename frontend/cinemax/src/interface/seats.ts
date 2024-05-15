import {Room} from "./room";

export interface Seat {
  id: number;
  seatIdentifier: string;
  priceMultiplier: number;
  room: Room;
}
