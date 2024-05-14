import { Component, OnInit } from '@angular/core';
import { MoviesGridComponent } from '../movies-grid/movies-grid.component';

@Component({
  selector: 'app-frontpage',
  standalone: true,
  imports: [MoviesGridComponent,],
  templateUrl: './frontpage.component.html',
  styleUrl: './frontpage.component.css'
})
export class FrontpageComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
