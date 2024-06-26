import { Routes } from '@angular/router';
import { MoviesComponent } from './movies/movies.component';
import { AddMovieComponent } from './add-movie/add-movie.component';
import { QrcodepageComponent } from './qrcodepage/qrcodepage.component';
import { BookingPageComponent } from './booking-page/booking-page.component';
import { SessionsComponent } from './sessions/sessions.component';
import {MyticketsComponent} from "./mytickets/mytickets.component";
import { DigitalSignagePageComponent } from './digital-signage-page/digital-signage-page.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { HomePageComponent } from './home-page/home-page.component';
import { MovieSessionsComponent } from './movie-sessions/movie-sessions.component';
import {FrontpageComponent} from "./frontpage/frontpage.component";
import { ValidateTicketComponent } from './validate-ticket/validate-ticket.component';
    



export const routes: Routes = [
    { path: 'login', component: LoginComponent},
    { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'homepage', redirectTo: 'frontpage', pathMatch: 'full'},
  { path: 'frontpage', component: FrontpageComponent },
    { path: 'movies', component: MoviesComponent},
    { path: 'addmovie', component: AddMovieComponent},
    { path: "buyTicket", component:BookingPageComponent},
    { path: "qrcode", component: QrcodepageComponent},
    { path: "sessions", component: SessionsComponent},
    { path: "myTickets", component: MyticketsComponent},
    { path: "signage", component: DigitalSignagePageComponent},
    { path: "register", component: RegisterComponent},
    { path: "changePassword", component: ChangePasswordComponent},
    { path: 'movie-sessions/:id', component: MovieSessionsComponent},
    { path: 'seat/:id', component:  BookingPageComponent},
    { path: 'validate/:id', component: ValidateTicketComponent}

];
