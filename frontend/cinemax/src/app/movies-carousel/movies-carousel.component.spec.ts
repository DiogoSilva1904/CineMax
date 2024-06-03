import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoviesCarouselComponent } from './movies-carousel.component';

describe('MoviesCarouselComponent', () => {
  let component: MoviesCarouselComponent;
  let fixture: ComponentFixture<MoviesCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoviesCarouselComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoviesCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
