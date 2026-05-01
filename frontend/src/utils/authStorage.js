const TOKEN_KEY = "token";
const USER_EMAIL_KEY = "userEmail";

export const saveAuth = (token, userEmail) => {
    localStorage.setItem(TOKEN_KEY, token);
    localStorage.setItem(USER_EMAIL_KEY, userEmail);
};

export const getToken = () => {
    return localStorage.getItem(TOKEN_KEY);
};

export const getUserEmail = () => {
    return localStorage.getItem(USER_EMAIL_KEY);
};

export const isLoggedIn = () => {
    return Boolean(getToken());
};

export const clearAuth = () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_EMAIL_KEY);
};