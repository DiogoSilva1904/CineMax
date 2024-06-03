import { Component, inject, OnInit } from '@angular/core';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';
import {ClientMovieCardComponent} from "../client-movie-card/client-movie-card/client-movie-card.component";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-movies-carousel',
  templateUrl: './movies-carousel.component.html',
  standalone: true,
  imports: [
    ClientMovieCardComponent,
    NgForOf,
    NgIf
  ],
  styleUrls: ['./movies-carousel.component.css']
})
export class MoviesCarouselComponent implements OnInit {
  ApiDataService = inject(ApiService);

  movies: any[] = [];
  currentSlideIndex: number = 0;

  constructor(private router: Router) {
    this.ApiDataService.getMovies().then((movies) => {
      this.movies = movies;
      console.log(this.movies);
    });
  }

  goToMovieSessions(movieId: number): void {
    this.router.navigate(['/movie-sessions', movieId]);
  }

  nextSlide(): void {
    const maxIndex = Math.max(this.movies.length - 2, 0);
    if (this.currentSlideIndex + 1 < this.movies.length) {
      this.currentSlideIndex++;
    } else {
      this.currentSlideIndex = 0;
    }
  }

  prevSlide(): void {
    if (this.currentSlideIndex > 0) {
      this.currentSlideIndex--;
    } else {
      this.currentSlideIndex = Math.max(this.movies.length - 2, 0);
    }
  }


  ngOnInit() {}

  protected readonly Math = Math;
}
