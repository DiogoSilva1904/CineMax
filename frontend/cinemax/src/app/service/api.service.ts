import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  baseUrl = "http://localhost:8080/api";

  constructor() { }

  async getSessions() {
    const url = this.baseUrl + "/sessions";
    const data = await fetch(url, {method: 'GET'});
    return await data.json() ?? undefined;
  }

  async postReservation(reservation: any) {
    const url = this.baseUrl + "/reservations";
    const data = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(reservation)
    });
    return await data.json() ?? undefined;
  }

  async getMovies() {
    const url = `${this.baseUrl}/movies`;
    const response = await fetch(url, { method: 'GET' });
    return await response.json() ?? undefined;
  }

  async getMovie(movieId: string|null) {
    const url = `${this.baseUrl}/movies/${movieId}`;
    const response = await fetch(url, { method: 'GET' });
    return await response.json() ?? undefined;
  }

  async addMovie(movie: any) {
    const url = `${this.baseUrl}/movies`;
    const response = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(movie) });
    return await response.json() ?? undefined;
  }

  async deleteMovie(movieId: string) {
    const url = `${this.baseUrl}/movies/${movieId}`;
    console.log('Deleting movie: url:', url);
    const response = await fetch(url, { method: 'DELETE' });
    return await response.statusText ?? undefined;
  }
  
}
