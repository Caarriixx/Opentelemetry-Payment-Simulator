import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';

import {MatCardModule} from '@angular/material/card';
import {MatTableModule} from '@angular/material/table';
import { MatOption, MatSelect } from '@angular/material/select';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatPaginator } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import {MatChipInputEvent, MatChipsModule, MatChipListbox} from '@angular/material/chips';
import { FormsModule } from '@angular/forms';
// Importa aquí otros módulos que necesites

@NgModule({
  imports: [
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatTableModule,
    MatSelect,
    MatOption,
    MatPaginator,
    MatSortModule,
    MatMenuTrigger,
    MatMenuModule,
    MatFormFieldModule,
    MatChipsModule,
    MatChipListbox,
    FormsModule
    // signal
    // Agrega otros módulos aquí
  ],
  exports: [
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatFormFieldModule,
    MatInputModule,
    MatCardModule,
    MatTableModule,
    MatSelect,
    MatOption,
    MatPaginator,
    MatSortModule,
    MatMenuTrigger,
    MatMenuModule,
    MatFormFieldModule,
    MatChipsModule,
    MatChipListbox,
    FormsModule
    // signal
    // Exporta los mismos módulos
  ]
})
export class MaterialModule { }
