// import { Injectable } from '@angular/core';
// import { WebsocketService } from './websocket.service';
// import { LogMessage } from '../models/login.model';
// import { Observable, Subject } from 'rxjs';

// @Injectable({
//   providedIn: 'root'
// })
// export class LoginService {

//   private loginSubject = new Subject<LogMessage>();  // Para emitir los datos de vuelta
//   private loginNotification = new Subject<string>(); // Para notificar cuando el login sea exitoso

//   constructor(private websocketService: WebsocketService) {}

//   // Método para unirse a una sala unica para ese usuario
//   joinRoom(userId: string): void {
//     this.websocketService.openConnection(userId).subscribe((message) => {
//       const messageContent = JSON.parse(message) as LogMessage;
//       console.log("Mensaje recibido en LoginService: ", messageContent);
//       console.log("Se abrio la conexion: ");
//       // Emitir los datos de login recibidos
//       this.loginSubject.next(messageContent);
  
//       // Emitir notificación de inicio de sesión exitoso
//       this.loginNotification.next(`Login receive for the user ${messageContent.userId}`);
//     });
//   }
  
//   sendMessage(userId: string, logMessage: LogMessage): void {
//     // Usa WebsocketService para enviar mensajes al destino dinámico /app/log/{userId}
//     this.websocketService.sendMessage(`/app/log/${userId}`, logMessage);
//   }
  

//   // Observable para que el componente se suscriba y reciba los datos de login
//   getLoginMessages(): Observable<LogMessage> {
//     return this.loginSubject.asObservable();
//   }

//   // Observable para notificar cuando el inicio de sesión sea exitoso
//   getLoginNotification(): Observable<string> {
//     return this.loginNotification.asObservable();
//   }
// }
