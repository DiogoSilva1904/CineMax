import { NgForOf } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import { Session } from 'inspector';

interface Seat {
  number: number;
  occupied: boolean;
}

@Component({
  selector: 'app-booking-page',
  standalone: true,
  imports: [NgForOf],
  templateUrl: './booking-page.component.html',
  styleUrl: './booking-page.component.css'
})
export class BookingPageComponent {
  seats: Seat[] = [
    { number: 1, occupied: false },
    { number: 2, occupied: true },
    { number: 3, occupied: false },
    { number: 4, occupied: false },
    { number: 5, occupied: false },
    { number: 6, occupied: false },
    { number: 7, occupied: false },
    { number: 8, occupied: false },
    { number: 9, occupied: false },
    { number: 10, occupied: false },
    { number: 11, occupied: false },
    { number: 12, occupied: false },
    { number: 13, occupied: false },
    { number: 14, occupied: false },
    { number: 15, occupied: false },
    { number: 16, occupied: false },
    { number: 17, occupied: false },
    { number: 18, occupied: false },
    { number: 19, occupied: false },
    { number: 20, occupied: false },
    { number: 21, occupied: false },
    { number: 22, occupied: false },
    { number: 23, occupied: false },
    { number: 24, occupied: false },
    { number: 25, occupied: false },
    { number: 26, occupied: false },
    { number: 27, occupied: false },
    { number: 28, occupied: false },
    { number: 29, occupied: false },
    { number: 30, occupied: false },
    { number: 31, occupied: false },
    { number: 32, occupied: false },
    { number: 33, occupied: false },
    { number: 34, occupied: false },
    { number: 35, occupied: false },
    { number: 36, occupied: false },
    { number: 37, occupied: false },
    { number: 38, occupied: false },
    { number: 39, occupied: false },
    { number: 40, occupied: false }
  ];
  selectedSeats: number[] = [];
  sessionTime: string = '18:00'; 
  day: string = 'Monday'; 
  movieName: string = 'Avengers: Endgame'; 
  totalPrice: number = 0;
  ApiService= inject(ApiService);
  Sessions: any[] = [];

  constructor() {
    this.ApiService.getSessions().then((sessions: any[]) => {
      console.log('Sessions:', sessions);
      this.Sessions = sessions;
    });
  }

  toggleSeat(seatNumber: number) {
    const index = this.selectedSeats.indexOf(seatNumber);
    if (index !== -1) {
      this.selectedSeats.splice(index, 1);
    } else {
      this.selectedSeats.push(seatNumber);
    }
    this.calculateTotalPrice(); 
  }

  calculateTotalPrice() {
    console.log(this.Sessions)
    this.totalPrice = this.selectedSeats.length * 10;
  }

  reserveSeats() {
    console.log('Reserving seats:', this.selectedSeats);
    console.log('Total Price:', this.totalPrice);
  }
}
