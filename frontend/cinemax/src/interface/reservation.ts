import {Session}   from "./session";

export interface Reservation {
  id: number;
  username: string;
  session: Session; // Assuming Session interface is already defined
  seatNumbers: string[]; // Array of seat numbers
}
