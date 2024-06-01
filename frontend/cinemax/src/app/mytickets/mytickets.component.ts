import { Component, OnInit, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import {NgForOf, NgIf, NgClass } from "@angular/common";
import { ClientNavbarComponent } from '../client-navbar/client-navbar.component';
import { QRCodeModule } from 'angularx-qrcode';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-mytickets',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    ClientNavbarComponent,
    QRCodeModule,
    NgClass
  ],
  templateUrl: './mytickets.component.html',
  styleUrls: ['./mytickets.component.css']
})
export class MyticketsComponent implements OnInit {

  private apiService = inject(ApiService);

  tickets: any = [];

  constructor(private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.loadTickets();
  }

  async loadImage(ticket: any): Promise<void> {
    const movie = ticket.session.movie;
    if (movie.imagePath == null) {
      ticket.imageUrl = 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png';
    }
    else {
      const imageBlob = await this.apiService.getImage(movie.imagePath);
      if (imageBlob) {
        const objectURL = URL.createObjectURL(imageBlob);
        ticket.imageUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL);
      }
    }
  }

  private async loadTickets() {
    try {
      const username = localStorage.getItem('username');
      const ticketData = await this.apiService.getReservationsByUser(username);
      for (const ticket of ticketData) {
        this.loadImage(ticket);
      }
      this.tickets = ticketData;
      console.log(this.tickets);
    } catch (error) {
      console.error('Error fetching tickets', error);
    }
  }

}
