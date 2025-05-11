import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AccountComponent } from '../account/account.component';
import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {RouterOutlet} from "@angular/router";
import {NgOptimizedImage} from "@angular/common";
import { Router } from '@angular/router';
import { MaterialModule } from './material/material.module';
import { FooterComponent } from "./footer/footer.component";
import { HeaderComponent } from "./header/header.component";
import { NotificationComponent } from "./notification/notification.component";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: true,
  imports: [
    FormsModule,
    RouterOutlet,
    MaterialModule,
    FooterComponent,
    HeaderComponent,
    NotificationComponent
],
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title: 'Payment Simulator' | undefined;
  constructor(private router: Router) {}

}

