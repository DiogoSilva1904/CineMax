import { Routes } from '@angular/router';
import { MoviesComponent } from './movies/movies.component';
import { FrontpageComponent } from './frontpage/frontpage.component';

export const routes: Routes = [
    { path: '', component: FrontpageComponent },
    { path: 'movies', component: MoviesComponent},
    { path: 'movies/:id', component: MoviesComponent}
];
