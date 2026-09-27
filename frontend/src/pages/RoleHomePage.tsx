import { useAuth } from "../auth/AuthContext";

const descriptions = {
  CUSTOMER: "Your cart and order history will be added in the next customer workflow step.",
  DRIVER: "Available and assigned deliveries will be added in the driver dashboard step.",
  ADMIN: "Restaurant, menu, driver and order controls will be added in the admin dashboard step.",
};

export default function RoleHomePage() {
  const { user } = useAuth();
  if (!user) return null;
  return <section className="page-section compact-page"><div className="placeholder-card">
    <p className="eyebrow">{user.role.toLowerCase()} account</p><h1>You are logged in.</h1><p>{descriptions[user.role]}</p>
  </div></section>;
}
