import { http } from "./http";
import type { AdminStudent } from "../types/adminStudent";

export async function listAdminStudents(): Promise<AdminStudent[]> {
  return http<AdminStudent[]>("/api/admin/students", { method: "GET" });
}
