import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';  
import { LogMessage } from 'src/app/model/login-message';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  isConnected: boolean = false;
  email: string = '';
  password: string = '';
  roomId: string = '';
  loginSuccessMessage: string = '';  
  loginData: LogMessage | null = null;  

  constructor(
    private loginService: LoginService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.roomId = params['userId'];  // Captura el roomId dinámicamente desde la URL
      this.connectToRoom(this.roomId);  // Conéctate al roomId dinámico
  
      // Se suscribe para recibir los datos de login
      this.loginService.getLoginMessages().subscribe((data: LogMessage) => {
        this.loginData = data;
        console.log("Datos de login guardados: ", this.loginData);
      });
  
      // Se suscribe para recibir la notificación de inicio de sesión exitoso
      this.loginService.getLoginNotification().subscribe((notification: string) => {
        console.log("Notificación recibida:", notification);
        this.loginSuccessMessage = notification;
        console.log(this.loginSuccessMessage);  
      });
    })   
  }
  

  connectToRoom(roomId: string): void {
    this.loginService.joinRoom(roomId);  // Pasa el roomId al servicio para manejar la conexión WebSocket
    this.isConnected = true;
  }

  sendMessage(): void {
    if (this.isConnected) {
      const logMessage = {
        userId: this.email,
        password: this.password
      } as LogMessage;
  
      console.log("Enviando mensaje de inicio de sesión:", logMessage);  
      this.loginService.sendMessage(this.roomId, logMessage);  // Usa el roomId dinámico
    } else {
      console.error('No hay conexión STOMP activa.');
    }
  }
}
