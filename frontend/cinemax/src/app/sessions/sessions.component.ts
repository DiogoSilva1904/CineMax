import { Component, inject } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import {MatTableModule} from '@angular/material/table';
import { ApiService } from '../service/api.service';
import {MatIconModule} from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { NgFor } from '@angular/common';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}



@Component({
  selector: 'app-sessions',
  standalone: true,
  imports: [ NavbarComponent, MatTableModule, MatIconModule, FormsModule, NgFor ],
  templateUrl: './sessions.component.html',
  styleUrl: './sessions.component.css'
})
export class SessionsComponent {

  ApiDataService = inject(ApiService);

  displayedColumns: string[] = ['position', 'name', 'weight', 'symbol', 'room', 'available_seats', 'delete'];

  sessions: any = [];
  movies: any = [];
  rooms: any = [];
  selectedMovie = null;
  selectedRoom = null;

  constructor() { 
      
      this.ApiDataService.getSessions().then((sessions) => {
        this.sessions = sessions;
        console.log(this.sessions);
      });
      this.ApiDataService.getMovies().then((movies) => {
        this.movies = movies;
        console.log(this.movies);
      });
      this.ApiDataService.getRooms().then((rooms) => {
        this.rooms = rooms;
        console.log(this.rooms);
      });
  }

  addSession() {
    console.log('Adding session');
    console.log('Selected movie:', this.selectedMovie);
    console.log('Selected room:', this.selectedRoom);

    if (!this.selectedMovie || !this.selectedRoom) {
      console.error('Invalid movie or room');
      return;
    }

    const date = document.getElementById('date') as HTMLInputElement;
    const time = document.getElementById('time') as HTMLInputElement;

    const session = {
      movie : this.selectedMovie,
      room : this.selectedRoom,
      date : date.value,
      time : time.value,
    };

    console.log('Session:', session);

    this.ApiDataService.addSession(session).then((response) => {
      console.log('Response:', response);
      if (response) {
        this.sessions.push(response);
      }
    });

    this.closeAddModal();
    location.reload();
  }

  closeAddModal() {
    const modal = document.getElementById('addModal');
    if (modal) {
      modal.style.display = 'none';
    }
  }

  showAddStudentModal() {
    const modal = document.getElementById('addModal');
    if (modal) {
      modal.style.display = 'block';
    }
  }

  deleteSession(sessionId: string) {
    console.log('Deleting session:', sessionId);
    this.ApiDataService.deleteSession(sessionId).then((response) => {
      console.log('Response:', response);
      location.reload();
    });
  }

}
