import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-client-movie-card',
  standalone: true,
  imports: [],
  templateUrl: './client-movie-card.component.html',
  styleUrl: './client-movie-card.component.css'
})
export class ClientMovieCardComponent {

  @Input() movie: any;

}
