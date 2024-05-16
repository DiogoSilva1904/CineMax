import { Routes } from '@angular/router';
import { MoviesComponent } from './movies/movies.component';
import { AddMovieComponent } from './add-movie/add-movie.component';
import { QrcodepageComponent } from './qrcodepage/qrcodepage.component';
import { BookingPageComponent } from './booking-page/booking-page.component';
import { SessionsComponent } from './sessions/sessions.component';
import { HomePageComponent } from './home-page/home-page.component';
import { MovieSessionsComponent } from './movie-sessions/movie-sessions.component';

export const routes: Routes = [
    { path: 'movies', component: MoviesComponent},
    { path: 'addmovie', component: AddMovieComponent},
    { path: "buyTicket", component:BookingPageComponent},
    { path: "qrcode", component: QrcodepageComponent},
    { path: "sessions", component: SessionsComponent},
    { path: '', component: HomePageComponent},
    { path: 'movie-sessions/:id', component: MovieSessionsComponent},
    { path: 'seat/:id', component:  BookingPageComponent}
];
