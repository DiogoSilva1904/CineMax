import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieSessionsComponent } from './movie-sessions.component';

describe('MovieSessionsComponent', () => {
  let component: MovieSessionsComponent;
  let fixture: ComponentFixture<MovieSessionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MovieSessionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MovieSessionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
