import { Component } from '@angular/core';
import { QRCodeModule } from 'angularx-qrcode';

@Component({
  selector: 'app-qrcodepage',
  standalone: true,
  imports: [QRCodeModule],
  templateUrl: './qrcodepage.component.html',
  styleUrl: './qrcodepage.component.css'
})
export class QrcodepageComponent {
}
