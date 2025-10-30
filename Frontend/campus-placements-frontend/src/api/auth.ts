import { http, setAuthToken } from './http';
import type { AuthResponse, LoginRequest, SignupRequest } from '../types/auth';

export async function signup(payload: SignupRequest): Promise<AuthResponse> {
  const data = await http<AuthResponse>('/api/auth/signup', { method: 'POST', body: payload });
  setAuthToken(data.token);
  return data;
}
export async function login(payload: LoginRequest): Promise<AuthResponse> {
  const data = await http<AuthResponse>('/api/auth/login', { method: 'POST', body: payload });
  setAuthToken(data.token);
  return data;
}

