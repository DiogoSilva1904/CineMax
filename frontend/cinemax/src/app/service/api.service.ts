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
  
}
