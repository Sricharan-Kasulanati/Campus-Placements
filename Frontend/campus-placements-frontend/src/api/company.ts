import { http } from "./http";
import type { Company } from "../types/company";
import type { Page } from "../types/page";

export type CompanyPayload = {
  name: string;
  description?: string;
  location?: string;
  website?: string;
  category?: string;
};

type SearchParams = {
  keyword?: string;
  category?: string;
  location?: string;
  page?: number;
  size?: number;
};

export async function searchCompaniesPage(
  query = "",
  page = 0,
  size = 12,
  sort = "name,asc"
): Promise<Page<Company>> {
  const q = query
    ? `&q=${encodeURIComponent(query)}&query=${encodeURIComponent(query)}`
    : "";
  return http<Page<Company>>(
    `/api/companies?page=${page}&size=${size}&sort=${encodeURIComponent(
      sort
    )}${q}`,
    {
      method: "GET",
    }
  );
}

export async function getCompany(id: number): Promise<Company> {
  return http<Company>(`/api/companies/${id}`, { method: "GET" });
}

export async function createCompany(payload: CompanyPayload): Promise<Company> {
  return http<Company>('/api/companies', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: payload as any,
  });
}

export async function updateCompany(
  id: number,
  payload: CompanyPayload
): Promise<Company> {
  return http<Company>(`/api/companies/update/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: { ...payload, id } as any,
  });
}
