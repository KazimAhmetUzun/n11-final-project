import axiosClient from "./axiosClient";

export const register = async (registerData) => {
    const response = await axiosClient.post("/api/auth/register", registerData);
    return response.data;
};

export const verifyEmail = async (verifyData) => {
    const response = await axiosClient.post("/api/auth/verify-email", verifyData);
    return response.data;
};

export const login = async (loginData) => {
    const response = await axiosClient.post("/api/auth/login", loginData);
    return response.data;
};