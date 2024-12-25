import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';  // Cette importation pourrait être la bonne, selon la version de la bibliothèque
import SockJS from 'sockjs-client';  // Importation correcte de SockJS

@Injectable({
  providedIn: 'root'
})
export class WebSocketServiceService {

  private stompClient: Client | null = null;

  connect() {
    const socket = new SockJS('http://localhost:8084/ws');
    this.stompClient = new Client({
      webSocketFactory: () => socket,
      onConnect: (frame) => {
        console.log('Connected: ', frame);
        this.stompClient?.subscribe('/topic/order-status', (message) => {
          console.log('Message received: ', message.body);
        });
      },
      onStompError: (frame) => {
        console.error('Broker reported error: ', frame.headers['message']);
        console.error('Additional details: ', frame.body);
      },
      onWebSocketClose: (event) => {
        console.error('WebSocket closed: ', event);
      },
      onDisconnect: () => {
        console.log('Disconnected from WebSocket server');
      }
    });

    this.stompClient.activate(); // Activer la connexion
  }

  disconnect() {
    this.stompClient?.deactivate();
  }
}
