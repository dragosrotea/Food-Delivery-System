import type { CartItem } from "../types/cart";

type Props = {
  item: CartItem;
  onIncrease: () => void;
  onDecrease: () => void;
  onRemove: () => void;
};

export default function CartItemRow({ item, onIncrease, onDecrease, onRemove }: Props) {
  return <article className="cart-row">
    <div><h3>{item.name}</h3><p>{item.price.toFixed(2)} RON each</p></div>
    <div className="quantity-control" aria-label={`${item.name} quantity`}>
      <button type="button" onClick={onDecrease} disabled={item.quantity === 1}>−</button>
      <strong>{item.quantity}</strong>
      <button type="button" onClick={onIncrease}>+</button>
    </div>
    <strong>{(item.price * item.quantity).toFixed(2)} RON</strong>
    <button className="remove-button" type="button" onClick={onRemove}>Remove</button>
  </article>;
}
