import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors, ValidatorFn,
  Validators
} from "@angular/forms";
import {CommonModule} from "@angular/common";
import {Balance, User} from "../models/account.model";
import {AccountService} from "../services/account.service";
import { MaterialModule } from '../app/material/material.module';
import { PaymentService } from '../services/payment.service';
import { PaymentMessage } from '../models/payment.model';
import { ActivatedRoute } from '@angular/router';
import { WebsocketService } from '../services/websocket.service';
import { TokenStorageService } from '../services/token-storage.service';


@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    MaterialModule
],
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  isConnected: boolean = false;
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
  tries: number = 3;
  userId: string = '';
  paymentData: PaymentMessage | null = null;
  paymentForm: FormGroup = this.formBuilder.group({}); // Inicialización en la declaración
  code!: number;
  recipientUsers: User[] = [];
  codeExpirationTime!: number;
  accounts: any;
  isVisualizationMode = false;

  constructor(private accountService: AccountService,
    private router: Router,
    private formBuilder: FormBuilder,
    private paymentService: PaymentService,
    private websocket: WebsocketService,
    private tokenStorage: TokenStorageService) {

    // Define los controles recipient, amount, y password con sus validaciones
    this.paymentForm = this.formBuilder.group({
      recipient: ['', Validators.required],
      amount: [
        '',
        [
          Validators.required,
          Validators.min(0.01),
          this.maxTwoDecimalValidator,
          //this.validateBalance(),
        ]
      ],
      verification: ['', [Validators.required, Validators.minLength(6), Validators.pattern(/^\d+$/)]]
    });
  }
  goBack() {
    // this.router.navigate(['/account']);
    window.location.replace("/account");
  }

  ngOnInit(): void {
    // this.account = this.accountService.getAccount();
    // // this.recipientEmails = this.paymentService.getRecipientEmails();
    // this.accounts = this.accountService.getAccounts();
    //console.log('Payment Form Initialized:', this.paymentForm.value);
    //! this.route.params.subscribe(params => {
    //!   this.userId = params['userId'];  // Captura el userId dinámicamente desde la URL
    //!   this.connectToRoom(this.userId);

    //! })
    this.websocket.onConnectionReady().subscribe((isConnected) => {
      if (isConnected) {
        this.getData();
        this.isVisualizationMode = this.accountService.getVisualizationMode();
      }
    });
  }
  getData() {
    this.accountService.getUserById(TokenStorageService.getId());
    this.accountService.getUserObservable().subscribe((user: User) => {
      this.account = user;
      this.accountService.getUsers();
      this.accountService.getUsersObservable().subscribe((users: User[]) => {
        console.log("Users recibidos")
        console.log(users)
        if(users){
          this.recipientUsers = users.filter(u => u.id !== this.account.id);
        }
      });
    });

  }

  maxTwoDecimalValidator(control: any): { [key: string]: boolean } | null {
    const value = control.value;
    if (value && !/^(\d+(\.\d{1,2})?)$/.test(value)) {
      return { maxTwoDecimals: true };
    }
    return null;
  }
  /*
  validateBalance(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!this.account) {
        return null;
      }

      const amount = parseFloat(control.value);
      if (amount > this.account.balance.balance) {
        return { exceedsBalance: true }; // Error si excede el balance
      }
      return null; // Válido si no excede el balance
    };
  }*/

  confirmPayment() {
    alert("Payment has been successfully done!");
    setTimeout(() => {
      // this.router.navigate(['/account']);
      this.goBack()
    }, 1000); // 1000 ms = 1 segundo
  }

  onSubmit() {
    console.log('Recipient:', this.paymentForm.get('recipient')?.value);
    console.log('Amount:', this.paymentForm.get('amount')?.value);
    console.log('Verification Code:', this.paymentForm.get('verification')?.value);
    if (this.paymentForm.invalid) {
      alert('Please fill in all the required fields.');
      return;
    }
    console.log("Form valido")
    const enteredCode = this.paymentForm.get('verification')?.value;

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

    const amount = parseFloat(this.paymentForm.get('amount')?.value);
    if (isNaN(amount) || amount <= 0) {
      alert('Please enter a valid amount.');
      return;
    }

    if (amount <= this.account.cc.balance) {
      console.log("Amount valido");
    /*
    if (amount > this.account.balance.balance) {
      alert('The amount exceeds your current balance.');
      return;
    }*/

    // if (!this.isConnected) {
    //   console.error('No hay conexión STOMP activa en payment');
    //   return;
    // }

    // Preparar los datos del pago
    const balance: Balance = {
      id:0,
      balance:0,
      createdAt: new Date()
    }

    const receiver: User = {
      id: this.paymentForm.get('recipient')?.value, // Ajusta según cómo obtienes el valor del formulario
      email: '', // Proporciona un valor adecuado o deja como un string vacío si no es necesario
      password: '', // Proporciona un valor adecuado o deja como un string vacío si no es necesario
      name: this.paymentForm.get('recipient')?.value, // Si necesitas usar otro valor, ajusta aquí
      surname: '', // Proporciona un valor adecuado o inicialízalo como vacío
      cc: balance, // O asigna un valor adecuado si es necesario
      dni: '', // Proporciona un valor adecuado o inicialízalo como vacío
      createdAt: new Date() // O ajusta según lo que sea necesario
    };

    // Enviar el pago a través de WebSockets (da error)
    //this.paymentService.sendPayment(this.account.userId, paymentData);

  // * Inicio Susceptible de error

    // Crear el objeto `paymentData`
    const paymentData: PaymentMessage = {
      sender: this.account as User, // Asegúrate de que `this.account` sea un objeto válido de tipo `User`
      receiver: this.paymentForm.get('recipient')?.value, // El `receiver` construido arriba
      amount: parseFloat(this.paymentForm.get('amount')?.value),
      state: 1, // Estado inicial, por ejemplo, 0 para pendiente
      date: new Date()
    }as PaymentMessage;

    // Crear `paymentData` asegurando que `sender` también sea del tipo `User`

    console.log('Preparando el pago para ser enviado:', paymentData);
    this.paymentService.sendPayment(this.account.id.toString(), paymentData); // Usa el ID o un valor que represente al `userId` en el backend
    console.log('Mensaje de pago enviado a través de WebSockets.');
    // Aquí podrías esperar una confirmación de éxito a través de WebSockets antes de llamar a confirmPayment
    // Supongamos que `this.paymentService` emite un evento cuando recibe confirmación
    //! this.paymentService.getPaymentNotification().subscribe((notification) => {
    //!     if (notification === 'Payment processed successfully') {
    //!         this.confirmPayment();
    //!     }
    //! });
    // * Fin Susceptible de error
    // Mensaje de confirmación
    alert('Payment request sent.');
    setTimeout(() => {
      this.router.navigate(['/account']);
    }, 1000);
    }else{
      console.log("Amount invalido");
    }
  }

  generarCodigo(): void {
    // Genera un código de 6 dígitos aleatorio
    this.code = Math.floor(100000 + Math.random() * 900000);
    this.codeExpirationTime = Date.now() + 30000; // 30 segundos
    // Muestra el código en un alert
    alert(`Tu código generado es: ${this.code}. Este código caducará en 30 segundos.`);
  }

  //! connectToRoom(userId: string): void {
  //!   this.paymentService.joinRoom(userId);  // Pasa el userId al servicio para manejar la conexión WebSocket
  //!   this.isConnected = true;
  //! }

  disableInteractions(): void {
    this.accountService.setVisualizationMode(true);
    this.isVisualizationMode = true;
  }
}
