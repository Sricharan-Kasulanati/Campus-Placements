import { http } from './http';
import type { Company } from '../types/company';
import type { Page } from '../types/page';

export async function searchCompaniesPage(
  query = '',
  page = 0,
  size = 12,
  sort = 'name,asc'
): Promise<Page<Company>> {
  const q = query ? `&q=${encodeURIComponent(query)}&query=${encodeURIComponent(query)}` : '';
  return http<Page<Company>>(`/api/companies?page=${page}&size=${size}&sort=${encodeURIComponent(sort)}${q}`, {
    method: 'GET',
  });
}

export async function getCompany(id: number): Promise<Company> {
  return http<Company>(`/api/companies/${id}`, { method: 'GET' });
}
