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
  const res = await fetch("/api/companies", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error("Failed to create company");
  return res.json();
}

export async function updateCompany(
  id: number,
  payload: CompanyPayload
): Promise<Company> {
  const res = await fetch(`/api/companies/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  if (!res.ok) throw new Error("Failed to update company");
  return res.json();
}
