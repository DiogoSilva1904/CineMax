import { NgFor } from '@angular/common';
import { Component, inject } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { MovieCardComponent } from '../movie-card/movie-card.component';
import { Router } from '@angular/router';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-movies',
  standalone: true,
  imports: [ NgFor, NavbarComponent, MovieCardComponent],
  templateUrl: './movies.component.html',
  styleUrl: './movies.component.css'
})
export class MoviesComponent {
  ApiDataService = inject(ApiService);

  movies = [];

  constructor(private router: Router) {

    this.ApiDataService.getMovies().then((movies) => {
      this.movies = movies;
      console.log("Movies fetched",this.movies);
      console.log("All sessions : ",this.ApiDataService.getSessions())
    });

  }



  addNewMovie() {
    this.router.navigate(['/addmovie']);
  }

}
