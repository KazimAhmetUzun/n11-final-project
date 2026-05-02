import { useState } from "react";
import { Link, Route, Routes, useNavigate } from "react-router-dom";
import kau61Logo from "./assets/kau61-logo.png";
import Cart from "./pages/Cart";
import Login from "./pages/Login";
import OrderDetail from "./pages/OrderDetail";
import Orders from "./pages/Orders";
import Payment from "./pages/Payment";
import ProductDetail from "./pages/ProductDetail";
import ProductList from "./pages/ProductList";
import Register from "./pages/Register";
import VerifyEmail from "./pages/VerifyEmail";
import { clearAuth, getUserEmail, isLoggedIn } from "./utils/authStorage";
import "./App.css";

function App() {
    const navigate = useNavigate();
    const [loggedIn, setLoggedIn] = useState(isLoggedIn());

    const handleLogin = () => {
        setLoggedIn(true);
    };

    const handleLogout = () => {
        clearAuth();
        setLoggedIn(false);
        navigate("/");
    };

    return (
        <div className="app">
            <header className="header">
                <div className="branding">
                    <Link to="/" className="branding-link">
                        <img src={kau61Logo} alt="KAU61 Logo" className="brand-logo" />

                        <div className="brand-text">
                            <h1>KAU61</h1>
                            <p>N 11 Final Project Fullstack E-Commerce Application</p>
                        </div>
                    </Link>
                </div>

                <nav className="nav">
                    <Link to="/">Ürünler</Link>
                    <Link to="/cart">Sepetim</Link>

                    {loggedIn ? (
                        <>
                            <Link to="/orders">Siparişlerim</Link>
                            <span className="user-email">{getUserEmail()}</span>
                            <button className="logout-button" onClick={handleLogout}>
                                Çıkış Yap
                            </button>
                        </>
                    ) : (
                        <>
                            <Link to="/login">Giriş Yap</Link>
                            <Link to="/register">Kayıt Ol</Link>
                        </>
                    )}
                </nav>
            </header>

            <main className="container">
                <Routes>
                    <Route path="/" element={<ProductList />} />
                    <Route path="/products/:id" element={<ProductDetail />} />
                    <Route path="/cart" element={<Cart />} />
                    <Route path="/orders" element={<Orders />} />
                    <Route path="/orders/:orderId/payment" element={<Payment />} />
                    <Route path="/orders/:orderId" element={<OrderDetail />} />
                    <Route path="/login" element={<Login onLogin={handleLogin} />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/verify-email" element={<VerifyEmail />} />
                </Routes>
            </main>
        </div>
    );
}

export default App;