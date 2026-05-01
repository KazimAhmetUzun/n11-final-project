import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { createPayment } from "../api/paymentApi";
import { clearCart } from "../api/cartApi";

function Payment() {
    const { orderId } = useParams();

    const [amount, setAmount] = useState("");
    const [card, setCard] = useState({
        cardHolderName: "John Doe",
        cardNumber: "5528790000000008",
        expireMonth: "12",
        expireYear: "2030",
        cvc: "123",
    });

    const [buyer, setBuyer] = useState({
        name: "Test",
        surname: "User",
        email: "test@example.com",
        identityNumber: "11111111111",
        phone: "+905350000000",
        city: "Istanbul",
        country: "Turkey",
        address: "Test address",
        zipCode: "34000",
    });

    const [loading, setLoading] = useState(false);
    const [result, setResult] = useState(null);
    const [error, setError] = useState("");

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

    const handlePayment = async (event) => {
        event.preventDefault();

        if (!amount || Number(amount) <= 0) {
            setError("Lütfen geçerli bir ödeme tutarı girin.");
            return;
        }

        try {
            setLoading(true);
            setError("");
            setResult(null);

            const paymentResponse = await createPayment({
                orderId: Number(orderId),
                amount: Number(amount),
                card,
                buyer,
            });

            setResult(paymentResponse);

            if (paymentResponse.status === "SUCCESS") {
                await clearCart();
            }

            setResult(paymentResponse);
        } catch (err) {
            console.error(err);
            setError("Ödeme işlemi sırasında bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <section>
            <Link to="/cart" className="back-link">
                ← Sepete geri dön
            </Link>

            <h2>Ödeme Sayfası</h2>
            <p>Sipariş ID: {orderId}</p>

            <form className="payment-form" onSubmit={handlePayment}>
                <div className="form-section">
                    <h3>Ödeme Bilgileri</h3>

                    <label>
                        Ödeme Tutarı
                        <input
                            type="number"
                            step="0.01"
                            value={amount}
                            onChange={(event) => setAmount(event.target.value)}
                            placeholder="Örn: 49999.99"
                        />
                    </label>
                </div>

                <div className="form-section">
                    <h3>Kart Bilgileri</h3>

                    <label>
                        Kart Sahibi
                        <input
                            name="cardHolderName"
                            value={card.cardHolderName}
                            onChange={handleCardChange}
                        />
                    </label>

                    <label>
                        Kart Numarası
                        <input
                            name="cardNumber"
                            value={card.cardNumber}
                            onChange={handleCardChange}
                        />
                    </label>

                    <div className="form-row">
                        <label>
                            Ay
                            <input
                                name="expireMonth"
                                value={card.expireMonth}
                                onChange={handleCardChange}
                            />
                        </label>

                        <label>
                            Yıl
                            <input
                                name="expireYear"
                                value={card.expireYear}
                                onChange={handleCardChange}
                            />
                        </label>

                        <label>
                            CVC
                            <input name="cvc" value={card.cvc} onChange={handleCardChange} />
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

                <button className="primary-button" type="submit" disabled={loading}>
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