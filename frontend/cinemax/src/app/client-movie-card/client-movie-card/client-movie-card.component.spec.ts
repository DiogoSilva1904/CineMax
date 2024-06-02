import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientMovieCardComponent } from './client-movie-card.component';

describe('ClientMovieCardComponent', () => {
  let component: ClientMovieCardComponent;
  let fixture: ComponentFixture<ClientMovieCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientMovieCardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ClientMovieCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
