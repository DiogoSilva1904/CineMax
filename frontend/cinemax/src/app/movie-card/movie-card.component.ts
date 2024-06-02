import { Component, Input, OnInit, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-movie-card',
  standalone: true,
  imports: [],
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.css'
})
export class MovieCardComponent implements OnInit{

  ApiDataService = inject(ApiService);

  @Input() movie: any;
  imageUrl: SafeUrl | undefined;

  constructor(private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.loadImage();
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

  deleteMovie(movieId: string) {
    console.log('Deleting movie:', movieId);
    this.ApiDataService.deleteMovie(movieId).then((response) => {
      console.log('Movie deleted:', response);
      window.location.reload();
    });
  }



}
