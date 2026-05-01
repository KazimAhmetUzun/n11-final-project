import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { login } from "../api/authApi";
import { saveAuth } from "../utils/authStorage";

function Login({ onLogin }) {
    const navigate = useNavigate();

    const [form, setForm] = useState({
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

    const extractToken = (data) => {
        return data.token || data.accessToken || data.jwt || data.data?.token;
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!form.email || !form.password) {
            setError("Email ve şifre zorunludur.");
            return;
        }

        try {
            setLoading(true);
            setError("");

            const data = await login(form);
            const token = extractToken(data);

            if (!token) {
                setError("Login başarılı ancak token response içinde bulunamadı.");
                return;
            }

            saveAuth(token, form.email);

            if (onLogin) {
                onLogin();
            }

            navigate("/");
        } catch (err) {
            console.error(err);
            setError(err.response?.data?.message || "Giriş başarısız.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <section className="auth-page">
            <div className="auth-card">
                <h2>Giriş Yap</h2>

                <form className="auth-form" onSubmit={handleSubmit}>
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
                        {loading ? "Giriş yapılıyor..." : "Giriş Yap"}
                    </button>
                </form>

                <p>
                    Hesabın yok mu? <Link to="/register">Kayıt ol</Link>
                </p>
            </div>
        </section>
    );
}

export default Login;