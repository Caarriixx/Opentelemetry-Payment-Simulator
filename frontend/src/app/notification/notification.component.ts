import { Component, inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { WebsocketService } from '../../services/websocket.service';
import { PaymentService } from '../../services/payment.service';
import { Payment } from '../../models/payment.model';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.css'
})
export class NotificationComponent {

  private _snackBar = inject(MatSnackBar);
  

  constructor(private websocket: WebsocketService, private paymentService: PaymentService){
    console.log("Constructor notificacion");
    // this._snackBar.open("Hola");
    this.websocket.onConnectionReady().subscribe((isConnected) => {
      if (isConnected) {
        this.getData();
      }
    });
  }

  getData() {
    this.paymentService.subscribePaymentNotification();
    this.paymentService.getPaymentNotification().subscribe((payment: Payment) => {
      console.log("PaymentNotification recibidos")
      console.log(payment)
      console.log(`El pago con id ${payment.id} esta en estado ${payment.state.description}`)
      this._snackBar.open(`El pago con id ${payment.id} esta en estado ${payment.state.description}`, "X", {
        duration: 5000,
        horizontalPosition: "center",
        verticalPosition: "top",
      });
    });

  }

}