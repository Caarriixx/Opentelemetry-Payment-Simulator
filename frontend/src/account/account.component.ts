import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { AccountService } from '../services/account.service';
import {CommonModule} from "@angular/common";
import {RouterLink} from "@angular/router";
import { User } from '../models/account.model';
import { MaterialModule } from '../app/material/material.module';
import {Router} from "@angular/router";
import { Payment, PaymentMessage } from '../models/payment.model';
import { access } from 'fs';
import { PaymentService } from '../services/payment.service';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
import { WebsocketService } from '../services/websocket.service';
import { TokenStorageService } from '../services/token-storage.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  standalone: true,
  imports: [CommonModule, RouterLink, MaterialModule],
  styleUrls: ['./account.component.css'],
  host: { 'ngSkipHydration': '' }  // Deshabilitar la hidratación
})
export class AccountComponent implements OnInit, AfterViewInit {


  account: User = {
    id: 0,
    name: '',
    surname: '',
    password: '',
    email: '',
    cc: {
      id: 0,
      balance: 0,
      createdAt: new Date()
    },
    dni: '',
    createdAt: new Date()
  };

  isVisualizationMode = false; // Nueva bandera para el modo solo visualización
  paymentHistory!: Payment[];
  dataSource!: MatTableDataSource<Payment>;
  displayedColumns: string[] = ['sender', 'receiver','amount','state', 'date'];
  paymentObservable: any;
  EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
  EXCEL_EXTENSION = '.xlsx';

