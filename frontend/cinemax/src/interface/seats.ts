import {Room} from "./room";

export interface Seat {
  id: number;
  seatIdentifier: string;
  priceMultiplier: number;
  room: Room; // Assuming Room interface is already defined
}
