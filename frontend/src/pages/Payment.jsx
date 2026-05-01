import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { clearCart } from "../api/cartApi";
import { getOrderById } from "../api/orderApi";
import { createPayment } from "../api/paymentApi";
import { isLoggedIn } from "../utils/authStorage";
import { getUserEmail } from "../utils/authStorage";

function Payment() {
    const { orderId } = useParams();

    const [order, setOrder] = useState(null);

    const [card, setCard] = useState({
        cardHolderName: "",
        cardNumber: "",
        expireMonth: "",
        expireYear: "",
        cvc: "",
    });

    const [buyer, setBuyer] = useState({
        name: "Test",
        surname: "User",
        email: getUserEmail() || "",
        identityNumber: "11111111111",
        phone: "+905350000000",
        city: "Istanbul",
        country: "Turkey",
        address: "Test address",
        zipCode: "34000",
    });

    const [loading, setLoading] = useState(false);
    const [orderLoading, setOrderLoading] = useState(false);
    const [result, setResult] = useState(null);
    const [error, setError] = useState("");

    const fetchOrder = async () => {
        try {
            setOrderLoading(true);
            setError("");

            const data = await getOrderById(orderId);
            setOrder(data);
        } catch (err) {
            console.error(err);
            setError("Sipariş bilgisi yüklenirken bir hata oluştu.");
        } finally {
            setOrderLoading(false);
        }
    };

    useEffect(() => {
        if (!isLoggedIn()) {
            navigate("/login");
            return;
        }

        fetchOrder();
    }, [orderId]);

    const handleCardChange = (event) => {
        const { name, value } = event.target;

        setCard((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleBuyerChange = (event) => {
        const { name, value } = event.target;

        setBuyer((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const validateCardForm = () => {
        if (
            !card.cardHolderName ||
            !card.cardNumber ||
            !card.expireMonth ||
            !card.expireYear ||
            !card.cvc
        ) {
            setError("Lütfen kart bilgilerini eksiksiz girin.");
            return false;
        }

        return true;
    };

    const handlePayment = async (event) => {
        event.preventDefault();

        if (!order) {
            setError("Sipariş bilgisi bulunamadı.");
            return;
        }

        if (!validateCardForm()) {
            return;
        }

        try {
            setLoading(true);
            setError("");
            setResult(null);

            const paymentResponse = await createPayment({
                orderId: Number(orderId),
                amount: Number(order.totalPrice),
                card,
                buyer,
            });

            setResult(paymentResponse);

            if (paymentResponse.status === "SUCCESS") {
                await clearCart();
                await fetchOrder();
            }
        } catch (err) {
            console.error(err);
            setError("Ödeme işlemi sırasında bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    if (orderLoading) {
        return <p>Sipariş bilgisi yükleniyor...</p>;
    }

    return (
        <section>
            <Link to="/cart" className="back-link">
                ← Sepete geri dön
            </Link>

            <h2>Ödeme Sayfası</h2>

            {order && (
                <div className="payment-order-summary">
                    <p>
                        <strong>Sipariş ID:</strong> {order.id}
                    </p>

                    <p>
                        <strong>Sipariş Durumu:</strong> {order.status}
                    </p>

                    <p className="payment-total">
                        <strong>Ödenecek Tutar:</strong> {order.totalPrice} TL
                    </p>
                </div>
            )}

            <form className="payment-form" onSubmit={handlePayment}>
                <div className="form-section">
                    <h3>Kart Bilgileri</h3>

                    <p className="form-hint">
                        Iyzico sandbox test kartı: 5528790000000008 / 12 / 2030 / 123
                    </p>

                    <label>
                        Kart Sahibi
                        <input
                            name="cardHolderName"
                            value={card.cardHolderName}
                            onChange={handleCardChange}
                            placeholder="John Doe"
                        />
                    </label>

                    <label>
                        Kart Numarası
                        <input
                            name="cardNumber"
                            value={card.cardNumber}
                            onChange={handleCardChange}
                            placeholder="5528790000000008"
                        />
                    </label>

                    <div className="form-row">
                        <label>
                            Ay
                            <input
                                name="expireMonth"
                                value={card.expireMonth}
                                onChange={handleCardChange}
                                placeholder="12"
                            />
                        </label>

                        <label>
                            Yıl
                            <input
                                name="expireYear"
                                value={card.expireYear}
                                onChange={handleCardChange}
                                placeholder="2030"
                            />
                        </label>

                        <label>
                            CVC
                            <input
                                name="cvc"
                                value={card.cvc}
                                onChange={handleCardChange}
                                placeholder="123"
                            />
                        </label>
                    </div>
                </div>

                <div className="form-section">
                    <h3>Alıcı Bilgileri</h3>

                    <div className="form-row">
                        <label>
                            Ad
                            <input name="name" value={buyer.name} onChange={handleBuyerChange} />
                        </label>

                        <label>
                            Soyad
                            <input
                                name="surname"
                                value={buyer.surname}
                                onChange={handleBuyerChange}
                            />
                        </label>
                    </div>

                    <label>
                        Email
                        <input name="email" value={buyer.email} onChange={handleBuyerChange} />
                    </label>

                    <label>
                        T.C. Kimlik No
                        <input
                            name="identityNumber"
                            value={buyer.identityNumber}
                            onChange={handleBuyerChange}
                        />
                    </label>

                    <label>
                        Telefon
                        <input name="phone" value={buyer.phone} onChange={handleBuyerChange} />
                    </label>

                    <div className="form-row">
                        <label>
                            Şehir
                            <input name="city" value={buyer.city} onChange={handleBuyerChange} />
                        </label>

                        <label>
                            Ülke
                            <input
                                name="country"
                                value={buyer.country}
                                onChange={handleBuyerChange}
                            />
                        </label>
                    </div>

                    <label>
                        Adres
                        <input
                            name="address"
                            value={buyer.address}
                            onChange={handleBuyerChange}
                        />
                    </label>

                    <label>
                        Posta Kodu
                        <input
                            name="zipCode"
                            value={buyer.zipCode}
                            onChange={handleBuyerChange}
                        />
                    </label>
                </div>

                {error && <p className="error-message">{error}</p>}

                <button
                    className="primary-button"
                    type="submit"
                    disabled={loading || !order}
                >
                    {loading ? "Ödeme yapılıyor..." : "Ödemeyi Tamamla"}
                </button>
            </form>

            {result && (
                <div className="payment-result">
                    <h3>Ödeme Sonucu</h3>
                    <p>
                        <strong>Status:</strong> {result.status}
                    </p>
                    <p>
                        <strong>Transaction ID:</strong> {result.transactionId || "-"}
                    </p>
                    <p>
                        <strong>Hata:</strong> {result.errorMessage || "-"}
                    </p>

                    <Link to={`/orders/${orderId}`} className="detail-button">
                        Siparişi Görüntüle
                    </Link>
                </div>
            )}
        </section>
    );
}

export default Payment;