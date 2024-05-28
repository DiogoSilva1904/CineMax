import { Component } from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {MatListItem, MatNavList} from "@angular/material/list";
import {RouterLink, Router } from "@angular/router";

@Component({
  selector: 'app-client-navbar',
  standalone: true,
  imports: [
    MatIcon,
    MatListItem,
    MatNavList,
    RouterLink
  ],
  templateUrl: './client-navbar.component.html',
  styleUrl: './client-navbar.component.css'
})
export class ClientNavbarComponent {

  constructor(private router: Router) { }

  public logout() {
    this.router.navigate(['/login']);
  }

}
