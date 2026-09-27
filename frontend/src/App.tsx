import { Route, Routes } from "react-router-dom";
import AppLayout from "./layouts/AppLayout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import NotFoundPage from "./pages/NotFoundPage";
import RestaurantPage from "./pages/RestaurantPage";
import RegisterPage from "./pages/RegisterPage";
import AccessDeniedPage from "./pages/AccessDeniedPage";
import RoleHomePage from "./pages/RoleHomePage";
import ProtectedRoute from "./components/ProtectedRoute";
import CartPage from "./pages/CartPage";
import CheckoutPage from "./pages/CheckoutPage";
import CustomerOrdersPage from "./pages/CustomerOrdersPage";

export default function App() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/restaurants/:restaurantId" element={<RestaurantPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/forbidden" element={<AccessDeniedPage />} />
        <Route path="/cart" element={<CartPage />} />
        <Route element={<ProtectedRoute allowedRoles={["CUSTOMER"]} />}>
          <Route path="/account" element={<CustomerOrdersPage />} />
          <Route path="/checkout" element={<CheckoutPage />} />
        </Route>
        <Route element={<ProtectedRoute allowedRoles={["DRIVER"]} />}>
          <Route path="/driver" element={<RoleHomePage />} />
        </Route>
        <Route element={<ProtectedRoute allowedRoles={["ADMIN"]} />}>
          <Route path="/admin" element={<RoleHomePage />} />
        </Route>
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}
