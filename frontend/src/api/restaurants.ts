import { apiRequest } from "./client";
import type { MenuItem } from "../types/menuItem";
import type { Restaurant } from "../types/restaurant";

export function getRestaurants(): Promise<Restaurant[]> {
  return apiRequest<Restaurant[]>("/api/restaurants");
}

export function getRestaurant(restaurantId: number): Promise<Restaurant> {
  return apiRequest<Restaurant>(`/api/restaurants/${restaurantId}`);
}

export function getRestaurantMenu(restaurantId: number): Promise<MenuItem[]> {
  return apiRequest<MenuItem[]>(
    `/api/restaurants/${restaurantId}/menu-items`,
  );
}
