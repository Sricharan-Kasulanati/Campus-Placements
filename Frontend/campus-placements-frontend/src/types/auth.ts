export type Role = 'ADMIN' | 'STUDENT';

export interface AuthResponse {
  token: string;
  fullName: string;
  role: Role;
}
export interface SignupRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: Role;
}
export interface LoginRequest {
  email: string;
  password: string;
}
