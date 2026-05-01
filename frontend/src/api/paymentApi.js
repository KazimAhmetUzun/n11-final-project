import axiosClient from "./axiosClient";
import { getUserEmail } from "../utils/authStorage";

const getRequiredUserEmail = () => {
    const userEmail = getUserEmail();

    if (!userEmail) {
        throw new Error("User is not logged in.");
    }

    return userEmail;
};

export const createPayment = async (paymentData) => {
    const userEmail = getRequiredUserEmail();

    const response = await axiosClient.post("/api/payments", {
        userEmail,
        ...paymentData,
    });

    return response.data;
};