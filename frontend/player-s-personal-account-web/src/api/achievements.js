import api from './api'

export const achievementsApi = {
  getAllAchievements() {
    return api.get('/achievements')
  },

  getPlayerAchievements(userId) {
    return api.get(`/users/${userId}/achievements`)
  },

  getAchievementByCode(code) {
    return api.get(`/achievements/code/${code}`)
  },

  grantAchievement(userId, achievementCode) {
    return api.post(`/users/${userId}/achievements/grant/${achievementCode}`)
  }
}