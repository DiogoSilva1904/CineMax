import {Component, OnInit, inject} from '@angular/core';
import {ClientNavbarComponent} from "../client-navbar/client-navbar.component";
import {NgForOf, NgIf} from "@angular/common";
import {Router, ActivatedRoute } from '@angular/router';
import {ApiService} from "../service/api.service";
import { DomSanitizer } from '@angular/platform-browser';


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
export class MovieSessionsComponent implements OnInit{
  movie: any;
  imageUrl: any;

  ApiDataService = inject(ApiService);

  constructor(private route: ActivatedRoute,private router: Router, private sanitizer: DomSanitizer) {
    
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const movieId = params.get('id');
      // Fetch movie details using the movie id
      this.ApiDataService.getMovie(movieId).then((movie) => {
        this.movie = movie;
        console.log(this.movie);
        this.loadImage();
      }).catch(error => {
        console.error('Error fetching movie:', error);
      });
    });
  }

  async loadImage(): Promise<void> {
    if (this.movie.imagePath == null) {
      this.imageUrl = 'https://i.ibb.co/FDGqCmM/papers-co-ag74-interstellar-wide-space-film-movie-art-33-iphone6-wallpaper.jpg';
    }
    else {
      const imageBlob = await this.ApiDataService.getImage(this.movie.imagePath);
      if (imageBlob) {
        const objectURL = URL.createObjectURL(imageBlob);
        this.imageUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL);
      }
    }
  }

  goToChooseSeat(sessionId: number): void {
    this.router.navigate(['/seat', sessionId]);
  }

}
