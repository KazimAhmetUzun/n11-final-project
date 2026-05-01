import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getProductById } from "../api/productApi";
import { addToCart } from "../api/cartApi";

function ProductDetail() {
    const { id } = useParams();

    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [successMessage, setSuccessMessage] = useState("");

    const fetchProduct = async () => {
        try {
            setLoading(true);
            setError("");

            const data = await getProductById(id);
            setProduct(data);
        } catch (err) {
            console.error(err);
            setError("Ürün detayı yüklenirken bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    const handleAddToCart = async () => {
        try {
            setSuccessMessage("");
            await addToCart(product, 1);
            setSuccessMessage("Ürün sepete eklendi.");
        } catch (err) {
            console.error(err);
            setError("Ürün sepete eklenirken bir hata oluştu.");
        }
    };

    useEffect(() => {
        fetchProduct();
    }, [id]);

    if (loading) {
        return <p>Ürün detayı yükleniyor...</p>;
    }

    if (error) {
        return (
            <div>
                <p className="error-message">{error}</p>
                <Link to="/" className="back-link">
                    Ürünlere geri dön
                </Link>
            </div>
        );
    }

    if (!product) {
        return null;
    }

    return (
        <section className="product-detail">
            <Link to="/" className="back-link">
                ← Ürünlere geri dön
            </Link>

            <div className="product-detail-card">
                <img src={product.imageUrl} alt={product.name} />

                <div className="product-detail-info">
                    <h2>{product.name}</h2>
                    <p>{product.description}</p>

                    <h3>{product.price} TL</h3>

                    <p>
                        <strong>Stok:</strong> {product.stock}
                    </p>

                    <p>
                        <strong>Kategori:</strong> {product.categoryName}
                    </p>

                    <button className="primary-button" onClick={handleAddToCart}>
                        Sepete Ekle
                    </button>

                    {successMessage && <p className="success-message">{successMessage}</p>}
                </div>
            </div>
        </section>
    );
}

export default ProductDetail;