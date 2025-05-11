import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { environment } from 'src/environments/environment';
import * as SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';  // Actualización para usar Client de stompjs

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {

  private socketClient: Client | null = null;  // Usa Client de Stomp.js
  private wssubscription: any;

  /**
   * Opens connection
   * @param idOperacion identificador al que nos suscribiremos
   * @returns connection observable que devolverá la respuesta una vez finalizada la espera
   */
  public openConnection(roomId: string): Subject<any> {
    this.close();  // Cierra cualquier conexión existente
    const receivedMessage: Subject<any> = new Subject<any>();
  
    // Inicializa el WebSocket y espera a que la conexión esté lista antes de suscribirse
    this.initSocket(environment.rutaZpWebsocket, roomId);
  
    if (this.socketClient) {
      // Espera a que la conexión esté lista antes de intentar suscribirse
      this.socketClient.onConnect = (frame) => {
        console.log("Connected: " + frame);
  
        // Una vez que la conexión está activa, suscríbete al canal dinámico
        this.subscribeToChannel(`/topic/${roomId}`, receivedMessage);
      };
  
      this.socketClient.activate();  // Activa el cliente STOMP y espera el onConnect
    } else {
      console.error("socketClient no ha sido inicializado correctamente.");
    }
  
    return receivedMessage;
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
  private initSocket(url: string, roomId: string): void {  
    console.log("Initialize WebSocket Connection");
  
    let ws = new SockJS(url);
    this.socketClient = new Client({
      webSocketFactory: () => ws,  // Crea el WebSocket con SockJS
      reconnectDelay: 5000,        // Intentar reconectar automáticamente después de 5 segundos
      heartbeatIncoming: 10000,    // Frecuencia del heartbeat
      heartbeatOutgoing: 10000,
      onConnect: (frame) => {
        console.log("Connected: " + frame);
  
        // Solo después de la conexión, suscribirse al canal dinámico /topic/{roomId}
        this.subscribeToChannel(`/topic/${roomId}`, new Subject<any>());  // Usa el roomId dinámico
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ' + frame.headers['message']);
        console.error('Additional details: ' + frame.body);
      }
    });
  
    this.socketClient.activate();  // Activa el cliente STOMP y luego espera el onConnect
  }
  
  
  


  /**
   * Sends a message to the server
   * @param destination destino al que se envía el mensaje
   * @param message mensaje que se enviará
   */
  public sendMessage(destination: string, message: any): void {
    if (this.socketClient && this.socketClient.active) {
      this.socketClient.publish({ destination, body: JSON.stringify(message) });
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
    }
    console.log("Disconnected");
  }

  /**
   * Subscribes to a specific channel
   * @param channel canal al que suscribirse
   * @param subject Subject que recibirá los mensajes del canal
   */
  public subscribeToChannel(channel: string, subject: Subject<any>): void {
    if (this.socketClient && this.socketClient.connected) {  // Verifica que la conexión STOMP esté activa
      this.socketClient.subscribe(channel, (message: any) => {
        console.log("Mensaje recibido en WebsocketService: ", message.body);
        subject.next(message.body);  // Envía el mensaje al Subject
      });
    } else {
      console.error('No hay conexión STOMP activa, no se puede suscribir.');
    }
  }
  
  
  
}
