export type PracticeTest = {
  id: number;
  companyId: number;
  title: string;
  fileUrl: string;
  fileSize: number;
  contentType: string;
  uploadedAt: string;
  jobRole?: string;
  description?: string;
};