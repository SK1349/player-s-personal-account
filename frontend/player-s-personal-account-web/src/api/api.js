import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8084/api', // ✅ Правильно
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 🔥 ВАЖНО: эта строка должна быть в конце!
export default api;