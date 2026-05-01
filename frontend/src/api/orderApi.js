import axiosClient from "./axiosClient";

const TEMP_USER_EMAIL = "test@example.com";

export const createOrder = async (cartItems) => {
    const items = cartItems.map((item) => ({
        productId: item.productId,
        productName: item.productName,
        price: item.price,
        quantity: item.quantity,
    }));

    const response = await axiosClient.post("/api/orders", {
        userEmail: TEMP_USER_EMAIL,
        items,
    });

    return response.data;
};

export const getOrderById = async (orderId) => {
    const response = await axiosClient.get(`/api/orders/${orderId}`);
    return response.data;
};

export const getOrdersByUserEmail = async () => {
    const response = await axiosClient.get(`/api/orders/user/${TEMP_USER_EMAIL}`);
    return response.data;
};