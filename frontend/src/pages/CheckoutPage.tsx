import { useState, type FormEvent } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { placeOrder } from "../api/orders";
import { useCart } from "../cart/CartContext";

export default function CheckoutPage() {
  const { restaurant, items, total, clearCart } = useCart();
  const navigate = useNavigate();
  const [street, setStreet] = useState("");
  const [city, setCity] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [submitting, setSubmitting] = useState(false);

  if (!restaurant || items.length === 0) return <Navigate to="/cart" replace />;

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!restaurant) return;
    setSubmitting(true); setErrorMessage("");
    try {
      const order = await placeOrder({ restaurantId: restaurant.id, deliveryStreet: street.trim(), deliveryCity: city.trim(), items: items.map((item) => ({ menuItemId: item.id, quantity: item.quantity })) });
      clearCart();
      navigate(`/account?placed=${order.id}`, { replace: true });
    } catch (error) {
      setErrorMessage(error instanceof Error ? error.message : "The order could not be placed.");
    } finally { setSubmitting(false); }
  }

  return <section className="content-page customer-page"><Link className="back-link" to="/cart">← Back to cart</Link>
    <div className="page-title"><div><p className="eyebrow">Final step</p><h1>Delivery details</h1></div></div>
    <div className="checkout-layout"><form className="checkout-form" onSubmit={submit}>
      {errorMessage && <div className="form-notice form-notice--error" role="alert">{errorMessage}</div>}
      <label>Street address<input value={street} onChange={(event) => setStreet(event.target.value)} required maxLength={160} autoComplete="street-address" /></label>
      <label>City<input value={city} onChange={(event) => setCity(event.target.value)} required maxLength={100} autoComplete="address-level2" /></label>
      <button className="button" type="submit" disabled={submitting}>{submitting ? "Placing order…" : "Place order"}</button>
    </form><aside className="cart-summary"><p>{restaurant.name}</p><ul>{items.map((item) => <li key={item.id}>{item.quantity} × {item.name}</li>)}</ul><strong>{total.toFixed(2)} RON</strong></aside></div>
  </section>;
}
