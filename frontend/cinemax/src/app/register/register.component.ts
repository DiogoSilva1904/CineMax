import { Component, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  ApiDataService = inject(ApiService);

  constructor(private router: Router) { }

  onSubmit() {
    const username = document.getElementById('username') as HTMLInputElement;
    const password = document.getElementById('password') as HTMLInputElement;
    const confirmPassword = document.getElementById('confirmPassword') as HTMLInputElement;
    const email = document.getElementById('email') as HTMLInputElement;

    if(password.value != confirmPassword.value){
      alert("Passwords do not match");
      return;
    }

    this.ApiDataService.registerUser(username.value, password.value, email.value).then((response) => {
      if (response === 400) {
        alert('Username already exists');
        return;
      }
      if (response === 200) {
        this.router.navigate(['login']);
      }
    });
  }

  onCreateAccount() {
    this.router.navigate(['login']);
  }

  onBackToLogin() {
    this.router.navigate(['login']);
  }

}
