import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-add-movie',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './add-movie.component.html',
  styleUrl: './add-movie.component.css'
})
export class AddMovieComponent {

  ApiDataService = inject(ApiService);

  movie: any = {
    title: '',
    duration: '',
    studio: '',
    genre: '',
    image:  null
  };

  file: File | null = null;

  submitForm() {
    var formData = new FormData();
    formData.append('title', this.movie.title);
    formData.append('duration', this.movie.duration);
    formData.append('studio', this.movie.studio);
    formData.append('genre', this.movie.genre);
    formData.append('image', this.movie.image);
    console.log('Form submitted:', formData);
    this.ApiDataService.addMovie(formData).then((response) => {
      console.log('Movie added:', response); 
    });
    this.resetForm();
  }

  onFileSelected(event: any) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.movie.image = input.files[0];
      console.log('File selected', this.movie.image);
    } else {
      this.file = null;
    }
  }

  resetForm() {
    this.movie = {
      title: '',
      duration: '',
      studio: '',
      genre: '',
      image: null
    };
    this.file = null;
  }

}
