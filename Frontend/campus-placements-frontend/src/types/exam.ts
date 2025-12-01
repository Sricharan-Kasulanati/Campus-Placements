export type QuizQuestionAdmin = {
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