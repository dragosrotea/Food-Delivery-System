import type { Order, OrderStatus } from "../types/order";

const labels: Record<OrderStatus, string> = {
  PLACED: "Placed", CONFIRMED: "Confirmed", PREPARING: "Preparing",
  READY_FOR_PICKUP: "Ready for pickup", OUT_FOR_DELIVERY: "Out for delivery",
  DELIVERED: "Delivered", CANCELLED: "Cancelled",
};

type Props = { order: Order; cancelling: boolean; onCancel: () => void };

export default function OrderCard({ order, cancelling, onCancel }: Props) {
  const canCancel = order.status === "PLACED" || order.status === "CONFIRMED";
  const createdAt = new Intl.DateTimeFormat("en-GB", { dateStyle: "medium", timeStyle: "short" }).format(new Date(order.createdAt));

  return <article className="order-card">
    <header><div><p className="eyebrow">Order #{order.id}</p><h3>{order.restaurantName}</h3><small>{createdAt}</small></div>
      <span className={`order-status order-status--${order.status.toLowerCase()}`}>{labels[order.status]}</span>
    </header>
    <ul>{order.items.map((item) => <li key={item.id}><span>{item.quantity} × {item.name}</span><strong>{item.lineTotal.toFixed(2)} RON</strong></li>)}</ul>
    <div className="order-card__details"><span>{order.deliveryStreet}, {order.deliveryCity}</span><strong>{order.totalPrice.toFixed(2)} RON</strong></div>
    {order.driverEmail && <p className="order-driver">Driver: {order.driverEmail}</p>}
    {canCancel && <button className="secondary-button" type="button" onClick={onCancel} disabled={cancelling}>{cancelling ? "Cancelling…" : "Cancel order"}</button>}
  </article>;
}
