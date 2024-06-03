import { NgFor } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { MovieCardComponent } from '../movie-card/movie-card.component';
import { NavigationEnd, Router } from '@angular/router';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-movies',
  standalone: true,
  imports: [ NgFor, NavbarComponent, MovieCardComponent],
  templateUrl: './movies.component.html',
  styleUrl: './movies.component.css'
})
export class MoviesComponent implements OnInit{
  ApiDataService = inject(ApiService);

  movies = [];

  constructor(private router: Router) { 
  }

  ngOnInit(): void {
    this.loadMovies();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.loadMovies();
      }
    });
  }

  loadMovies() {
    this.ApiDataService.getMovies().then((movies) => {
      this.movies = movies;
      console.log(this.movies);
    });
  }

  addNewMovie() {
    this.router.navigate(['/addmovie']);
  }

}
