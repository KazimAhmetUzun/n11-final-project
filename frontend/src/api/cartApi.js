import axiosClient from "./axiosClient";
import { getUserEmail } from "../utils/authStorage";

const getRequiredUserEmail = () => {
    const userEmail = getUserEmail();

    if (!userEmail) {
        throw new Error("User is not logged in.");
    }

    return userEmail;
};

export const addToCart = async (product, quantity = 1) => {
    const userEmail = getRequiredUserEmail();

    const response = await axiosClient.post("/api/cart", {
        userEmail,
        productId: product.id,
        productName: product.name,
        price: product.price,
        quantity,
    });

    return response.data;
};

export const getCart = async () => {
    const userEmail = getRequiredUserEmail();

    const response = await axiosClient.get(`/api/cart/${userEmail}`);
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
    const userEmail = getRequiredUserEmail();

    await axiosClient.delete(`/api/cart/clear/${userEmail}`);
};