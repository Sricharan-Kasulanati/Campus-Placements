import { http } from './http';
import type { UpdateProfileRequest, UserProfile } from '../types/user';

export function getProfile() {
  return http<UserProfile>('/api/user/me');
}

export function updateProfile(body: UpdateProfileRequest) {
  return http<UserProfile>('/api/user/me', { method: 'PUT', body });
}
