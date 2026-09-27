import { apiRequest } from "./client";
import type { AuthResponse, RegisterResponse } from "../types/auth";

type Credentials = { email: string; password: string };

export function login(credentials: Credentials): Promise<AuthResponse> {
  return apiRequest<AuthResponse>("/api/auth/login", { method: "POST", body: JSON.stringify(credentials) });
}

export function register(credentials: Credentials): Promise<RegisterResponse> {
  return apiRequest<RegisterResponse>("/api/auth/register", { method: "POST", body: JSON.stringify(credentials) });
}
