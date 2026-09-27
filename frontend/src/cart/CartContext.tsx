import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import type { MenuItem } from "../types/menuItem";
import type { CartRestaurant, CartState } from "../types/cart";

const STORAGE_KEY = "food-delivery-cart";
const emptyCart: CartState = { restaurant: null, items: [] };

function loadCart(): CartState {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (!stored) return emptyCart;
    const cart = JSON.parse(stored) as CartState;
    return cart.restaurant && Array.isArray(cart.items) ? cart : emptyCart;
  } catch {
    return emptyCart;
  }
}

type CartContextValue = CartState & {
  itemCount: number;
  total: number;
  addItem: (restaurant: CartRestaurant, item: MenuItem) => boolean;
  replaceCart: (restaurant: CartRestaurant, item: MenuItem) => void;
  increaseQuantity: (itemId: number) => void;
  decreaseQuantity: (itemId: number) => void;
  removeItem: (itemId: number) => void;
  clearCart: () => void;
};

const CartContext = createContext<CartContextValue | null>(null);

export function CartProvider({ children }: { children: ReactNode }) {
  const [cart, setCart] = useState<CartState>(loadCart);

  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(cart));
  }, [cart]);

  function addItem(restaurant: CartRestaurant, item: MenuItem): boolean {
    if (cart.restaurant && cart.restaurant.id !== restaurant.id) return false;
    setCart((current) => {
      const existing = current.items.find((entry) => entry.id === item.id);
      const items = existing
        ? current.items.map((entry) => entry.id === item.id ? { ...entry, quantity: entry.quantity + 1 } : entry)
        : [...current.items, { id: item.id, name: item.name, price: item.price, quantity: 1 }];
      return { restaurant, items };
    });
    return true;
  }

  function replaceCart(restaurant: CartRestaurant, item: MenuItem) {
    setCart({ restaurant, items: [{ id: item.id, name: item.name, price: item.price, quantity: 1 }] });
  }

  function changeQuantity(itemId: number, amount: number) {
    setCart((current) => ({ ...current, items: current.items.map((item) => item.id === itemId ? { ...item, quantity: Math.max(1, item.quantity + amount) } : item) }));
  }

  function removeItem(itemId: number) {
    setCart((current) => {
      const items = current.items.filter((item) => item.id !== itemId);
      return items.length ? { ...current, items } : emptyCart;
    });
  }

  const itemCount = cart.items.reduce((sum, item) => sum + item.quantity, 0);
  const total = cart.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const value = useMemo(() => ({
    ...cart, itemCount, total, addItem, replaceCart,
    increaseQuantity: (id: number) => changeQuantity(id, 1),
    decreaseQuantity: (id: number) => changeQuantity(id, -1),
    removeItem, clearCart: () => setCart(emptyCart),
  }), [cart, itemCount, total]);

  return <CartContext.Provider value={value}>{children}</CartContext.Provider>;
}

export function useCart(): CartContextValue {
  const context = useContext(CartContext);
  if (!context) throw new Error("useCart must be used inside CartProvider");
  return context;
}
