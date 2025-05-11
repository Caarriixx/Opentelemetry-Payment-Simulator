import { Injectable } from '@angular/core';
import { WebsocketService } from './websocket.service';
import { Subject, Observable } from 'rxjs';
import { Payment, PaymentMessage } from '../models/payment.model';
import { Balance, User } from '../models/account.model';

@Injectable({
  providedIn: 'root'
})
export class DepositService {

  private depositsSubject = new Subject<Payment[]>();
  private depositNotification = new Subject<Payment>();
  private visualizationMode = new Subject<boolean>(); // Para notificar si estamos en modo visualización

  constructor(private websocketService: WebsocketService) {
  }


  setVisualizationMode(mode: boolean) {
    this.visualizationMode.next(mode);
  }
  getVisualizationMode(): Observable<boolean> {
    return this.visualizationMode.asObservable();
  }

  public getDepositsByUser(user: User): void {
    console.log("Deposit Service getDepositsByUser")
    this.websocketService.sendMessage('/app/deposit/user', user);

    // Suscribirse al destino para recibir la respuesta
    this.websocketService.subscribe(`/topic/deposit/user`).subscribe((message) => {
      console.log('Deposits recibidos:', message);
      this.depositsSubject.next(message);
    });
  }

  getDepositsObservable(): Observable<any>{
    return this.depositsSubject.asObservable();
  }

  sendDeposit(userId: string, deposit: PaymentMessage): void {
    // Usa la ruta dinámica para enviar mensajes de pago
    console.log('Mensaje de deposito en service');
    this.websocketService.sendMessage(`/app/deposit`, deposit);
  }

  subscribeDepositNotification(): void {
    console.log("Deposit Service subscribeDepositNotification");
    // Suscribirse al destino para recibir la respuesta
    this.websocketService.subscribe(`/topic/notification/deposit`).subscribe((message) => {
      console.log('DepositNotificacion recibidos:', message);
      this.depositNotification.next(message);
    });
  }

  getDepositNotification(): Observable<Payment> {
    return this.depositNotification.asObservable();
  }
}
