import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QrcodepageComponent } from './qrcodepage.component';

describe('QrcodepageComponent', () => {
  let component: QrcodepageComponent;
  let fixture: ComponentFixture<QrcodepageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QrcodepageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(QrcodepageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
