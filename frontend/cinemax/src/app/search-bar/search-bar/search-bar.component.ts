import {Component, Output, EventEmitter, inject} from '@angular/core';
import {ApiService} from "../../service/api.service";

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.css'
})
export class SearchBarComponent {
  @Output() search: EventEmitter<string> = new EventEmitter<string>();
  ApiDataService = inject(ApiService);


  constructor() { }

  onSearch(value: string): void {
    console.log(this.search.emit(value));
  }

}
