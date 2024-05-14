// app.module.ts

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Import FormsModule if needed
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { ClientMainPage } from './client/clientMainPage.component';

// Define your routes
const routes = [
  { path: 'login', component: LoginComponent },
  { path: 'client', component: ClientMainPage },
  { path: '', redirectTo: '/login', pathMatch: 'full' }, 
  { path: '**', redirectTo: '/login' } 
];


export class AppModule { } 
