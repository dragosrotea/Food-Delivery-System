import type { MenuItem } from "./menuItem";

export type CartRestaurant = { id: number; name: string };
export type CartItem = Pick<MenuItem, "id" | "name" | "price"> & { quantity: number };
export type CartState = { restaurant: CartRestaurant | null; items: CartItem[] };
