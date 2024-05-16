import {Component, inject} from '@angular/core';
import {ClientNavbarComponent} from "../client-navbar/client-navbar.component";
import {NgForOf, NgIf} from "@angular/common";
import {Router, ActivatedRoute } from '@angular/router';
import {ApiService} from "../service/api.service";


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
  movie: any;

  ApiDataService = inject(ApiService);

  constructor(private route: ActivatedRoute,private router: Router) {
    this.route.paramMap.subscribe(params => {
      const movieId = params.get('id');
      // Fetch movie details using the movie id
      this.ApiDataService.getMovie(movieId).then((movie) => {
        this.movie = movie;
        console.log(this.movie);
      }).catch(error => {
        console.error('Error fetching movie:', error);
      });
    });
  }

  goToChooseSeat(sessionId: number): void {
    this.router.navigate(['/seat', sessionId]);
  }

}
