import {Session} from "./session";
import {Seat} from "./seats";

export interface Room {
  id: number;
  name: string;
  capacity: number;
  rows: number;
  columns: number;
  type: string;
  sessions?: Session[];
  seats?: Seat[];
}
