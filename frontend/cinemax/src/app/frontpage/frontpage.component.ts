import {Component, inject, OnInit} from '@angular/core';
import { MoviesGridComponent } from '../movies-grid/movies-grid.component';
import {NgForOf} from "@angular/common";
import {ApiService} from "../service/api.service";
import {Router} from "@angular/router";
import {MovieCardComponent} from "../movie-card/movie-card.component";
import {ClientMovieCardComponent} from "../client-movie-card/client-movie-card/client-movie-card.component";

@Component({
  selector: 'app-frontpage',
  standalone: true,
  imports: [MoviesGridComponent, NgForOf, MovieCardComponent, ClientMovieCardComponent,],
  templateUrl: './frontpage.component.html',
  styleUrl: './frontpage.component.css'
})
export class FrontpageComponent implements OnInit {
  ApiDataService = inject(ApiService);

  movies = [];

  constructor(private router: Router) {

    this.ApiDataService.getMovies().then((movies) => {
      this.movies = movies;
      console.log(this.movies);
    });

  }

  ngOnInit() {
  }

}
