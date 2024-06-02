import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyticketsComponent } from './mytickets.component';

describe('MyticketsComponent', () => {
  let component: MyticketsComponent;
  let fixture: ComponentFixture<MyticketsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyticketsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MyticketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
