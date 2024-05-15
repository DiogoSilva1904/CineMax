import {Movie} from "./movie";
import {Room} from "./room";
import {Reservation} from "./reservation";

export interface Session {
  id: number;
  date: string;
  time: string;
  movie: Movie; // Assuming Movie interface is already defined
  room: Room; // Assuming Room interface is already defined
  reservations?: Reservation[]; // Assuming Reservation interface is already defined
  bookedSeats: string[]; // Array with the seats occupied
}
