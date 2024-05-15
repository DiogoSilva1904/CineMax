import { Routes } from '@angular/router';
import { MoviesComponent } from './movies/movies.component';
import { FrontpageComponent } from './frontpage/frontpage.component';
import { AddMovieComponent } from './add-movie/add-movie.component';
import { QrcodepageComponent } from './qrcodepage/qrcodepage.component';

export const routes: Routes = [
    { path: '', component: FrontpageComponent },
    { path: 'movies', component: MoviesComponent},
    { path: 'addmovie', component: AddMovieComponent},
    { path: "qrcode", component: QrcodepageComponent}
];
