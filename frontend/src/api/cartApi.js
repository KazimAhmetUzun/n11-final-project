import axiosClient from "./axiosClient";

const TEMP_USER_EMAIL = "test@example.com";

export const addToCart = async (product, quantity = 1) => {
    const response = await axiosClient.post("/api/cart", {
        userEmail: TEMP_USER_EMAIL,
        productId: product.id,
        productName: product.name,
        price: product.price,
        quantity,
    });

    return response.data;
};

export const getCart = async () => {
    const response = await axiosClient.get(`/api/cart/${TEMP_USER_EMAIL}`);
    return response.data;
};

export const updateCartItem = async (cartItemId, quantity) => {
    const response = await axiosClient.put(`/api/cart/${cartItemId}`, {
        quantity,
    });

    return response.data;
};

export const removeCartItem = async (cartItemId) => {
    await axiosClient.delete(`/api/cart/${cartItemId}`);
};

export const clearCart = async () => {
    await axiosClient.delete(`/api/cart/clear/${TEMP_USER_EMAIL}`);
};