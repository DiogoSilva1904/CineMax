import { NgForOf } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ApiService } from '../service/api.service';

interface Seat {
  seatIdentifier: string;
  occupied: boolean;
}

interface Session {
  id: number;
  date: string;
  time: string;
  movie: any;
  room: any;
  reservation: any[];
  bookedSeats: string[];
}

interface Reservation {
  id: number;
  username: string;
  price: number;
  session: Session;
  seatNumbers: string[];
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
    { seatIdentifier: "A1", occupied: false },
    { seatIdentifier: "A2", occupied: false },
    { seatIdentifier: "A3", occupied: false },
    { seatIdentifier: "A4", occupied: false },
    { seatIdentifier: "A5", occupied: false },
    { seatIdentifier: "A6", occupied: false },
    { seatIdentifier: "A7", occupied: false },
    { seatIdentifier: "A8", occupied: false },
    { seatIdentifier: "A9", occupied: false },
    { seatIdentifier: "A10", occupied: false },
    { seatIdentifier: "B1", occupied: false },
    { seatIdentifier: "B2", occupied: false },
    { seatIdentifier: "B3", occupied: false },
    { seatIdentifier: "B4", occupied: false },
    { seatIdentifier: "B5", occupied: false },
    { seatIdentifier: "B6", occupied: false },
    { seatIdentifier: "B7", occupied: false },
    { seatIdentifier: "B8", occupied: false },
    { seatIdentifier: "B9", occupied: false },
    { seatIdentifier: "B10", occupied: false },
    { seatIdentifier: "C1", occupied: false },
    { seatIdentifier: "C2", occupied: false },
    { seatIdentifier: "C3", occupied: false },
    { seatIdentifier: "C4", occupied: false },
    { seatIdentifier: "C5", occupied: false },
    { seatIdentifier: "C6", occupied: false },
    { seatIdentifier: "C7", occupied: false },
    { seatIdentifier: "C8", occupied: false },
    { seatIdentifier: "C9", occupied: false },
    { seatIdentifier: "C10", occupied: false },
    { seatIdentifier: "D1", occupied: false },
    { seatIdentifier: "D2", occupied: false },
    { seatIdentifier: "D3", occupied: false },
    { seatIdentifier: "D4", occupied: false },
    { seatIdentifier: "D5", occupied: false },
    { seatIdentifier: "D6", occupied: false },
    { seatIdentifier: "D7", occupied: false },
    { seatIdentifier: "D8", occupied: false },
    { seatIdentifier: "D9", occupied: false },
    { seatIdentifier: "D10", occupied: false },
  ];
  selectedSeats: string[] = [];
  sessionTime: string = '18:00';
  day: string = 'Monday';
  movieName: string = 'Avengers: Endgame';
  totalPrice: number = 0;
  ApiService= inject(ApiService);
  Sessions: Session[] = [];
  User: string = 'User';
  Session: any;

  constructor() {
    this.ApiService.getSessions().then((sessions: any[]) => {
      this.Sessions = sessions;
      sessions.forEach((session) => {
        if (session.id === 1)
          this.movieName = session.movie.title;
          this.day = session.date;
          this.sessionTime = session.time;
          this.Session = session;
          for (let i = 0; i < this.seats.length; i++) {
            this.seats[i].occupied = session.bookedSeats.includes(this.seats[i].seatIdentifier);
          }
      });
    });
  }

  toggleSeat(seatIdentifier: string) {
    const index = this.selectedSeats.indexOf(seatIdentifier);
    if (index !== -1) {
      this.selectedSeats.splice(index, 1);
    } else {
      this.selectedSeats.push(seatIdentifier);
    }
    this.calculateTotalPrice();
  }

  calculateTotalPrice() {
    this.totalPrice = this.selectedSeats.length * 10;
  }

  reserveSeats() {
    const reservation = {
      id: 1,
      username: this.User,
      price: this.totalPrice,
      session: this.Session,
      seatNumbers: this.selectedSeats
    };
    this.ApiService.postReservation(reservation).then((data) => {
      console.log(data);
    });
  }
}
