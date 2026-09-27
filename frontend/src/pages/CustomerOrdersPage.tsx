import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { cancelOrder, getMyOrders } from "../api/orders";
import OrderCard from "../components/OrderCard";
import type { Order } from "../types/order";

type LoadState = "loading" | "success" | "error";

export default function CustomerOrdersPage() {
  const [searchParams] = useSearchParams();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loadState, setLoadState] = useState<LoadState>("loading");
  const [errorMessage, setErrorMessage] = useState("");
  const [cancellingId, setCancellingId] = useState<number | null>(null);

  async function loadOrders() {
    setLoadState("loading"); setErrorMessage("");
    try { setOrders(await getMyOrders()); setLoadState("success"); }
    catch (error) { setErrorMessage(error instanceof Error ? error.message : "Orders could not be loaded."); setLoadState("error"); }
  }

  useEffect(() => { void loadOrders(); }, []);

  async function handleCancel(orderId: number) {
    if (!window.confirm(`Cancel order #${orderId}?`)) return;
    setCancellingId(orderId); setErrorMessage("");
    try {
      const updated = await cancelOrder(orderId);
      setOrders((current) => current.map((order) => order.id === updated.id ? updated : order));
    } catch (error) { setErrorMessage(error instanceof Error ? error.message : "The order could not be cancelled."); }
    finally { setCancellingId(null); }
  }

  return <section className="content-page customer-page">
    <div className="page-title"><div><p className="eyebrow">Customer account</p><h1>Your orders</h1></div></div>
    {searchParams.get("placed") && <div className="form-notice form-notice--success" role="status">Order #{searchParams.get("placed")} was placed successfully.</div>}
    {errorMessage && <div className="form-notice form-notice--error" role="alert">{errorMessage}</div>}
    {loadState === "loading" && <div className="status-panel" role="status"><span className="spinner" aria-hidden="true" />Loading orders…</div>}
    {loadState === "error" && <button className="button button-small" type="button" onClick={loadOrders}>Try again</button>}
    {loadState === "success" && orders.length === 0 && <div className="status-panel"><div><strong>No orders yet.</strong><p>Your first order will appear here.</p></div></div>}
    {loadState === "success" && orders.length > 0 && <div className="order-list">{orders.map((order) => <OrderCard key={order.id} order={order} cancelling={cancellingId === order.id} onCancel={() => handleCancel(order.id)} />)}</div>}
  </section>;
}
