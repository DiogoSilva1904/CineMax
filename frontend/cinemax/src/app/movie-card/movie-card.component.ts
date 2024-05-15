import { Component, Input, inject } from '@angular/core';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-movie-card',
  standalone: true,
  imports: [],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.css'
})
export class MovieCardComponent {

  ApiDataService = inject(ApiService);

  @Input() movie: any;

  constructor() { }

  deleteMovie(movieId: string) {
    console.log('Deleting movie:', movieId);
    this.ApiDataService.deleteMovie(movieId).then((response) => {
      console.log('Movie deleted:', response);
      window.location.reload();
    });
  }



}
