import { Route, Routes } from "react-router-dom";
import AppLayout from "./layouts/AppLayout";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import NotFoundPage from "./pages/NotFoundPage";
import RestaurantPage from "./pages/RestaurantPage";

export default function App() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/restaurants/:restaurantId" element={<RestaurantPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Route>
    </Routes>
  );
}
