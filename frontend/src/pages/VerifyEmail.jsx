import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { verifyEmail } from "../api/authApi";

function VerifyEmail() {
    const navigate = useNavigate();
    const location = useLocation();

    const queryParams = new URLSearchParams(location.search);
    const initialEmail = queryParams.get("email") || "";

    const [form, setForm] = useState({
        email: initialEmail,
        code: "",
    });

    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const handleChange = (event) => {
        const { name, value } = event.target;

        setForm((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!form.email || !form.code) {
            setError("Email ve doğrulama kodu zorunludur.");
            return;
        }

        try {
            setLoading(true);
            setError("");
            setMessage("");

            await verifyEmail(form);

            setMessage("Email doğrulandı. Giriş sayfasına yönlendiriliyorsunuz.");

            setTimeout(() => {
                navigate("/login");
            }, 1200);
        } catch (err) {
            console.error(err);
            setError(err.response?.data?.message || "Email doğrulama başarısız.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <section className="auth-page">
            <div className="auth-card">
                <h2>Email Doğrulama</h2>

                <p>Email adresine gelen doğrulama kodunu gir.</p>

                <form className="auth-form" onSubmit={handleSubmit}>
                    <label>
                        Email
                        <input
                            name="email"
                            type="email"
                            value={form.email}
                            onChange={handleChange}
                        />
                    </label>

                    <label>
                        Doğrulama Kodu
                        <input
                            name="code"
                            value={form.code}
                            onChange={handleChange}
                            placeholder="123456"
                        />
                    </label>

                    {message && <p className="success-message">{message}</p>}
                    {error && <p className="error-message">{error}</p>}

                    <button className="primary-button" type="submit" disabled={loading}>
                        {loading ? "Doğrulanıyor..." : "Doğrula"}
                    </button>
                </form>

                <p>
                    Koda sahipsen <Link to="/login">giriş sayfasına dön</Link>
                </p>
            </div>
        </section>
    );
}

export default VerifyEmail;