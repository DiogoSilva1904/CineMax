import { NgForOf } from '@angular/common';
import { Component, OnInit, OnDestroy, inject } from '@angular/core';
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
export class DigitalSignagePageComponent implements OnInit, OnDestroy{
  currentTime: string = this.getCurrentTime();
  ApiService= inject(ApiService);
  Sessions: Session[] | undefined;
  interval: any;

  constructor() {
  }

  ngOnInit(): void {
    this.currentTime = this.getCurrentTime();
    this.initDataFetching();
  }

  ngOnDestroy(): void {
    if (this.interval) {
      clearInterval(this.interval);
    }
  }

  async initDataFetching(): Promise<void> {
    try {
      this.Sessions = await this.ApiService.getSessionsByDate("2024-05-15");
      this.interval = setInterval(() => {
        this.refreshData();
      }, 10000);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }

  async refreshData(): Promise<void> {
    try {
      this.Sessions = await this.ApiService.getSessionsByDate("2024-05-15");
      this.currentTime = this.getCurrentTime();
    } catch (error) {
      console.error('Error fetching data:', error);
    }
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
