export interface UserProfile {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: 'ADMIN' | 'STUDENT';
}

export interface UpdateProfileRequest {
  firstName: string;
  lastName: string;
  email: string;
  newPassword?: string;
}
