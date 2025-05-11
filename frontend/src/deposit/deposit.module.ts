import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DepositComponent } from './deposit.component';
import {AccountComponent} from "../../account/account.component";

@NgModule({
  declarations: [],
  imports: [
    AccountComponent,
    CommonModule,
    RouterModule,
    DepositComponent
  ]
})
export class DepositModule { }
