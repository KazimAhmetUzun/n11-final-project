import axiosClient from "./axiosClient";

const TEMP_USER_EMAIL = "test@example.com";

export const createPayment = async (paymentData) => {
    const response = await axiosClient.post("/api/payments", {
        userEmail: TEMP_USER_EMAIL,
        ...paymentData,
    });

    return response.data;
};