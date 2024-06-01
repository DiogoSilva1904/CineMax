import { Component, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { tick } from '@angular/core/testing';

@Component({
  selector: 'app-validate-ticket',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './validate-ticket.component.html',
  styleUrl: './validate-ticket.component.css'
})
export class ValidateTicketComponent {

  ApiDataService = inject(ApiService);
  ticketid: any = '';
  
  constructor(private router: Router, private route: ActivatedRoute) { 
    this.ticketid = this.route.snapshot.paramMap.get('id');
  }

  ngOnInit() {
    //this.clearLocalStorage();
  }

  clearLocalStorage() {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
    }
  }

  onSubmit() {
    const username = document.getElementById('username') as HTMLInputElement;
    const password = document.getElementById('password') as HTMLInputElement;
    console.log(this.ticketid);

    this.ApiDataService.loginUser(username.value, password.value).then((response) => {
      if (response.status === 400) {
        alert('Invalid username or password');
        return;
      }
      if (response.status === 200) {
        console.log(response.data.role);

        if (response.data.role === 'ROLE_ADMIN') {
          this.ApiDataService.validateTicket(this.ticketid, response.data.jwt).then((response) => {
            console.log(response.status);
            if (response.status === 200) {
              alert('Ticket validated');
            } else {
              alert('Ticket already validated');
            }
          });
        } else {
          alert('You are not an admin');
        }
      }
    });
  }

}
