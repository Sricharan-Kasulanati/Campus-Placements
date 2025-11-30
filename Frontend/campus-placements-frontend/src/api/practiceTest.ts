import { getAuthToken, http } from "./http";
import { PracticeTest } from "types/practiceTest";

export async function uploadPracticeTest(
  companyId: number,
  title: string,
  jobRole: string,
  description: string,
  file: File
): Promise<PracticeTest> {
  const form = new FormData();
  form.append("title", title);
  form.append("jobRole", jobRole);
  form.append("description", description);
  form.append("file", file);

  const token = getAuthToken();

  const res = await fetch(`/api/companies/${companyId}/practice-tests`, {
    method: "POST",
    headers: token ? { Authorization: `Bearer ${token}` } : undefined,
    body: form,
  });

  if (!res.ok) {
    throw new Error("Failed to upload practice test");
  }
  return res.json();
}

export async function listPracticeTests(
  companyId: number
): Promise<PracticeTest[]> {
  return http<PracticeTest[]>(
    `/api/companies/${companyId}/practice-tests`,
    { method: "GET" }
  );
}