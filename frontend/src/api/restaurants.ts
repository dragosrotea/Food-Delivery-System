import { apiRequest } from "./client";
import type { Restaurant } from "../types/restaurant";

export function getRestaurants(): Promise<Restaurant[]> {
  return apiRequest<Restaurant[]>("/api/restaurants");
}
