import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Movie} from "../interface/movie";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private host = "localhost/8080";
  private moviesUrl = '/api/movies';

  constructor(private http: HttpClient) { }

  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.moviesUrl}`);
  }

  getMovieById(id: number): Observable<Movie> {
    return this.http.get<Movie>(`${this.moviesUrl}/${id}`);
  }

  saveMovie(movie: Movie): Observable<Movie> {
    return this.http.post<Movie>(`${this.moviesUrl}`, movie);
  }
}
