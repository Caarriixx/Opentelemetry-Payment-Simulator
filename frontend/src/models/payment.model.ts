import { User } from "./account.model";

// Crear `paymentData` asegurando que `sender` también sea del tipo `User`
export interface PaymentMessage {
  sender: User; // El remitente del pago, de tipo User
  receiver: User; // El destinatario del pago, de tipo User
  amount: number; // Monto del pago
  state: number; // Estado del pago, por ejemplo, 0 para pendiente, 1 para aprobado, etc.
  date: Date; // Fecha del pago
}

export interface Payment {
  id: number,
  sender: User; // El remitente del pago, de tipo User
  receiver: User; // El destinatario del pago, de tipo User
  amount: number; // Monto del pago
  state: State; // Estado del pago, por ejemplo, 0 para pendiente, 1 para aprobado, etc.
  date: Date; // Fecha del pago
}

export interface State {
  state: number,
  description: string
}



