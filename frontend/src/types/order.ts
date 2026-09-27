export type OrderStatus = "PLACED" | "CONFIRMED" | "PREPARING" | "READY_FOR_PICKUP" | "OUT_FOR_DELIVERY" | "DELIVERED" | "CANCELLED";

export type OrderItem = {
  id: number;
  menuItemId: number;
  name: string;
  unitPrice: number;
  quantity: number;
  lineTotal: number;
};

export type Order = {
  id: number;
  customerId: number;
  customerEmail: string;
  restaurantId: number;
  restaurantName: string;
  driverId: number | null;
  driverEmail: string | null;
  status: OrderStatus;
  totalPrice: number;
  deliveryStreet: string;
  deliveryCity: string;
  createdAt: string;
  items: OrderItem[];
};

export type CreateOrderRequest = {
  restaurantId: number;
  deliveryStreet: string;
  deliveryCity: string;
  items: { menuItemId: number; quantity: number }[];
};
