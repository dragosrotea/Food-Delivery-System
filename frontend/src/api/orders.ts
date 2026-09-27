import { apiRequest } from "./client";
import type { CreateOrderRequest, Order } from "../types/order";

export function placeOrder(request: CreateOrderRequest): Promise<Order> {
  return apiRequest<Order>("/api/orders", {
    method: "POST",
    authenticated: true,
    body: JSON.stringify(request),
  });
}

export function getMyOrders(): Promise<Order[]> {
  return apiRequest<Order[]>("/api/orders", { authenticated: true });
}

export function cancelOrder(orderId: number): Promise<Order> {
  return apiRequest<Order>(`/api/orders/${orderId}/cancel`, {
    method: "POST",
    authenticated: true,
  });
}
