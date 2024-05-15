import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Movie} from "../interface/movie";
import {Session} from "node:inspector";
import {Room} from "../interface/room";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private host = "http://localhost/8080";
  private moviesUrl = `${this.host}/api/movies`;
  private sessionsUrl = `${this.host}/api/sessions`;
  private roomsUrl = `${this.host}/api/rooms`;

  constructor(private http: HttpClient) { }

  // =============== Movies api methods ===============


  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.moviesUrl}`);
  }

  getMovieById(id: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.moviesUrl}/${id}`);
  }

  saveMovie(movie: Movie): Observable<Movie> {
    return this.http.post<Movie>(`${this.moviesUrl}`, movie);
  }

  // =============== Session api methods ===============

  getAllSessions(): Observable<Session[]> {
    return this.http.get<Session[]>(`${this.sessionsUrl}`);
  }

  getSessionById(id: number): Observable<Session> {
    return this.http.get<Session>(`${this.sessionsUrl}/${id}`);
  }

  saveSession(session: Session): Observable<Session> {
    return this.http.post<Session>(`${this.sessionsUrl}`, session);
  }

  // =============== Room api methods ===============

  getAllRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(`${this.roomsUrl}`);
  }

  getRoomById(id: number): Observable<Room> {
    return this.http.get<Room>(`${this.roomsUrl}/${id}`);
  }

  saveRoom(room: Room): Observable<Room> {
    return this.http.post<Room>(`${this.roomsUrl}`, room);
  }

}
