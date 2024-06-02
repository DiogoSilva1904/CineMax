import { Component, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NavbarComponent } from '../navbar/navbar.component';


@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [ FormsModule, NavbarComponent ],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent {

  ApiDataService = inject(ApiService);

  constructor(private router: Router) { }

  onSubmit() {
    const username = document.getElementById('username') as HTMLInputElement;
    const password = document.getElementById('password') as HTMLInputElement;
    const newPassword = document.getElementById('newpassword') as HTMLInputElement;
    const confirmPassword = document.getElementById('confirmPassword') as HTMLInputElement;

    if(newPassword.value != confirmPassword.value){
      alert("Passwords do not match");
      return;
    }

    this.ApiDataService.changePassword(username.value, password.value, newPassword.value).then((response) => {
      if (response === 400) {
        alert('Invalid username or password');
        return;
      }
      if (response === 200) {
        this.router.navigate(['login']);
      }
    });

  }

}
