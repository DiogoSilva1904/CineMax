import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { BrowserModule } from '@angular/platform-browser';
import { LoginComponent } from './app/components/login/login.component';
import { ClientMainPage } from './app/client/clientMainPage.component';
import { Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';
// Define your routes

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'client', component: ClientMainPage },
  { path: '', redirectTo: '/login', pathMatch: 'full' }, 
  { path: '**', redirectTo: '/login' } 
];

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
