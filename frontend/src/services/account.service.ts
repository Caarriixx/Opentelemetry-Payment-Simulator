import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import {Balance, User} from "../models/account.model";
import { WebsocketService } from './websocket.service';
import { LogMessage } from '../models/login.model';

@Injectable({
  providedIn: 'root'
})
export class AccountService{


  private isVisualizationMode = false;
  private userSubject = new Subject<User>();  // Para emitir los datos de vuelta
  private usersSubject = new Subject<User[]>();
  private accountNotification = new Subject<string>();

  constructor(private websocketService: WebsocketService) {
    const savedMode = localStorage.getItem('isVisualizationMode');
    this.isVisualizationMode = savedMode === 'true';
  }

  public getVisualizationMode(): boolean {
    return this.isVisualizationMode;
  }
  // Método para actualizar el valor de la bandera
  public setVisualizationMode(value: boolean): void {
    this.isVisualizationMode = value;
    localStorage.setItem('isVisualizationMode', String(value));
  }

  // Obtener lista de usuarios a través de WebSocket
  public getUsers(): void {
    // Enviar solicitud para obtener todos los usuarios
    this.websocketService.sendMessage('/app/user/list', {});

    // Suscribirse al destino para recibir la respuesta
    this.websocketService.subscribe('/topic/users').subscribe((message) => {
      console.log('Usuarios recibidos:', message);
      this.usersSubject.next(message);
    });
  }

  // Obtener un usuario específico por ID a través de WebSocket
  public getUserById(userId: number): void {
    console.log("Account Service getUserById")
    this.websocketService.sendMessage(`/app/user/${userId.toString()}`, userId);

    // Suscribirse al destino para recibir la respuesta
    this.websocketService.subscribe(`/topic/user/${userId}`).subscribe((message) => {
      console.log('Usuario recibido:', message);
      this.userSubject.next(message);
    });
  }
  // Obtener un usuario específico por ID a través de WebSocket
  public getUserLogin(loginMessage: LogMessage): void {
    this.websocketService.sendMessage('/app/log', loginMessage);

    // Suscribirse al destino para recibir la respuesta
    this.websocketService.subscribe(`/topic/login`).subscribe((message) => {
      console.log('Usuario recibido:', message);
      this.userSubject.next(message);
    });
  }

  // Obtener el observable del usuario
  public getUserObservable() : Observable<any> {
    return this.userSubject.asObservable();
  }
  public getUsersObservable() : Observable<any> {
    return this.usersSubject.asObservable();
  }
}
