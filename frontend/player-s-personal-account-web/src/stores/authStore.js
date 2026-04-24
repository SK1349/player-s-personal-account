import { defineStore } from 'pinia'
import api from '../api/api'

const cleanToken = (token) => {
  return token?.replace(/^["']|["']$/g, '')?.replace(/\s+/g, '')?.trim() || null;
};

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: cleanToken(localStorage.getItem('token')),
    user: JSON.parse(localStorage.getItem('user') || '{}'),
    loading: false,
    error: null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    userId: (state) => state.user?.id || null
  },

  actions: {
    async register(formData) {
      this.loading = true
      this.error = null

      try {
        await api.post('/auth/register', {
          nickname: formData.nickname,
          email: formData.email,
          password: formData.password,
          gender: formData.gender || null
        })

        const loginRes = await api.post('/auth/login', {
          email: formData.email,
          password: formData.password
        })

        const token = cleanToken(loginRes.data.token)
        const userId = loginRes.data.userId || loginRes.data.id
        const userData = {
          id: userId,
          nickname: loginRes.data.nickname,
          email: loginRes.data.email,
          avatarUrl: loginRes.data.avatarUrl || null,
        }

        localStorage.setItem('token', token)
        localStorage.setItem('user', JSON.stringify(userData))

        this.token = token
        this.user = userData

        return true

      } catch (e) {
        this.error = e.response?.data?.message || e.response?.data?.error || 'Ошибка при регистрации или входе'
        return false
      } finally {
        this.loading = false
      }
    },

    async login(email, password) {
      this.loading = true
      this.error = null
      try {
        const response = await api.post('/auth/login', { email, password })

        const token = cleanToken(response.data.token)
        const userId = response.data.userId || response.data.id
        const user = {
          id: userId,
          nickname: response.data.nickname,
          email: response.data.email,
          avatarUrl: response.data.avatarUrl || null
        }

        if (token) {
          this.token = token
          localStorage.setItem('token', token)
        }
        this.user = user
        localStorage.setItem('user', JSON.stringify(user))
        return true
      } catch (e) {
        this.error = e.response?.data?.message || 'Неверный логин или пароль'
        return false
      } finally {
        this.loading = false
      }
    },

    logout() {
      this.token = null
      this.user = {}
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    },

    async fetchProfile() {
      if (!this.userId) {
        return
      }
      try {
        const res = await api.get(`/users/${this.userId}`)
        this.user = res.data
        localStorage.setItem('user', JSON.stringify(res.data))
      } catch (e) {
        throw e
      }
    }
  },

  persist: true
})