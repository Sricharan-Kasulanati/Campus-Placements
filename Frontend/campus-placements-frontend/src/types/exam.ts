export type QuizStudentSummary = {
  id: number;
  title: string;
  description: string | null;
  durationMinutes: number | null;
};

export type QuizQuestionStudent = {
  id: number;
  questionText: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
};

export type QuizForTaking = {
  quizId: number;
  companyId: number;
  jobRole: string;
  title: string;
  description: string | null;
  durationMinutes: number | null;
  questions: QuizQuestionStudent[];
};

export type QuizAnswer = {
  questionId: number;
  selectedOption: string;
};

export type QuestionResult = {
  questionId: number;
  questionText: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
  correctOption: string;
  selectedOption: string | null;
  correct: boolean;
};

export type QuizResult = {
  score: number;
  totalQuestions: number;
  questions: QuestionResult[];
};

export type QuizQuestionAdmin = {
  id?: number;
  questionText: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
  correctOption: string;
};

export type QuizAdminRequest = {
  title: string;
  jobRole: string;
  description?: string;
  durationMinutes?: number;
  active?: boolean;
  questions: QuizQuestionAdmin[];
};

export type QuizSummaryAdmin = {
  id: number;
  companyId: number;
  jobRole: string;
  title: string;
  active: boolean;
  questionCount: number;
};

export type QuizAdminDetail = {
  id: number;
  companyId: number;
  jobRole: string;
  title: string;
  description: string | null;
  durationMinutes: number | null;
  active: boolean;
  questions: QuizQuestionAdmin[];
};