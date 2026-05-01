import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { register } from "../api/authApi";

function Register() {
    const navigate = useNavigate();

    const [form, setForm] = useState({
        fullName: "",
        email: "",
        password: "",
    });

    const [loading, setLoading] = useState(false);
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

        if (!form.fullName || !form.email || !form.password) {
            setError("Lütfen tüm alanları doldurun.");
            return;
        }

        try {
            setLoading(true);
            setError("");

            await register(form);

            navigate(`/verify-email?email=${encodeURIComponent(form.email)}`);
        } catch (err) {
            console.error(err);
            setError(err.response?.data?.message || "Kayıt olurken bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <section className="auth-page">
            <div className="auth-card">
                <h2>Kayıt Ol</h2>

                <form className="auth-form" onSubmit={handleSubmit}>
                    <label>
                        Ad Soyad
                        <input
                            name="fullName"
                            value={form.fullName}
                            onChange={handleChange}
                            placeholder="Kazım Ahmet Uzun"
                        />
                    </label>

                    <label>
                        Email
                        <input
                            name="email"
                            type="email"
                            value={form.email}
                            onChange={handleChange}
                            placeholder="test@example.com"
                        />
                    </label>

                    <label>
                        Şifre
                        <input
                            name="password"
                            type="password"
                            value={form.password}
                            onChange={handleChange}
                            placeholder="******"
                        />
                    </label>

                    {error && <p className="error-message">{error}</p>}

                    <button className="primary-button" type="submit" disabled={loading}>
                        {loading ? "Kayıt yapılıyor..." : "Kayıt Ol"}
                    </button>
                </form>

                <p>
                    Zaten hesabın var mı? <Link to="/login">Giriş yap</Link>
                </p>
            </div>
        </section>
    );
}

export default Register;