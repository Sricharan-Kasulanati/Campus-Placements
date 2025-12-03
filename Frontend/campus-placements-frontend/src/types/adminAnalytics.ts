export type AdminCompanyAnalytics = {
  companyId: number;
  companyName: string;
  registeredStudents: number;
  totalAttempts: number;
  avgScorePercent: number;
  passRate50Percent: number;
};

export type AdminAnalyticsOverview = {
  totalStudents: number;
  totalCompanies: number;
  totalQuizzes: number;
  totalAttempts: number;
  companies: AdminCompanyAnalytics[];
};