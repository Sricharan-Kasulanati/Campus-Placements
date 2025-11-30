import type { Company } from "./company";

export type AdminStudent = {
  id: number;
  fullName: string;
  email: string;
  registeredCompanies: Pick<Company, "id" | "name">[];
};
