import { StudentAnalyticsOverview } from "types/analytics";
import { http } from "./http";

export async function fetchStudentAnalyticsOverview(): Promise<StudentAnalyticsOverview> {
  return http<StudentAnalyticsOverview>("/api/students/me/quiz-analytics/overview", {
    method: "GET",
  });
}