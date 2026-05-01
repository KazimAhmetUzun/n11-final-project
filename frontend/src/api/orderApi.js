import axiosClient from "./axiosClient";
import { getUserEmail } from "../utils/authStorage";

const getRequiredUserEmail = () => {
    const userEmail = getUserEmail();

    if (!userEmail) {
        throw new Error("User is not logged in.");
    }

    return userEmail;
};

export const createOrder = async (cartItems) => {
    const userEmail = getRequiredUserEmail();

    const items = cartItems.map((item) => ({
        productId: item.productId,
        productName: item.productName,
        price: item.price,
        quantity: item.quantity,
    }));

    const response = await axiosClient.post("/api/orders", {
        userEmail,
        items,
    });

    return response.data;
};

export const getOrderById = async (orderId) => {
    const response = await axiosClient.get(`/api/orders/${orderId}`);
    return response.data;
};

export const getOrdersByUserEmail = async () => {
    const userEmail = getRequiredUserEmail();

    const response = await axiosClient.get(`/api/orders/user/${userEmail}`);
    return response.data;
};