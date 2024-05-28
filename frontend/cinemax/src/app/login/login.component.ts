import { Component, inject } from '@angular/core';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ FormsModule ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  ApiDataService = inject(ApiService);

  constructor(private router: Router) { }

  ngOnInit() {
    this.clearLocalStorage();
  }

  clearLocalStorage() {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
    }
  }

  onSubmit() {
    const username = document.getElementById('username') as HTMLInputElement;
    const password = document.getElementById('password') as HTMLInputElement;

    this.ApiDataService.loginUser(username.value, password.value).then((response) => {
      if (response.status === 400) {
        alert('Invalid username or password');
        return;
      }
      if (response.status === 200) {
     
        localStorage.setItem('token', response.data.jwt);
        localStorage.setItem('username', username.value);
        localStorage.setItem('role', response.data.role);

        this.router.navigate(['homepage']);
      }
    });
  }

  onCreateAccount() {
    this.router.navigate(['register']);
  }

  


}
