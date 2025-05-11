import { Injectable } from '@angular/core';

const TOKEN_KEY = 'AuthToken';
const TOKEN_ID = 'IDToken';
const USERNAME_KEY = 'AuthUsername';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private roles: Array<string> = [];
  constructor() { }

  public saveToken(token: string) {
    console.log('Attempting to save token:', token); // Log for debugging
    if (this.isTokenValid(token)) {
      window.sessionStorage.removeItem(TOKEN_KEY);
      window.sessionStorage.setItem(TOKEN_KEY, token);
      console.log('Token saved successfully.'); // Log for debugging
    } else {
      console.error('Invalid JWT token format. Token was not saved.');
    }
  }

  public static generateUUID(): number {
    return Number('xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx'.replace(/[xy]/g, (char) => {
      const random = (Math.random() * 16) | 0;
      const value = char === 'x' ? random : (random & 0x3) | 0x8;
      return value.toString();
    }));
  }

  public static getToken(): string {
    if (typeof window !== 'undefined' && typeof sessionStorage !== 'undefined') {
      const token = sessionStorage.getItem(TOKEN_KEY) || '{}';
      console.log('Retrieved Token:', token);  // Log para verificar el token
      return token;
    }
    return '{}';  // Devuelve un valor por defecto si no se puede acceder a sessionStorage
  }

  public static saveId(token: number) {
    console.log('Attempting to save token:', token); // Log for debugging
    localStorage.setItem(TOKEN_ID, token.toString());
    console.log('Token saved successfully.'); // Log for debugging

  }

  public static getId(): number {
    const token = localStorage.getItem(TOKEN_ID);
    return token ? Number(token) : -1;
  }

  public saveUsername(username: string) {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.setItem(USERNAME_KEY, username);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY) || '{}';
  }

  private isTokenValid(token: string): boolean {
    if (!token) {
      return false;
    }
    const periodCount = (token.match(/\./g) || []).length;
    return periodCount === 2;
  }
}
