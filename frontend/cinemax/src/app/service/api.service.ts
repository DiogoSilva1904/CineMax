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
  
}
