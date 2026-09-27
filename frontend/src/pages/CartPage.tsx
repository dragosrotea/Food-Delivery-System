import { Link } from "react-router-dom";
import { useCart } from "../cart/CartContext";
import CartItemRow from "../components/CartItemRow";

export default function CartPage() {
  const { restaurant, items, total, increaseQuantity, decreaseQuantity, removeItem, clearCart } = useCart();

  if (!restaurant || items.length === 0) return <section className="content-page compact-page"><div className="placeholder-card">
    <p className="eyebrow">Your cart</p><h1>Your cart is empty.</h1><p>Choose a restaurant and add something from its menu.</p>
    <Link className="button" to="/">Browse restaurants</Link>
  </div></section>;

  return <section className="content-page customer-page">
    <div className="page-title"><div><p className="eyebrow">{restaurant.name}</p><h1>Your cart</h1></div><button className="text-action" type="button" onClick={clearCart}>Clear cart</button></div>
    <div className="cart-layout"><div className="cart-list">
      {items.map((item) => <CartItemRow key={item.id} item={item} onIncrease={() => increaseQuantity(item.id)} onDecrease={() => decreaseQuantity(item.id)} onRemove={() => removeItem(item.id)} />)}
    </div><aside className="cart-summary"><p>Estimated total</p><strong>{total.toFixed(2)} RON</strong><small>The backend confirms current prices when you order.</small><Link className="button" to="/checkout">Continue to checkout</Link></aside></div>
  </section>;
}
