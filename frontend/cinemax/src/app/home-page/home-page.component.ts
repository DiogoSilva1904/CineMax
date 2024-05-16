import {Component, inject, OnInit} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import { NgFor } from '@angular/common';
import { MovieCardComponent } from '../movie-card/movie-card.component';
import { ClientNavbarComponent } from '../client-navbar/client-navbar.component';
import { ApiService } from '../service/api.service';

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
  movies: any[] = [];
  ApiDataService = inject(ApiService);


  constructor(private router: Router) {}

  async ngOnInit(): Promise<void> {
    this.ApiDataService.getMovies().then((movies) => {
      this.movies = movies;
      console.log(this.movies);
    });
  }


  goToMovieSessions(movieId: number): void {
    this.router.navigate(['/movie-sessions', movieId]);
  }
}
