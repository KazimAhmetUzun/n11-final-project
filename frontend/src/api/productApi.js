import axiosClient from "./axiosClient";

export const getProducts = async (page = 0, size = 10) => {
    const response = await axiosClient.get(`/api/products?page=${page}&size=${size}`);
    return response.data;
};

export const getProductById = async (id) => {
    const response = await axiosClient.get(`/api/products/${id}`);
    return response.data;
};