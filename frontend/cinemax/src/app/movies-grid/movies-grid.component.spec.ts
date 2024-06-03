import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoviesGridComponent } from './movies-grid.component';

describe('MoviesGridComponent', () => {
  let component: MoviesGridComponent;
  let fixture: ComponentFixture<MoviesGridComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoviesGridComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MoviesGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
