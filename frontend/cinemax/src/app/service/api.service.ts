import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  baseUrl = "http://localhost:8080/api";

  constructor(@Inject(PLATFORM_ID) private platformId: Object) { }

  private getAuthToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem('token');
    } else {
      return null;
    }
  }

  private getHeaders(withAuth: boolean = false,isMultipart: boolean = false): Headers {
    const headers = new Headers({
    });
    if (!isMultipart) {
      headers.append('Content-Type', 'application/json');
    }
    if (withAuth) {
      const token = this.getAuthToken();
      if (token) {
        headers.append('Authorization', `Bearer ${token}`);
      }
    }
    return headers;
  }

  async getSessions() {
    const url = `${this.baseUrl}/sessions`;
    const response = await fetch(url, {
      method: 'GET',
      headers: this.getHeaders(true)
    });
    return await response.json() ?? undefined;
  }

  async getSession(sessionId: string | null) {
    const url = `${this.baseUrl}/sessions/${sessionId}`;
    const response = await fetch(url, {
      method: 'GET',
      headers: this.getHeaders(true)
    });
    return await response.json() ?? undefined;
  }

  async postReservation(reservation: any) {
    const url = `${this.baseUrl}/reservations`;
    const response = await fetch(url, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(reservation)
    });
    return await response.json() ?? undefined;
  }

  async getReservationById(reservationId: string) {
    const url = `${this.baseUrl}/reservations/${reservationId}`;
    const response = await fetch(url, { 
      method: 'GET',
      headers: this.getHeaders(true)
    });
    return await response.json() ?? undefined;
  }

  async getMovies() {
    const url = `${this.baseUrl}/movies`;
    const response = await fetch(url, {
      method: 'GET',
      headers: this.getHeaders(true)
    });
    return await response.json() ?? undefined;
  }

  async getMovie(movieId: string | null) {
    const url = `${this.baseUrl}/movies/${movieId}`;
    const response = await fetch(url, {
      method: 'GET',
      headers: this.getHeaders(true)
    });
    return await response.json() ?? undefined;
  }

  async addMovie(movieData: any) {
    const url = `${this.baseUrl}/movies`;
    console.log('movieData', movieData);
    const response = await fetch(url, {
      method: 'POST',
      headers: this.getHeaders(true,true),
      body: movieData
    });
    return await response.json() ?? undefined;
  }

  async deleteMovie(movieId: string) {
    const url = `${this.baseUrl}/movies/${movieId}`;
    const response = await fetch(url, {
      method: 'DELETE',
      headers: this.getHeaders(true)
    });
    return response.statusText ?? undefined;
  }

  async getRooms() {
    const url = `${this.baseUrl}/rooms`;
    const response = await fetch(url, {
      method: 'GET',
      headers: this.getHeaders(true)
    });
    return await response.json() ?? undefined;
  }

  async addSession(session: any) {
    const url = `${this.baseUrl}/sessions`;
    const response = await fetch(url, {
      method: 'POST',
      headers: this.getHeaders(true),
      body: JSON.stringify(session)
    });
    return await response.json() ?? undefined;
  }

  async deleteSession(sessionId: string) {
    const url = `${this.baseUrl}/sessions/${sessionId}`;
    const response = await fetch(url, {
      method: 'DELETE',
      headers: this.getHeaders(true)
    });
    return response.statusText ?? undefined;
  }

  async registerUser(username: string, password: string, email: string) {
    const url = `${this.baseUrl}/login/register`;
    const response = await fetch(url, {
      method: 'POST',
      headers: this.getHeaders(),
      body: JSON.stringify({ username, password, email })
    });
    return response.status ?? undefined;
  }

  async loginUser(username: string, password: string) {
    const url = `${this.baseUrl}/login`;
    const response = await fetch(url, {
      method: 'POST',
      headers: this.getHeaders(),
      body: JSON.stringify({ username, password })
    });

    let responseData;
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      responseData = await response.json();
    } else {
      responseData = await response.text();
    }

    return {
      status: response.status,
      data: responseData
    };
  }

  async changePassword(username: string, currentPassword: string, newPassword: string) {
    const url = `${this.baseUrl}/login/change-password`;
    const response = await fetch(url, {
      method: 'PUT',
      headers: this.getHeaders(true),
      body: JSON.stringify({ username, currentPassword, newPassword })
    });
    return response.status ?? undefined;
  }


  async getSessionsByDate(date: string) {
    const url = `${this.baseUrl}/sessions/date/${date}`;
    const response = await fetch(url, {
      method: 'GET',
      headers: this.getHeaders(true)
    });
    return await response.json() ?? undefined;
  }

  async getImage(path: string): Promise<Blob | undefined> {
    const url = `${this.baseUrl}/movies/image/${path}`;
    const response = await fetch(url, {
      method: 'GET',
      headers: this.getHeaders(true),
    });
    
    if (!response.ok) {
      console.error(`Failed to fetch image. Status: ${response.status}`);
      return undefined;
    }
    
    return await response.blob();
  }
  
  async getReservationsByUser(username: string | null) {
    const url = `${this.baseUrl}/reservations/user/${username}`;
    const response = await fetch(url, {
      method: 'GET',
      headers: this.getHeaders(true)
    });
    return await response.json() ?? undefined;
  }

  async validateTicket(ticketId: string, jwt: string) {
    const url = `${this.baseUrl}/reservations/${ticketId}`;
    const response = await fetch(url, {
      method: 'PUT',
      headers: new Headers({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${jwt}`
      })
    });
    return { status: response.status };
  }

}
