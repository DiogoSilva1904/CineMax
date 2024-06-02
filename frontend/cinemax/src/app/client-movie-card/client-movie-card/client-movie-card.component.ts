import {Component, Input, inject} from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-client-movie-card',
  standalone: true,
  imports: [],
  templateUrl: './client-movie-card.component.html',
  styleUrl: './client-movie-card.component.css'
})
export class ClientMovieCardComponent {

  @Input() movie: any;

  ApiDataService = inject(ApiService);

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

}
