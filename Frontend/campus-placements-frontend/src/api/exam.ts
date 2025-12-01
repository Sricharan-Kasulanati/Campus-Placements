import { QuizAdminDetail, QuizAdminRequest, QuizAnswer, QuizForTaking, QuizResult, QuizStudentSummary, QuizSummaryAdmin } from "types/exam";
import { http, getAuthToken } from "./http";

export async function createQuiz(
  companyId: number,
  payload: QuizAdminRequest
): Promise<QuizAdminDetail> {
  return http<QuizAdminDetail>(
    `/api/admin/companies/${companyId}/quizzes`,
    {
      method: "POST",
      body: payload,
    }
  );
}

export async function submitQuiz(
  quizId: number,
  answers: QuizAnswer[]
): Promise<QuizResult> {
  return http<QuizResult>(`/api/quizzes/${quizId}/submit`, {
    method: "POST",
    body: { answers },
  });
}

export async function fetchStudentQuizzes(
  companyId: number,
  jobRole?: string
): Promise<QuizStudentSummary[]> {
  const url =
    jobRole && jobRole.trim().length > 0
      ? `/api/companies/${companyId}/quizzes?jobRole=${encodeURIComponent(
          jobRole
        )}`
      : `/api/companies/${companyId}/quizzes`;

  return http<QuizStudentSummary[]>(url, { method: "GET" });
}

export async function fetchQuizForTaking(
  quizId: number
): Promise<QuizForTaking> {
  return http<QuizForTaking>(`/api/quizzes/${quizId}/take`, {
    method: "GET",
  });
}

export async function fetchAdminQuizzes(
  companyId: number,
  jobRole?: string
): Promise<QuizSummaryAdmin[]> {
  const url =
    jobRole && jobRole.trim().length > 0
      ? `/api/admin/companies/${companyId}/quizzes?jobRole=${encodeURIComponent(
          jobRole
        )}`
      : `/api/admin/companies/${companyId}/quizzes`;

  return http<QuizSummaryAdmin[]>(url, { method: "GET" });
}

export async function fetchQuizAdmin(
  quizId: number
): Promise<QuizAdminDetail> {
  return http<QuizAdminDetail>(`/api/admin/quizzes/${quizId}`, {
    method: "GET",
  });
}

export async function updateQuiz(
  quizId: number,
  payload: QuizAdminRequest
): Promise<QuizAdminDetail> {
  return http<QuizAdminDetail>(`/api/admin/quizzes/${quizId}`, {
    method: "PUT",
    body: JSON.stringify(payload),
  });
}

export async function deleteQuiz(quizId: number): Promise<void> {
  await http<void>(`/api/admin/quizzes/${quizId}`, {
    method: "DELETE",
  });
}

