import { Injectable } from '@angular/core';
import { User } from '../models/account.model';

@Injectable({
  providedIn: 'root'
})
export class SessionService {
  private readonly sessionIdKey = 'sessionId';
  private readonly userKey = 'connectedUser';

  constructor() {
    this.ensureSessionIdExists();
  }

  /**
   * Genera un ID único (UUID)
   */
  private generateUUID(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (char) => {
      const random = (Math.random() * 16) | 0;
      const value = char === 'x' ? random : (random & 0x3) | 0x8;
      return value.toString(16);
    });
  }

  /**
   * Verifica si existe un ID de sesión y lo genera si no existe.
   */
  private ensureSessionIdExists(): void {
    if (!sessionStorage.getItem(this.sessionIdKey)) {
      sessionStorage.setItem(this.sessionIdKey, this.generateUUID());
    }
  }

  /**
   * Obtiene el ID único de la sesión actual.
   */
  getSessionId(): string | null{
    return sessionStorage.getItem(this.sessionIdKey);
  }

  /**
   * Guarda el usuario conectado.
   * @param user Objeto con información del usuario
   */
  saveUser(user: User): void {
    sessionStorage.setItem(this.userKey, JSON.stringify(user));
  }

  /**
   * Obtiene el usuario conectado.
   */
  getUser(): User {
    const userData = sessionStorage.getItem(this.userKey);
    return userData ? JSON.parse(userData) : null;
  }

  /**
   * Limpia los datos de sesión.
   */
  clearSession(): void {
    sessionStorage.removeItem(this.sessionIdKey);
    sessionStorage.removeItem(this.userKey);
  }
}
