import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-carousel',
  templateUrl: './carousel.component.html',
  standalone: true,
  styleUrls: ['./carousel.component.css']
})
export class CarouselComponent implements OnInit {
  items: string[] = [];
  currentIndex = 0;

  ngOnInit() {
    // Define static image URLs here
    this.items = [
      'https://images.squarespace-cdn.com/content/v1/54ec2161e4b01dbc251cbdae/8109841b-663b-476b-beee-e72bd234ee95/best-ideas-For-Successful-Banner-Advertising-30.jpg',
      'https://i.pinimg.com/736x/c9/0a/d2/c90ad2944dc5ede35976e544c5e6923b.jpg',
      'https://www.befunky.com/images/wp/wp-2016-06-banner-maker-featured-file-new.png?auto=avif,webp&format=jpg&width=1150&crop=16:9'
    ];
  }

  prevSlide() {
    this.currentIndex = (this.currentIndex === 0) ? this.items.length - 1 : this.currentIndex - 1;
  }

  nextSlide() {
    this.currentIndex = (this.currentIndex === this.items.length - 1) ? 0 : this.currentIndex + 1;
  }
}
