import { Component } from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';


@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [ MatSidenavModule ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

}
