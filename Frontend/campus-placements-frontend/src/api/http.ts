export type HttpOptions = {
  method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  body?: unknown;
  headers?: Record<string, string>;
  auth?: boolean;
};

const BASE = process.env.REACT_APP_API_BASE ?? '';

let _token: string | null = localStorage.getItem('auth_token') || null;

export function setAuthToken(token?: string) {
  _token = token ?? null;
  if (_token) localStorage.setItem('auth_token', _token);
  else localStorage.removeItem('auth_token');
}

export function getAuthToken() {
  return _token;
}

export class HttpError extends Error {
  status: number;
  constructor(status: number, message: string) {
    super(message);
    this.status = status;
  }
}

export async function http<T>(path: string, opts: HttpOptions = {}): Promise<T> {
  const { method = 'GET', body, headers = {}, auth = true } = opts;

  const res = await fetch(`${BASE}${path}`, {
    method,
    headers: {
      'Content-Type': 'application/json',
      ...(auth && _token ? { Authorization: `Bearer ${_token}` } : {}),
      ...headers,
    },
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  let data: any = null;
  try { data = await res.json(); } catch {}

  if (!res.ok) {
    throw new HttpError(res.status, data?.message || res.statusText);
  }
  return data as T;
}
