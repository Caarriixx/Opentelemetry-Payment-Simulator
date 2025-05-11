import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AccountComponent } from './account.component';

@NgModule({
  declarations: [],
  imports: [
    CommonModule, // Importa otros módulos que necesites
    RouterModule,
    AccountComponent
  ],
  exports: [AccountComponent] // Exporta componentes si los necesitas en otros módulos
})
export class AccountModule { }
