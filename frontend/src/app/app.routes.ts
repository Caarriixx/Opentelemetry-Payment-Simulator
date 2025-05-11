import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from '../login/login.component';
import { AccountComponent } from '../account/account.component';
import { PaymentComponent } from '../payment/payment.component';
import { DepositComponent } from '../deposit/deposit.component';
import { HttpClientModule } from "@angular/common/http"; // HttpClientModule importado aquí
import { FormsModule } from "@angular/forms";
import { AuthService } from '../auth/auth.service'; // Servicio de autenticación
// import { AuthInterceptor } from '../auth/auth-interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { DevComponent } from './dev/dev.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, // Redirigir a la página de login
  { path: 'login', component: LoginComponent },
  { path: 'account', component: AccountComponent },
  { path: 'payment', component: PaymentComponent },
  { path: 'deposit', component: DepositComponent },
  { path: 'dev', component: DevComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes),
    HttpClientModule,  // Para manejar solicitudes HTTP, no funciona del todo bien (deprecated)
    FormsModule
  ],
  exports: [
    RouterModule
  ],
  providers: [
    // AuthService,  // Proveedor del servicio de autenticación
    // {
    //   provide: HTTP_INTERCEPTORS,  // Agrega el interceptor
    //   // useClass: AuthInterceptor,
    //   multi: true
    // }
  ]
})
export class AppRoutingModule { }


