import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8084/api',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const achievementsApi = {
  getPlayerAchievements(userId) {
    return api.get(`/users/${userId}/achievements`);
  },

  getAchievement(achievementId) {
    return api.get(`/achievements/${achievementId}`);
  }
};

export default api;