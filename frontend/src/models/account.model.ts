export interface User {
  id: number;
  name: string;
  surname: string; 
  password: string;
  email: string;
  cc: Balance; 
  dni: string;
  createdAt: Date; 
}

export interface Balance {
  id: number;
  balance: number;
  createdAt: Date;
}
