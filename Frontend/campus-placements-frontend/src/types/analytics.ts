
export type ScoreBucket = {
  label: string;
  count: number;
};

export type QuizAnalytics = {
  quizId: number;
  quizTitle: string;
  jobRole?: string;
  myAttemptsCount: number;
  myAverageScorePercent: number;
  myBestScorePercent: number;
  myLastScorePercent: number | null;
  totalAttemptsCount: number;
  overallAverageScorePercent: number;
  overallBestScorePercent: number;
  scoreDistribution: ScoreBucket[];
};

export type CompanyAnalytics = {
  companyId: number;
  companyName: string;
  myAttemptsCount: number;
  myAverageScorePercent: number;
  myBestScorePercent: number;
  quizzes: QuizAnalytics[];
};

export type StudentAnalyticsOverview = {
  companies: CompanyAnalytics[];
};