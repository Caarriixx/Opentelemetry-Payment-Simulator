import {Component, OnInit} from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import {LoginInfo} from "../auth/login-info";
import {AuthService} from "../auth/auth.service";
import { LogMessage } from '../models/login.model';
import {TokenStorageService} from "../services/token-storage.service";
import {NgIf} from "@angular/common";  // Importa Router
// import { LoginService } from '../services/login.service';
import { MaterialModule } from '../app/material/material.module';
import {signal} from '@angular/core';
import { AccountService } from '../services/account.service';
import { WebsocketService } from '../services/websocket.service';
import { SessionService } from '../services/session.service';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    MaterialModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']  // Cambia styleUrl a styleUrls
})

export class LoginComponent implements OnInit {
  form: any = {};
  isConnected: boolean = false;
  email: string = '';
  password: string = '';
  userId: string = '';
  token?: string;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  loginSuccessMessage: string = '';
  loginData: LogMessage | null = null;
  private loginInfo?: LoginInfo;

  // websocket!: WebsocketService

  constructor(private authService: AuthService,
    private tokenStorage: TokenStorageService,
    // private loginService: LoginService,
    private route: ActivatedRoute,
    private accountService: AccountService,
    private websocket: WebsocketService,
    private sesionService: SessionService
  ) {}  // Inyecta Router en el constructor

  hide = signal(true);
  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }

  ngOnInit(): void {
    // if (this.tokenStorage.getToken() != null && this.tokenStorage.getToken() != '{}') {
    //     this.isLoggedIn = true;
    // }
    // this.route.params.subscribe(params => {
    //   this.userId = params['userId'];  // Captura el userId dinámicamente desde la URL
    //   this.connectToRoom(this.userId);

      // // Se suscribe para recibir los datos de login
      //! this.loginService.getLoginMessages().subscribe((data: LogMessage) => {
      //!   this.loginData = data;
      //!   console.log("Datos de login guardados: ", this.loginData);
      //!   this.login(data);
      //! });

      // // Se suscribe para recibir la notificación de inicio de sesión exitoso
      //! this.loginService.getLoginNotification().subscribe((notification: string) => {
      //!   console.log("Notificación recibida:", notification);
      //!   this.loginSuccessMessage = notification;
      //!   console.log(this.loginSuccessMessage);
      //! });
    // });

    // this.websocket.openConnection("1");
    // this.websocket = WebsocketService.getInstance()

    // this.websocket.openConnection(this.sesionService.getSessionId() ?? "");
    this.isConnected = true; // ! Esto es temporal

    TokenStorageService.saveId(TokenStorageService.generateUUID());
  }

  onSubmit() {
    if (!this.isConnected) {
      console.error('No hay conexión STOMP activa.');
      return;
    }
    const loginMessage = {
      userId: -1,
      userName: this.form.username,
      password: this.form.password
    } as LogMessage;
    console.log("Enviando mensaje de inicio de sesión:", loginMessage);

    this.accountService.getUserLogin(loginMessage);
    this.accountService.getUserObservable().subscribe((user: LogMessage) => {
      this.login(user);
    });

    // Usa el servicio `LoginService` para enviar el mensaje al servidor
      // ! this.loginService.sendMessage(this.userId, loginMessage);
  //   this.loginService.getLoginNotification().subscribe((notification: string) => {

  //     // // setTimeout(() => {
        // window.location.replace("/account");
  //     // // }, 2000);
  //     // console.log(this.form);


    console.log(this.form);

    // console.log("me voy a accountttt");
    // localStorage.setItem('loginNotification', `Inicio de sesión exitoso para el usuario ${this.form.username}`);
    //window.location.replace("account"); //aqui hay un cambio de pagina

  //     // this.loginInfo = new LoginInfo(this.form.username, this.form.password);

  //     // this.authService.attemptAuth(this.loginInfo).subscribe(
  //     //   data => {
  //     //     this.tokenStorage.saveToken(data.accessToken || '{}');
  //     //     this.tokenStorage.saveUsername(data.username || '{}');
  //     //     console.log('Access Token:', data.accessToken);
  //     //     this.isLoginFailed = false;
  //     //     this.isLoggedIn = true;
  //     //     this.token = this.tokenStorage.getToken();
  //     //     //window.location.replace("account");  // Aqui hay otro cambio de pagina?

  //     //   },
  //     //   error => {
  //     //     console.log(error);
  //     //     this.errorMessage = error.error.message;
  //     //     this.isLoginFailed = true;
  //     //   }
  //     // );
  //   }
  // )
}

  login(message: LogMessage){

    console.log("Login...")

    this.isLoginFailed = false;

    if(message.userId != -1){
      console.log("Login Success");
      TokenStorageService.saveId(message.userId);
      localStorage.setItem('loginNotification', `Inicio de sesión exitoso para el usuario ${this.form.username}`);

      this.accountService.setVisualizationMode(false);
      window.location.replace("/account");
    }else{
      console.log("Login Fail")
      this.isLoginFailed = true;
    }

  }


  connectToRoom(userId: string): void {
    // this.loginService.joinRoom(userId);  // Pasa el userId al servicio para manejar la conexión WebSocket
    this.isConnected = true;
  }

  //envia el mensaje con los datos del login
  /*
  sendMessage(): void {
    if (this.isConnected) {
      const logMessage = {
        userId: this.email,
        password: this.password
      } as LogMessage;

      console.log("Enviando mensaje de inicio de sesión:", logMessage);
      this.loginService.sendMessage(this.userId, logMessage);  // Usa el roomId dinámico
    } else {
      console.error('No hay conexión STOMP activa.');
    }
  }
*/
}
