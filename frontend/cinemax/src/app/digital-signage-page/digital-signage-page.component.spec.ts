import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DigitalSignagePageComponent } from './digital-signage-page.component';

describe('DigitalSignagePageComponent', () => {
  let component: DigitalSignagePageComponent;
  let fixture: ComponentFixture<DigitalSignagePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DigitalSignagePageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DigitalSignagePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
