import { AdminAnalyticsOverview } from "types/adminAnalytics";
import { http } from "./http";
import { StudentAnalyticsOverview } from "types/analytics";

export async function fetchAdminAnalyticsOverview(): Promise<AdminAnalyticsOverview> {
  return http<AdminAnalyticsOverview>(
    "/api/admin/students/analytics/overview",
    {
      method: "GET",
    }
  );
}

export async function fetchStudentAnalyticsOverviewForStudent(
  studentId: number
): Promise<StudentAnalyticsOverview> {
  return http<StudentAnalyticsOverview>(
    `/api/admin/students/${studentId}/quiz-analytics/overview`,
    { method: "GET" }
  );
}
