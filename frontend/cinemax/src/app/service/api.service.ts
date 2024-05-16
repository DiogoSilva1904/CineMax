import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  baseURL = 'http://localhost:8080';

  constructor() { }

  async getMovies() {
    const url = `${this.baseURL}/api/movies`;
    const response = await fetch(url, { method: 'GET' });
    return await response.json() ?? undefined;
  }

  async getMovie(movieId: string|null) {
    const url = `${this.baseURL}/api/movies/${movieId}`;
    const response = await fetch(url, { method: 'GET' });
    return await response.json() ?? undefined;
  }

  async addMovie(movie: any) {
    const url = `${this.baseURL}/api/movies`;
    const response = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(movie) });
    return await response.json() ?? undefined;
  }

  async deleteMovie(movieId: string) {
    const url = `${this.baseURL}/api/movies/${movieId}`;
    console.log('Deleting movie: url:', url);
    const response = await fetch(url, { method: 'DELETE' });
    return await response.statusText ?? undefined;
  }

  async getSessions() {
    const url = `${this.baseURL}/api/sessions`;
    const response = await fetch(url, { method: 'GET' });
    return await response.json() ?? undefined;
  }

}
