import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
    clearCart,
    getCart,
    removeCartItem,
    updateCartItem,
} from "../api/cartApi";
import { createOrder } from "../api/orderApi";
import { isLoggedIn } from "../utils/authStorage";

function Cart() {
    const [items, setItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const fetchCart = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getCart();
            setItems(data || []);
        } catch (err) {
            console.error(err);
            setError("Sepet yüklenirken bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (!isLoggedIn()) {
            navigate("/login");
            return;
        }

        fetchCart();
    }, []);

    const handleIncrease = async (item) => {
        await updateCartItem(item.id, item.quantity + 1);
        fetchCart();
    };

    const handleCreateOrder = async () => {
        if (items.length === 0) {
            setError("Sepetiniz boş.");
            return;
        }

        try {
            setLoading(true);
            setError("");

            const order = await createOrder(items);

            navigate(`/orders/${order.id}/payment`);
        } catch (err) {
            console.error(err);
            setError("Sipariş oluşturulurken bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    const handleDecrease = async (item) => {
        if (item.quantity <= 1) {
            return;
        }

        await updateCartItem(item.id, item.quantity - 1);
        fetchCart();
    };

    const handleRemove = async (itemId) => {
        await removeCartItem(itemId);
        fetchCart();
    };

    const handleClearCart = async () => {
        await clearCart();
        fetchCart();
    };

    const totalPrice = items.reduce((total, item) => {
        return total + Number(item.totalPrice || 0);
    }, 0);

    if (loading) {
        return <p>Sepet yükleniyor...</p>;
    }

    if (error) {
        return <p className="error-message">{error}</p>;
    }

    return (
        <section>
            <h2>Sepetim</h2>

            {items.length === 0 ? (
                <p>Sepetiniz boş.</p>
            ) : (
                <>
                    <div className="cart-list">
                        {items.map((item) => (
                            <div className="cart-item" key={item.id}>
                                <div>
                                    <h3>{item.productName}</h3>
                                    <p>Birim Fiyat: {item.price} TL</p>
                                    <p>Toplam: {item.totalPrice} TL</p>
                                </div>

                                <div className="cart-actions">
                                    <button onClick={() => handleDecrease(item)}>-</button>
                                    <span>{item.quantity}</span>
                                    <button onClick={() => handleIncrease(item)}>+</button>
                                    <button
                                        className="danger-button"
                                        onClick={() => handleRemove(item.id)}
                                    >
                                        Sil
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="cart-summary">
                        <h3>Genel Toplam: {totalPrice.toFixed(2)} TL</h3>

                        <div className="cart-summary-actions">
                            <button className="primary-button" onClick={handleCreateOrder}>
                                Sipariş Oluştur
                            </button>

                            <button className="danger-button" onClick={handleClearCart}>
                                Sepeti Temizle
                            </button>
                        </div>
                    </div>
                </>
            )}
        </section>
    );
}

export default Cart;