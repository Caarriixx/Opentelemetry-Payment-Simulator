import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {CommonModule} from "@angular/common";
import {Balance, User} from "../models/account.model";
import {AccountService} from "../services/account.service";
import { MaterialModule } from '../app/material/material.module';
import { PaymentService } from '../services/payment.service';
import { PaymentMessage } from '../models/payment.model';
import { ActivatedRoute } from '@angular/router';
import { WebsocketService } from '../services/websocket.service';
import { TokenStorageService } from '../services/token-storage.service';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors, ValidatorFn,
  Validators
} from "@angular/forms";
import {DepositService} from "../services/deposit.service";

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MaterialModule,
    FormsModule
  ],
  styleUrls: ['./deposit.component.css']
})
export class DepositComponent implements OnInit {
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
  isConnected: boolean = false;
  tries: number = 3;
  userId: string = '';
  depositForm: FormGroup = this.formBuilder.group({}); // Inicialización en la declaración
  code!: number;
  codeExpirationTime!: number;
  isVisualizationMode = false;

  constructor(private accountService: AccountService,
              private router: Router,
              private formBuilder: FormBuilder,
              private depositService: DepositService,
              private websocket: WebsocketService) {    // Modificamos la validación de la contraseña para que requiera al menos 6 caracteres

    // Define los controles recipient, amount, y password con sus validaciones
    this.depositForm = this.formBuilder.group({
      amount: [
        '',
        [
          Validators.required,
          Validators.min(0.01),
          this.maxTwoDecimalValidator,
        ]
      ],
      verification: ['', [Validators.required, Validators.minLength(6), Validators.pattern(/^\d+$/)]]
    });
  }

  // Custom validator for max two decimal places
  maxTwoDecimalValidator(control: any): { [key: string]: boolean } | null {
    const value = control.value;
    if (value && !/^(\d+(\.\d{1,2})?)$/.test(value)) {
      return { maxTwoDecimals: true };
    }
    return null;
  }

  ngOnInit(): void {
    this.websocket.onConnectionReady().subscribe((isConnected) => {
      if (isConnected) {
        this.getData();
      }
    });
  }

  getData() {
    this.accountService.getUserById(TokenStorageService.getId());
    this.accountService.getUserObservable().subscribe((user: User) => {
      this.account = user;
    });
  }

  onSubmit() {
    console.log('Amount:', this.depositForm.get('amount')?.value);
    console.log('Verification Code:', this.depositForm.get('verification')?.value);
    if (this.depositForm.invalid) {
      alert('Please correct the errors before submitting.');
      return;
    }
    console.log("Form valido")
    const enteredCode = this.depositForm.get('verification')?.value;

    if (Date.now() > this.codeExpirationTime) {
      alert('El código ha caducado. Por favor, genera un nuevo código.');
      return;
    }

    if (this.tries > 0 && enteredCode !== String(this.code)) {
      this.tries--;
      alert(`Code do not match. You have ${this.tries} tries left.`);
      if (this.tries === 0) {
        alert('You have exceeded the maximum tries. You are now in visualization mode.');
        this.disableInteractions();
        this.router.navigate(['/account']);
      }
      return;
    }

    const amount = parseFloat(this.depositForm.get('amount')?.value);
    if (isNaN(amount) || amount <= 0) {
      alert('Please enter a valid amount.');
      return;
    }

    if (amount > 10000) {
      alert('The amount cannot exceed 10,000.');
      return;
    }

    const balance: Balance = {
      id:0,
      balance:0,
      createdAt: new Date()
    }

    const depositData: PaymentMessage = {
      sender: this.account as User, // Asegúrate de que `this.account` sea un objeto válido de tipo `User`
      receiver: this.account as User, // El `receiver` construido arriba
      amount: parseFloat(this.depositForm.get('amount')?.value),
      state: 1, // Estado inicial, por ejemplo, 0 para pendiente
      date: new Date()
    }as PaymentMessage;

    console.log('Preparando el deposito para ser enviado:', depositData);
    this.depositService.sendDeposit(this.account.id.toString(), depositData); // Usa el ID o un valor que represente al `userId` en el backend
    console.log('Mensaje de deposito enviado a través de WebSockets.');

    alert('Deposit request sent.');
    setTimeout(() => {
      this.goBack()
    }, 1000);

  }

  generarCodigo(): void {
    // Genera un código de 6 dígitos aleatorio
    this.code = Math.floor(100000 + Math.random() * 900000);
    this.codeExpirationTime = Date.now() + 30000; // 30 segundos
    // Muestra el código en un alert
    alert(`Tu código generado es: ${this.code}. Este código caducará en 30 segundos.`);
  }

  goBack() {
    console.log("vuelve account");
    // this.router.navigate(['/account']);
    window.location.replace("/account");
  }

  disableInteractions(): void {
    this.accountService.setVisualizationMode(true);
    this.isVisualizationMode = true;
  }
}
