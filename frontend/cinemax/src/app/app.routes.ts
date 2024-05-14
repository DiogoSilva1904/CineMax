import { RouterModule, Routes } from '@angular/router';
import { ClientMainPage } from './client/mainPage/clientMainPage.component';
import { LoginComponent } from './components/login/login.component';
import { NgModule } from '@angular/core';

export const routes: Routes = [{ path: 'login', component: LoginComponent },
{ path: 'client', component: ClientMainPage },
{ path: '', redirectTo: '/login', pathMatch: 'full' }, 
{ path: '**', redirectTo: '/login' } ];
@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class AppRoutingModule { }