  activeFilters: string[] = [];
  searchText: string = '';  // Texto de búsqueda

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private accountService: AccountService,
    private paymentService: PaymentService,
    private websocket: WebsocketService,
    private tokenStorage: TokenStorageService,
    private router: Router
  ) {
      this.websocket.onConnectionReady().subscribe((isConnected) => {
        if (isConnected) {
          this.getData();
        }
      });
    }

  ngOnInit(): void{
    console.log('AccountComponent initialized');

    this.isVisualizationMode = this.accountService.getVisualizationMode();
    console.log("Visualization mode on init:", this.isVisualizationMode);
    this.paymentService.getPaymentNotification().subscribe(() => {
      this.isVisualizationMode = this.accountService.getVisualizationMode();
    });
    this.blockBrowserNavigation();
    this.getData();
  }

  makePayment() {
    if (this.isVisualizationMode) {
      console.log('No se permite realizar pagos en este modo.');
      return;
    }
    if (!this.isVisualizationMode) {
      window.location.replace("payment");
    }
  }

  makeDeposit() {
    if (this.isVisualizationMode) {
      console.log('No se permite realizar depositos en este modo.');
      return;
    }
    if (!this.isVisualizationMode) {
      window.location.replace("deposit");
    }
  }

  downloadExcel(): void {
    // Crear una versión aplanada de paymentHistory con los campos sender y receiver como strings
    const flatData = this.paymentHistory.map(payment => ({
      sender: payment.sender.name,
      receiver: payment.receiver.name,
      amount: payment.amount,
      state: payment.state.description,
      date: payment.date ? new Date(payment.date).toLocaleString() : 'N/A'
    }));

    // Convertir la tabla de historial de pagos a una hoja de Excel
    const worksheet = XLSX.utils.json_to_sheet(flatData);
    const workbook = { Sheets: { 'data': worksheet }, SheetNames: ['data'] };
    const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });

    // Llamar a la función saveExcelFile para guardar el archivo
    this.saveExcelFile(excelBuffer, "HistorialPagos");
  }

  private saveExcelFile(buffer: any, fileName: string): void {
    const data: Blob = new Blob([buffer], { type: this.EXCEL_TYPE });
    FileSaver.saveAs(data, fileName + this.EXCEL_EXTENSION);
  }

  getData() {
    this.accountService.getUserById(TokenStorageService.getId());
    this.accountService.getUserObservable().subscribe((user: User) => {
      this.account = user;
      this.paymentService.getPaymentsByUser(this.account);
      this.paymentService.getPaymentsObservable().subscribe((payments: Payment[]) => {
        // console.log("Payments recibidos")
        // console.log(payments)
        if(payments){
          this.paymentHistory = payments;
          this.dataSource = new MatTableDataSource(payments);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sortingDataAccessor = (item: Payment, property: string) => {
            // console.log(`Payment: ${item}`);
            // console.log(`Property: ${property}`);
            switch (property) {
              case 'sender':
                return item.sender.name.toLowerCase();
              case 'receiver':
                return item.receiver.name.toLowerCase();
              case 'state':
                return item.state.description.toLowerCase();
              default:
                return (item as any)[property];
            }
          };
          this.dataSource.sort = this.sort;
        }
      });
    });
  }

  // applyFilter(event: Event) {
  //   const filterValue = (event.target as HTMLInputElement).value;
  //   this.dataSource.filter = filterValue.trim().toLowerCase();

  //   // Para que el filtro aplique también en los objetos anidados
  //   this.dataSource.filterPredicate = (data: Payment, filter: string) => {
  //     const dataStr = `${data.id} ${data.sender.name} ${data.receiver.name} ${data.amount} ${data.state.description} ${data.date}`;
  //     // console.log(`Buscando en: ${dataStr}`);
  //     return dataStr.toLowerCase().includes(filter);
  //   };

  //   if (this.dataSource.paginator) {
  //     this.dataSource.paginator.firstPage();
  //   }

  // }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sortingDataAccessor = (item: Payment, property: string) => {
      switch (property) {
        case 'sender':
          return item.sender.name.toLowerCase();
        case 'receiver':
          return item.receiver.name.toLowerCase();
        case 'state':
          return item.state.description.toLowerCase();
        default:
          return (item as any)[property];
      }
    };
    this.dataSource.sort = this.sort
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.searchText = filterValue.trim().toLowerCase();
    this.filterData();
  }

  // Método para filtrar la data según la barra de búsqueda y los filtros activos
  filterData() {
    let filteredData = this.paymentHistory;

    // Filtrar por búsqueda (searchText)
    if (this.searchText) {
      filteredData = filteredData.filter(row =>
        row.sender.name.toLowerCase().includes(this.searchText) ||
        row.receiver.name.toLowerCase().includes(this.searchText) ||
        row.state.description.toLowerCase().includes(this.searchText)
      );
    }

    // Filtrar por filtros activos (chips)
    if (this.activeFilters.length > 0) {
      filteredData = filteredData.filter(data =>
        this.activeFilters.some(filter => `${data.id} ${data.sender.name} ${data.receiver.name} ${data.amount} ${data.state.description} ${data.date}`.toLowerCase().includes(filter.toLowerCase()))
      );
    }

    // Actualizar la fuente de datos de la tabla
    this.dataSource = new MatTableDataSource(filteredData);
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  // Método para añadir un filtro activo (chip)
  addFilter(filter: string) {
    if (!this.activeFilters.includes(filter)) {
      this.activeFilters.push(filter);
      this.filterData(); // Reaplicar el filtro después de agregar
    }
  }

  // Método para eliminar un filtro activo (chip)
  removeFilter(filter: string) {
    const index = this.activeFilters.indexOf(filter);
    if (index >= 0) {
      this.activeFilters.splice(index, 1);
      this.filterData(); // Reaplicar el filtro después de eliminar
    }
  }

  blockBrowserNavigation(): void {
    if (this.isVisualizationMode) {
      // Añadir un estado vacío al historial para evitar navegación hacia atrás
      history.pushState(null, '', window.location.href);

      // Escuchar eventos de navegación hacia atrás
      window.addEventListener('popstate', () => {
        if (this.isVisualizationMode) {
          // Volver a la misma página y evitar navegación hacia atrás
          history.pushState(null, '', window.location.href);
        }
      });
    }
  }

  ngOnDestroy(): void {
    // Remover el evento popstate al destruir el componente
    window.removeEventListener('popstate', () => {
      history.pushState(null, '', window.location.href);
    });
  }
}
