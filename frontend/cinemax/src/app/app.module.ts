// app.module.ts

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'; // Import FormsModule
import { RouterModule, Routes } from '@angular/router'; // Import RouterModule if needed
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { ClientMainPage } from './client/clientMainPage.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'client', component: ClientMainPage },
  { path: '', redirectTo: '/login', pathMatch: 'full' }, 
  { path: '**', redirectTo: '/login' } 
];

@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    BrowserModule,
    FormsModule, // Include FormsModule here
    RouterModule.forRoot(routes) // Include RouterModule if needed
  ],
  providers: [],
})

export class AppModule { } 
