import {Session}   from "./session";

export interface Movie {
  id: number;
  title: string;
  category: string;
  genre: string;
  studio: string;
  duration: string;
  sessions?: Session[];
}
