import axios from 'axios';

const BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8088';

const api = axios.create({
  baseURL: BASE_URL
});

// Attach JWT automatically to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('bibliotech_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// On 401, clear auth state and force re-login
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('bibliotech_token');
      localStorage.removeItem('bibliotech_user');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
