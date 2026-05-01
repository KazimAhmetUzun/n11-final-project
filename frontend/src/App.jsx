import { Link, Route, Routes } from "react-router-dom";
import Cart from "./pages/Cart";
import ProductDetail from "./pages/ProductDetail";
import ProductList from "./pages/ProductList";
import Payment from "./pages/Payment";
import OrderDetail from "./pages/OrderDetail";
import "./App.css";

function App() {
    return (
        <div className="app">
            <header className="header">
                <Link to="/" className="logo-link">
                    <h1>N11 Final Project</h1>
                </Link>

                <p>Fullstack E-Commerce Application</p>

                <nav className="nav">
                    <Link to="/">Ürünler</Link>
                    <Link to="/cart">Sepetim</Link>
                </nav>
            </header>

            <main className="container">
                <Routes>
                    <Route path="/" element={<ProductList />} />
                    <Route path="/products/:id" element={<ProductDetail />} />
                    <Route path="/cart" element={<Cart />} />
                    <Route path="/orders/:orderId/payment" element={<Payment />} />
                    <Route path="/orders/:orderId" element={<OrderDetail />} />
                </Routes>
            </main>
        </div>
    );
}

export default App;