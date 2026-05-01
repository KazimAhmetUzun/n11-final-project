import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getOrderById } from "../api/orderApi";

function OrderDetail() {
    const { orderId } = useParams();

    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const fetchOrder = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getOrderById(orderId);
            setOrder(data);
        } catch (err) {
            console.error(err);
            setError("Sipariş detayı yüklenirken bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchOrder();
    }, [orderId]);

    if (loading) {
        return <p>Sipariş yükleniyor...</p>;
    }

    if (error) {
        return <p className="error-message">{error}</p>;
    }

    if (!order) {
        return null;
    }

    return (
        <section>
            <Link to="/orders" className="back-link">
                ← Siparişlerime geri dön
            </Link>

            <div className="order-detail-card">
                <h2>Sipariş Detayı</h2>

                <p>
                    <strong>Sipariş ID:</strong> {order.id}
                </p>

                <p>
                    <strong>Kullanıcı:</strong> {order.userEmail}
                </p>

                <p>
                    <strong>Durum:</strong> {order.status}
                </p>

                <p>
                    <strong>Toplam:</strong> {order.totalPrice} TL
                </p>

                {order.status === "CREATED" && (
                    <Link to={`/orders/${order.id}/payment`} className="primary-button">
                    Ödeme Yap
                    </Link>
                )}

                <h3>Ürünler</h3>

                <div className="order-items">
                    {order.items?.map((item) => (
                        <div className="order-item" key={item.id}>
                            <span>{item.productName}</span>
                            <span>Adet: {item.quantity}</span>
                            <span>Toplam: {item.totalPrice} TL</span>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}

export default OrderDetail;