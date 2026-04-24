import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8084/api',
  headers: { 'Content-Type': 'application/json' },
});

const cleanToken = (token) => {
  return token?.replace(/^["']|["']$/g, '')?.replace(/\s+/g, '')?.trim() || null;
};

api.interceptors.request.use((config) => {
  const rawToken = localStorage.getItem('token');
  const token = cleanToken(rawToken);

  const isPublicPath = config.url && config.url.includes('/auth/');

  if (token && !isPublicPath) {
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
  },
  getAllAchievements() {
    return api.get('/achievements');
  }
};

export default api;