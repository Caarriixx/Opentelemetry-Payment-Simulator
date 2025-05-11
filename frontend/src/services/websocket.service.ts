import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import SockJS from 'sockjs-client';
import { Client, Stomp, StompSubscription } from '@stomp/stompjs';  // Actualización para usar Client de stompjs
import { TokenStorageService } from './token-storage.service';
//import { PaymentMessage } from '../models/payment.model';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  // private static instance: WebsocketService | null = null;
  private socketClient: Client | null = null;  // Usa Client de Stomp.js
  private wssubscription: any;
  private subscriptions: Map<string, StompSubscription> = new Map();
  private isConnected = false; // Estado de conexión
  private connectionSubject = new Subject<boolean>();

  constructor(){
    console.log("Web Socket Constructor")
    this.openConnection("1");
  }

  // public static getInstance(): WebsocketService {
  //   if (!WebsocketService.instance) {
  //     WebsocketService.instance = new WebsocketService();
  //   }
  //   return WebsocketService.instance;
  // }

  // public getIsConnected(){
  //   return this.isConnected;
  // }

  /**
   * Opens connection
   * @param idOperacion identificador al que nos suscribiremos
   * @returns connection observable que devolverá la respuesta una vez finalizada la espera
   */
  public openConnection(sesionId: string): void {
    // this.connectionSubject.next(false);
    // this.close();  // Cierra cualquier conexión existente
  
    // Inicializa el WebSocket y espera a que la conexión esté lista antes de suscribirse
    if (!this.isConnected) {

      this.initSocket('http://localhost:8080/socket');
      // this.isConnected = true;
      // this.connectionSubject.next(this.isConnected);
      // this.connectionSubject.next(true);
      console.log('Conexión WebSocket establecida.');
    } else {
      // this.connectionSubject.next(false);
      console.log('Ya existe una conexión WebSocket activa.');
    }

    // const receivedMessage: Subject<any> = new Subject<any>();
  
    // if (this.socketClient) {
    //   // Espera a que la conexión esté lista antes de intentar suscribirse
    //   this.socketClient.onConnect = (frame) => {
    //     console.log("Connected: " + frame);

    //   };

    //   this.socketClient.onStompError = (frame) => {
    //     console.error('Broker reported error: ' + frame.headers['message']);
    //     console.error('Additional details: ' + frame.body);
    // };
  
    //   this.socketClient.activate();  // Activa el cliente STOMP y espera el onConnect
    // } else {
    //   console.error("socketClient no ha sido inicializado correctamente.");
    // }
  
    // return receivedMessage;
  }
  
  /**
   * Suscribirse a un canal
   * @param destination Ruta del canal (topic o user queue)
   * @returns Subject para recibir mensajes
   */
  public subscribe(destination: string): Subject<any> {
    const subject = new Subject<any>();

    if (this.socketClient && this.socketClient.connected) {
      const subscription = this.socketClient.subscribe( `${destination}/${TokenStorageService.getId()}`, (message) => {
        subject.next(JSON.parse(message.body));
      });
      this.subscriptions.set(destination, subscription);
    } else {
      console.error('WebSocket no está conectado');
    }

    return subject;
  }

  /**
   * Desuscribirse de un canal
   * @param destination Ruta del canal
   */
  public unsubscribe(destination: string): void {
    const subscription = this.subscriptions.get(destination);
    if (subscription) {
      subscription.unsubscribe();
      this.subscriptions.delete(destination);
    }
  }
  

  /**
   * Configures socket client
   * @param client 
   * @returns cliente configurado
   */
  private configureSocketClient(client: Client): Client {
    client.debug = (msg) => console.log(new Date(), msg);  // Función de debug
    return client;
  }

  /**
   * Inits socket
   * @param url 
   */
  private initSocket(url: string): void {  
    console.log("Initialize WebSocket Connection");
  
    // let ws = new SockJS(url);
    // this.socketClient = Stomp.client(url);
    this.socketClient = new Client({
      webSocketFactory: () => new SockJS(url),
      reconnectDelay: 5000,  // Reintentos automáticos
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      onConnect: (frame) => {
        // console.log('Conexión establecida:', frame);
        console.log("Conexion establecida")
        this.isConnected = true;  // Cambia el estado de conexión
        this.connectionSubject.next(this.isConnected);
      },
      onStompError: (frame) => {
        console.error('Error en STOMP:', frame.headers['message']);
        console.error('Detalles:', frame.body);
        this.isConnected = false; // Marca la conexión como no activa
        this.connectionSubject.next(this.isConnected);
      },
      onWebSocketClose: () => {
        console.warn('WebSocket cerrado.');
        this.isConnected = false;
        this.connectionSubject.next(this.isConnected);
      }
    });
      this.socketClient.activate();
  }
  

  /**
   * Sends a message to the server
   * @param destination destino al que se envía el mensaje
   * @param message mensaje que se enviará
   */
  public sendMessage(destination: string, message: any): void {
    if (this.socketClient && this.socketClient.active) {
      console.log(`Enviando mensaje a ${destination}/${TokenStorageService.getId()}`)
      this.socketClient.publish({ destination:`${destination}/${TokenStorageService.getId()}`, body: JSON.stringify(message) });
    } else {
      console.error('No hay conexión STOMP activa.');
    }
  }

  /**
   * Closes websocket service
   */
  close(): void {
    if (this.socketClient !== null) {
      this.socketClient.deactivate();  // Usa deactivate en lugar de disconnect
      this.socketClient = null;
      this.subscriptions.clear();
      this.isConnected = false;
      this.connectionSubject.next(this.isConnected);
    }
    console.log("Websococket disconnected");
  }

  public onConnectionReady(): Observable<boolean> {
    return this.connectionSubject.asObservable();
  }

  // public getConnection(): Client | null{
  //   return this.socketClient;
  // }

  // /**
  //  * Subscribes to a specific channel
  //  * @param channel canal al que suscribirse
  //  * @param subject Subject que recibirá los mensajes del canal
  //  */
  // public subscribeToChannel(channel: string, subject: Subject<any>): void {
  //   if (this.socketClient && this.socketClient.connected) {
  //     this.socketClient.subscribe(channel, (message: any) => {
  //       try {
  //         const parsedMessage = JSON.parse(message.body);

  //         // Comprueba si es un solo usuario o una lista
  //         if (Array.isArray(parsedMessage)) {
  //             console.log("Lista recibida:", parsedMessage);
  //             subject.next(parsedMessage);  // Emitir lista de usuarios
  //         } else {
  //             console.log("Individual recibido:", parsedMessage);
  //             subject.next(parsedMessage);  // Emitir usuario individual
  //         }
  //       } catch (error) {
  //           console.error("Error procesando mensaje en Account Channel:", error);
  //       }
  //     });
  //   } else {
  //     console.error('No hay conexión STOMP activa, reintentando la suscripción...');
  //       // Lógica de reintento, por ejemplo, puedes esperar un tiempo y reintentar:
  //       setTimeout(() => {
  //           this.subscribeToLoginChannel(channel, subject);
  //       }, 1000); // Reintenta después de 1 segundo
  //   }
  // }

  // /**
  //  * Subscribes to a specific channel
  //  * @param channel canal al que suscribirse
  //  * @param subject Subject que recibirá los mensajes del canal
  //  */
  // public subscribeToLoginChannel(channel: string, subject: Subject<any>): void {
  //   if (this.socketClient && this.socketClient.connected) {
  //     this.socketClient.subscribe(channel, (message: any) => {
  //       console.log("Notificación personalizada recibida: ", message.body);
  //       subject.next(message.body);
  //     });
  //   } else {
  //     console.error('No hay conexión STOMP activa, reintentando la suscripción...');
  //       // Lógica de reintento, por ejemplo, puedes esperar un tiempo y reintentar:
  //       setTimeout(() => {
  //           this.subscribeToLoginChannel(channel, subject);
  //       }, 1000); // Reintenta después de 1 segundo
  //   }
  // }
  //enviar los datos al springboot
  // public sendPaymentMessage(destination: string, message: any): void {
  //   if (this.socketClient && this.socketClient.active) {
  //     console.log('Mensaje de pago entro bien en websocket.');
  //     this.socketClient.publish({ destination, body: JSON.stringify(message) });
  //   } else {
  //     console.error('No hay conexión STOMP activa para enviar el mensaje de pago.');
  //   }
  // }
  
 //para recibir los datos del springboot en el canal
  // public subscribeToPaymentChannel(channel: string, subject: Subject<any>): void {
  //   if (this.socketClient && this.socketClient.connected) {
  //     this.socketClient.subscribe(channel, (message: any) => {
  //       console.log("Mensaje de pago recibido: ", message.body);
  //       subject.next(message.body);
  //     });
  //   } else {
  //     console.error('No hay conexión STOMP activa, no se puede suscribir al canal de pagos.');
  //     setTimeout(() => {
  //       this.subscribeToPaymentChannel(channel, subject);
  //   }, 1000); // Reintenta después de 1 segundo
  //   }
  // }
  
  // public subscribeToAccountChannel(channel: string, subject: Subject<any>): void {
  //   if (this.socketClient && this.socketClient.connected) {
  //     this.socketClient.subscribe(channel, (message: any) => {
  //       console.log("Mensaje de account recibido: ", message.body);
  //       subject.next(message.body);
  //     });
  //   } else {
  //     console.error('No hay conexión STOMP activa, no se puede suscribir al canal de pagos.');
  //     setTimeout(() => {
  //       this.subscribeToAccountChannel(channel, subject);
  //   }, 1000); // Reintenta después de 1 segundo
  //   }
  // }
  
  
  
  
}
