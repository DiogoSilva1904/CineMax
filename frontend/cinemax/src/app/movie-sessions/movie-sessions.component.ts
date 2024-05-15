import { Component } from '@angular/core';
import {ClientNavbarComponent} from "../client-navbar/client-navbar.component";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-movie-sessions',
  standalone: true,
  imports: [
    ClientNavbarComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './movie-sessions.component.html',
  styleUrl: './movie-sessions.component.css'
})
export class MovieSessionsComponent {
  movie =
    {
      id: 1,
      title: 'The Shawshank Redemption',
      category: 'Drama',
      genre: 'Drama',
      studio: 'Columbia Pictures',
      duration: '2h 22m',
      sessions: [
        { id: 1, date: '2024-05-15', time: '18:00', movieId: 1, roomId: 1, bookedSeats: ['A1', 'A2'] },
        { id: 2, date: '2024-05-15', time: '18:00', movieId: 1, roomId: 1, bookedSeats: ['A1', 'A2'] },
        { id: 3, date: '2024-05-15', time: '18:00', movieId: 1, roomId: 1, bookedSeats: ['A1', 'A2'] },
        { id: 4, date: '2024-05-15', time: '18:00', movieId: 1, roomId: 1, bookedSeats: ['A1', 'A2'] }

      ]
    };

  constructor() { }

}
