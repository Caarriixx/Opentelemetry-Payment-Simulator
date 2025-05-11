import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { CommonModule } from '@angular/common';  // Importar CommonModule
import { HttpClientModule } from '@angular/common/http';
import { AccountModule } from '../account/account.module';
import {AccountService} from "../services/account.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {AppRoutingModule} from "./app.routes";
import {PaymentComponent} from "../payment/payment.component";
import {LoginComponent} from "../login/login.component";
import {DepositComponent} from "../deposit/deposit.component";
import {AccountComponent} from "../account/account.component"; // Asegúrate de que la ruta sea correcta
import { WebsocketService } from '../services/websocket.service';
import { SessionService } from '../services/session.service';
import { MaterialModule } from './material/material.module';

@NgModule({
  declarations: [

  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AccountModule,
    AppComponent,
    CommonModule,
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    PaymentComponent,
    LoginComponent,
    DepositComponent,
    AccountComponent
  ],
  providers: [
    AccountService,
    WebsocketService,
    SessionService
  ],
})
export class AppModule { }

