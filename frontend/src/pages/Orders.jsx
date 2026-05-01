import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { getOrdersByUserEmail } from "../api/orderApi";
import { isLoggedIn } from "../utils/authStorage";

function Orders() {
    const navigate = useNavigate();

    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const fetchOrders = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getOrdersByUserEmail();
            setOrders(data || []);
        } catch (err) {
            console.error(err);
            setError("Siparişler yüklenirken bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (!isLoggedIn()) {
            navigate("/login");
            return;
        }

        fetchOrders();
    }, []);

    if (loading) {
        return <p>Siparişler yükleniyor...</p>;
    }

    if (error) {
        return <p className="error-message">{error}</p>;
    }

    return (
        <section>
            <h2>Siparişlerim</h2>

            {orders.length === 0 ? (
                <p>Henüz siparişiniz bulunmuyor.</p>
            ) : (
                <div className="orders-list">
                    {orders.map((order) => (
                        <div className="order-card" key={order.id}>
                            <div>
                                <h3>Sipariş #{order.id}</h3>

                                <p>
                                    <strong>Durum:</strong>{" "}
                                    <span className={`status-badge status-${order.status?.toLowerCase()}`}>
                    {order.status}
                  </span>
                                </p>

                                <p>
                                    <strong>Toplam:</strong> {order.totalPrice} TL
                                </p>

                                <p>
                                    <strong>Email:</strong> {order.userEmail}
                                </p>
                            </div>

                            <div className="order-actions">
                                <Link to={`/orders/${order.id}`} className="detail-button">
                                    Detaya Git
                                </Link>

                                {order.status === "CREATED" && (
                                    <Link
                                        to={`/orders/${order.id}/payment`}
                                        className="primary-button"
                                    >
                                        Ödeme Yap
                                    </Link>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </section>
    );
}

export default Orders;