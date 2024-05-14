import { Routes } from '@angular/router';
import { MoviesComponent } from './movies/movies.component';
import { AddMovieComponent } from './add-movie/add-movie.component';

export const routes: Routes = [
    { path: '', component: MoviesComponent },
    { path: 'addmovie', component: AddMovieComponent }
];
