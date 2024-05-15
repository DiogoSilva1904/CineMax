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

  movie = {
    title: '',
    duration: null,
    studio: '',
    genre: '',
    //poster: null
  };

  submitForm() {
    // Handle form submission logic here
    console.log('Form submitted:', this.movie);
    this.ApiDataService.addMovie(this.movie).then((response) => {
      console.log('Movie added:', response);
    });
    // clear form
    this.movie = {
      title: '',
      duration: null,
      studio: '',
      genre: '',
      //poster: null
    };
  }

  onFileSelected(event: any) {
    // Handle file selection
    //this.movie.poster = event.target.files[0];
    console.log('File selected');
  }

}
