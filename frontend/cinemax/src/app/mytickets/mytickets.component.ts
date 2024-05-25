import { Component, OnInit, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-mytickets',
  standalone: true,
  imports: [
    NgIf,
    NgForOf
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
      const ticketData = await this.apiService.getReservationById("1");
      this.tickets = [ticketData];
      console.log(this.tickets);
    } catch (error) {
      console.error('Error fetching tickets', error);
    }
  }

}
