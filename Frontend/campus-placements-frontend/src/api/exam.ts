import { QuizAdminDetail, QuizAdminRequest } from "types/exam";
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

