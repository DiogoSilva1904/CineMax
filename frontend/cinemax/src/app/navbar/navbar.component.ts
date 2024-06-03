import { Component } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';


@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [ MatSidenavModule,MatListModule,MatIconModule, ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  public goToSessions() {
    this.router.navigate(['/sessions']);
  }

  public goToMovies() {
    this.router.navigate(['/movies']);
  }

  public goToChangePassword() {
    this.router.navigate(['/changePassword']);
  }

  public goToLogin() {
    this.router.navigate(['/login']);
  }

}
