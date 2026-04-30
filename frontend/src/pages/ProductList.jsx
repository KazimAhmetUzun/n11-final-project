import { useEffect, useState } from "react";
import { getProducts } from "../api/productApi";

function ProductList() {
    const [products, setProducts] = useState([]);
    const [pageInfo, setPageInfo] = useState({
        page: 0,
        size: 10,
        totalPages: 0,
        last: false,
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const fetchProducts = async (page = 0) => {
        try {
            setLoading(true);
            setError("");

            const data = await getProducts(page, 10);

            setProducts(data.content || []);
            setPageInfo({
                page: data.page,
                size: data.size,
                totalPages: data.totalPages,
                last: data.last,
            });
        } catch (err) {
            console.error(err);
            setError("Ürünler yüklenirken bir hata oluştu.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchProducts(0);
    }, []);

    const goPrevious = () => {
        if (pageInfo.page > 0) {
            fetchProducts(pageInfo.page - 1);
        }
    };

    const goNext = () => {
        if (!pageInfo.last) {
            fetchProducts(pageInfo.page + 1);
        }
    };

    if (loading) {
        return <p>Ürünler yükleniyor...</p>;
    }

    if (error) {
        return <p className="error-message">{error}</p>;
    }

    return (
        <section>
            <h2>Ürünler</h2>

            <div className="product-grid">
                {products.map((product) => (
                    <div className="product-card" key={product.id}>
                        <img src={product.imageUrl} alt={product.name} />

                        <h3>{product.name}</h3>
                        <p>{product.description}</p>

                        <strong>{product.price} TL</strong>

                        <p>Stok: {product.stock}</p>
                        <p>Kategori: {product.categoryName}</p>
                    </div>
                ))}
            </div>

            <div className="pagination">
                <button onClick={goPrevious} disabled={pageInfo.page === 0}>
                    Önceki
                </button>

                <span>
          Sayfa {pageInfo.page + 1} / {pageInfo.totalPages}
        </span>

                <button onClick={goNext} disabled={pageInfo.last}>
                    Sonraki
                </button>
            </div>
        </section>
    );
}

export default ProductList;