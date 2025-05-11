import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {catchError, Observable, tap} from 'rxjs';
import { map } from 'rxjs/operators';
import { LoginInfo } from './login-info';
import { JwtResponse } from './jwt-response';
import { TokenStorageService } from '../services/token-storage.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginUrl = 'http://localhost:8080/login';

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  attemptAuth(credentials: LoginInfo): Observable<any> {
    return this.http.post<any>(this.loginUrl, credentials, httpOptions).pipe(
      map(response => {
        console.log('Authentication response:', response); // Log for debugging
        if (response.accessToken) {
          this.tokenStorage.saveToken(response.accessToken);
        } else {
          console.log('Authentication response does not contain an accessToken.'); // Log for debugging
          console.error('Authentication response does not contain an accessToken.');
        }
        return response;
      })
    );
  }
}
