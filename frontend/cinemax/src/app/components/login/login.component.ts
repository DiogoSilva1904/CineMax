// login.component.ts

import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
    username: string = ''; // Define username property
    password: string = ''; // Define password property
  
    constructor(private router: Router) {}
  
    redirectToOtherPage(): void {
      // Navigate to another page
      this.router.navigate(['/other-page']);
    }
  }
