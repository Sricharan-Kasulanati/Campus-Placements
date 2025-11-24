import { http } from './http';
import type { Company } from '../types/company';

export function getMyRegistrationIds(): Promise<number[]> {
  return http<number[]>(`/api/students/me/registrations/ids`, { method: 'GET' });
}

export function getMyCompanies(): Promise<Company[]> {
  return http<Company[]>(`/api/students/me/registrations`, { method: 'GET' });
}

export async function registerForCompany(companyId: number): Promise<void> {
  await http<void>(`/api/students/me/registrations/${companyId}`, { method: 'POST' });
}

export async function unregisterForCompany(companyId: number): Promise<void> {
  await http<void>(`/api/students/me/registrations/${companyId}`, { method: 'DELETE' });
}
