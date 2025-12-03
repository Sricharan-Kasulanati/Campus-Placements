import { http } from './http';
import type { UpdateProfileRequest, UserProfile } from '../types/user';

export function getProfile() {
  return http<UserProfile>('/api/users/getUser');
}

export function updateProfile(body: UpdateProfileRequest) {
  return http<UserProfile>('/api/users/updateUser', { method: 'PUT', body });
}
