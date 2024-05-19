import { NgForOf } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ApiService } from '../service/api.service';


interface Session {
  id: number;
  date: string;
  time: string;
  availableSeats: number;
  movie: any;
  room: any;
  reservation: any[];
  bookedSeats: string[];
}
  
@Component({
  selector: 'app-digital-signage-page',
  standalone: true,
  imports: [NgForOf],
  templateUrl: './digital-signage-page.component.html',
  styleUrl: './digital-signage-page.component.css'
})
export class DigitalSignagePageComponent {
  currentTime: string = this.getCurrentTime();
  ApiService= inject(ApiService);
  Sessions: Session[] | undefined;

  constructor() {
    this.ApiService.getSessionsByDate("2024-05-15").then((sessions) => {
      this.Sessions = sessions;
    });
  }

  getCurrentTime(): string {
    const now = new Date();
    const hours = this.padZero(now.getHours());
    const minutes = this.padZero(now.getMinutes());
    const seconds = this.padZero(now.getSeconds());
    return `${hours}:${minutes}`;
  }

  padZero(value: number): string {
    return value < 10 ? '0' + value : value.toString();
  }
}
