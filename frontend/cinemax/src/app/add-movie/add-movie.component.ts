import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-movie',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './add-movie.component.html',
  styleUrl: './add-movie.component.css'
})
export class AddMovieComponent {

  movie = {
    title: '',
    duration: null,
    description: '',
    genre: '',
    poster: null
  };

  submitForm() {
    // Handle form submission logic here
    console.log('Form submitted:', this.movie);
    // You can send the form data to a backend service for further processing
  }

  onFileSelected(event: any) {
    // Handle file selection
    this.movie.poster = event.target.files[0];
    console.log('File selected');
  }

}
