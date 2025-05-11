import { Injectable } from '@angular/core';
import { WebsocketService } from './websocket.service';
import { Subject, Observable } from 'rxjs';
import { Payment, PaymentMessage } from '../models/payment.model';
import { Balance, User } from '../models/account.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  ////////////////////////////////////////////////////////////

  private balance1: Balance = {
    id: 1,
    balance: 2500,
    createdAt: new Date()
  }

  private balance2: Balance = {
    id: 2,
    balance: 3500,
    createdAt: new Date()
  }

  private account: User = {
    id: 1,
    name: 'Ovidiu',
    surname: 'Teodor',
    password: '123456',
    email: 'ovi@xeridia.com',
    cc: this.balance1,
    dni: '12345678A',
    createdAt: new Date()
  };
  private account2: User = {
    id: 2,
    name: 'Rubén',
    surname: 'BA',
    password: '123456',
    email: 'rbn@xeridia.com',
    cc: this.balance2,
    dni: '12345678A',
    createdAt: new Date()
  };

  paymentHistory: PaymentMessage[] = [
    {sender: this.account, receiver: this.account2, amount: 100, state: 1, date: new Date()},
    {sender: this.account, receiver: this.account2, amount: 100, state: 2, date: new Date()},
    {sender: this.account2, receiver: this.account, amount: 100, state: 3, date: new Date()},
    {sender: this.account2, receiver: this.account, amount: 100, state: 4, date: new Date()},

  ];
  ////////////////////////////////////////////////////////////////

  private paymentsSubject = new Subject<Payment[]>();
  private paymentSubject = new Subject<PaymentMessage>();
  private paymentNotification = new Subject<Payment>();
  private visualizationMode = new Subject<boolean>(); // Para notificar si estamos en modo visualización

  constructor(private websocketService: WebsocketService) {}

  setVisualizationMode(mode: boolean) {
    this.visualizationMode.next(mode);
  }
  getVisualizationMode(): Observable<boolean> {
    return this.visualizationMode.asObservable();
  }

  public getPaymentsByUser(user: User): void {
    console.log("Payment Service getPaymetsByUser")
    this.websocketService.sendMessage('/app/payment/user', user);

    // Suscribirse al destino para recibir la respuesta
    this.websocketService.subscribe(`/topic/payment/user`).subscribe((message) => {
      console.log('Payments recibidos:', message);
      this.paymentsSubject.next(message);
    });
  }

  getPaymentsObservable(): Observable<any>{
    return this.paymentsSubject.asObservable();
  }

  sendPayment(userId: string, payment: PaymentMessage): void {
    // Usa la ruta dinámica para enviar mensajes de pago
    console.log('Mensaje de pago en service');
    this.websocketService.sendMessage(`/app/payment`, payment);
  }

  getPaymentMessages(): Observable<PaymentMessage> {
    return this.paymentSubject.asObservable();
  }

  subscribePaymentNotification(): void{
    console.log("Payment Service subscribePaymentNotification")

    // Suscribirse al destino para recibir la respuesta
    this.websocketService.subscribe(`/topic/payment/notification`).subscribe((message) => {
      console.log('PaymentNotificacion recibidos:', message);
      this.paymentNotification.next(message);
    });
  }

  getPaymentNotification(): Observable<Payment> {
    return this.paymentNotification.asObservable();
  }
  // joinRoom(userId: string): void {
  //   this.websocketService.openConnection(userId).subscribe((message) => {
  //     const messageContent = JSON.parse(message) as PaymentMessage;
  //     console.log("Mensaje recibido en PaymentService: ", messageContent);
  //     console.log("Se abrio la conexion: ");
  //     // Emitir los datos de login recibidos
  //     this.paymentSubject.next(messageContent);

  //     // Emitir notificación de inicio de sesión exitoso
  //     this.paymentNotification.next(`Payment sucessfull from the user ${messageContent.sender.email}`);
  //   });
  // }

  // Método para obtener todos los correos electrónicos
  // getRecipientEmails(): string[] {
  //   return this.accounts.map(account => account.email);
  // }


  //maneja la respuesta del springboot
  /*
  handleResponse(): void {
    this.websocketService.openConnection('userId').subscribe((message) => {
      const paymentResponse = JSON.parse(message) as PaymentMessage;
      this.paymentSubject.next(paymentResponse);
      this.paymentNotification.next(`Payment processed for ${paymentResponse.receiver.name}`);
    });

  }*/

  // joinRoom(userId: string): void {
  //   this.websocketService.openConnection(userId).subscribe((message) => {
  //     const messageContent = JSON.parse(message) as PaymentMessage;
  //     console.log("Mensaje recibido en PaymentService: ", messageContent);
  //     console.log("Se abrio la conexion: ");
  //     // Emitir los datos de login recibidos
  //     this.paymentSubject.next(messageContent);

  //     // Emitir notificación de inicio de sesión exitoso
  //     this.paymentNotification.next(`Payment sucessfull from the user ${messageContent.sender.email}`);
  //   });
  // }

  // Método para obtener todos los correos electrónicos
  // getRecipientEmails(): string[] {
  //   return this.accounts.map(account => account.email);
  // }


  //maneja la respuesta del springboot
  /*
  handleResponse(): void {
    this.websocketService.openConnection('userId').subscribe((message) => {
      const paymentResponse = JSON.parse(message) as PaymentMessage;
      this.paymentSubject.next(paymentResponse);
      this.paymentNotification.next(`Payment processed for ${paymentResponse.receiver.name}`);
    });

  }*/

  getPaymentsMOCK(): PaymentMessage[]{
    return this.paymentHistory;
  }


}

