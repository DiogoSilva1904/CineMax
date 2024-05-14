import { Component, OnInit, Input, TemplateRef } from '@angular/core';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-movies-grid',
  standalone: true,
  imports: [NgForOf,],
  templateUrl: './movies-grid.component.html',
  styleUrl: './movies-grid.component.css'
})
export class MoviesGridComponent implements OnInit {
  @Input() limit: number = 0;
  @Input() columns: number = 4;
  @Input() exclude?: number | number[];
  movies: any[] = [];
  previewUrl = '';

  constructor(private dialog: MatDialog, private sanitizer: DomSanitizer, private router: Router) { }

  ngOnInit() {
    this.getMovies();
  }

  getMovies() {
    console.log(this.limit, this.exclude);
    //this.db.getMovies(this.limit, this.exclude).subscribe(movies => this.movies = movies);;
  }

  openModal(template: TemplateRef<any>, previewUrl: string) {
    this.previewUrl = previewUrl;
    this.router.navigate(['/movies']);
    //this.dialog.open(template, { width: '80%', height: '80%' });
  }

  closeDialog(): void {
    this.dialog.closeAll();
  }

  getPreviewUrl() {
    return this.sanitizer.bypassSecurityTrustResourceUrl(this.previewUrl);
  }

  getEmbedUrl(url: string) {
    return url.replace('https://www.youtube.com/watch?v=', 'https://www.youtube.com/embed/');
  }
}
