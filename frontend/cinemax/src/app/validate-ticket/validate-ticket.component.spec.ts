import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidateTicketComponent } from './validate-ticket.component';

describe('ValidateTicketComponent', () => {
  let component: ValidateTicketComponent;
  let fixture: ComponentFixture<ValidateTicketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValidateTicketComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ValidateTicketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
