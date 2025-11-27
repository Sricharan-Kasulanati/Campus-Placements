import { http } from './http';
import type { Company } from '../types/company';

export async function searchCompanies(query = ''): Promise<Company[]> {
  const q = query ? `?query=${encodeURIComponent(query)}` : '';
  return http<Company[]>(`/api/companies${q}`, { method: 'GET' });
}

export async function getCompany(id: number): Promise<Company> {
  return http<Company>(`/api/companies/${id}`, { method: 'GET' });
}
