import { Component, OnInit } from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { NgFor } from '@angular/common';
import { MovieCardComponent } from '../movie-card/movie-card.component';
import { ClientNavbarComponent } from '../client-navbar/client-navbar.component';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [
    NgFor,
    MovieCardComponent,
    ClientNavbarComponent,
    RouterLink
  ],
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements OnInit {
  movies = [
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
    },
    {
      id: 2,
      title: 'The Godfather',
      category: 'Crime',
      genre: 'Crime, Drama',
      studio: 'Paramount Pictures',
      duration: '2h 55m',
      sessions: [
        { id: 5, date: '2024-05-16', time: '20:00', movieId: 2, roomId: 2, bookedSeats: ['B1', 'B2'] }
      ]
    },
    {
      id: 3,
      title: 'The Godfather: Part II',
      category: 'Crime',
      genre: 'Crime, Drama',
      studio: 'Paramount Pictures',
      duration: '3h 22m',
      sessions: [
        { id: 6, date: '2024-05-17', time: '19:00', movieId: 3, roomId: 3, bookedSeats: ['C1', 'C2'] }
      ]
    },
    {
      id: 4,
      title: 'The Dark Knight',
      category: 'Action',
      genre: 'Action, Crime, Drama',
      studio: 'Warner Bros. Pictures',
      duration: '2h 32m',
      sessions: [
        { id: 7, date: '2024-05-18', time: '21:00', movieId: 4, roomId: 4, bookedSeats: ['D1', 'D2'] }
      ]
    }
  ];

  constructor(private router: Router) {}

  ngOnInit(): void {

  }

  goToMovieSessions(movieId: number): void {
    this.router.navigate(['/movie-sessions', movieId]);
  }
}
