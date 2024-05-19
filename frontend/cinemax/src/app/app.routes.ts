import { Routes } from '@angular/router';
import { MoviesComponent } from './movies/movies.component';
import { FrontpageComponent } from './frontpage/frontpage.component';
import { AddMovieComponent } from './add-movie/add-movie.component';
import { QrcodepageComponent } from './qrcodepage/qrcodepage.component';
import { BookingPageComponent } from './booking-page/booking-page.component';
import { SessionsComponent } from './sessions/sessions.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent},
    { path: '', redirectTo: 'login', pathMatch: 'full' },
    { path: 'frontpage', component: FrontpageComponent },
    { path: 'movies', component: MoviesComponent},
    { path: 'addmovie', component: AddMovieComponent},
    { path: "buyTicket", component:BookingPageComponent},
    { path: "qrcode", component: QrcodepageComponent},
    { path: "sessions", component: SessionsComponent},
    { path: "register", component: RegisterComponent},
    { path: "changePassword", component: ChangePasswordComponent}
];
