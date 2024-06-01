import { Component, OnInit, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import {NgForOf, NgIf, NgClass } from "@angular/common";
import { ClientNavbarComponent } from '../client-navbar/client-navbar.component';
import { QRCodeModule } from 'angularx-qrcode';

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

  constructor() { }

  ngOnInit() {
    this.loadTickets();
  }

  private async loadTickets() {
    try {
      const username = localStorage.getItem('username');
      const ticketData = await this.apiService.getReservationsByUser(username);
      this.tickets = ticketData;
      console.log(this.tickets);
    } catch (error) {
      console.error('Error fetching tickets', error);
    }
  }

}
