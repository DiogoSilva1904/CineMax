import {Session}   from "./session";

export interface Reservation {
  id: number;
  username: string;
  session: Session;
  seatNumbers: string[];
}
