import { defineStore } from 'pinia'
import api from '../api/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: JSON.parse(localStorage.getItem('user') || '{}'),
    loading: false,
    error: null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    userId: (state) => state.user?.id
  },


  actions: {
    async register(formData) {
      this.loading = true
      this.error = null
      try {
        const response = await api.post('/users', formData)

        this.user = response.data
        localStorage.setItem('user', JSON.stringify(response.data))
        return true
      } catch (e) {
        this.error = e.response?.data?.message || 'Ошибка регистрации'
        return false
      } finally {
          this.loading = false
      }
    },

    async login(email, password) {
      this.loading = true
      this.error = null
      try {
        const response = await api.post('/users/login', { email, password })

        const token = response.data.token || response.data.accessToken
        const user = response.data.user || response.data

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
       if (!this.userId) return
       try {
         const res = await api.get(`/profile/${this.userId}`)
         this.user = res.data
         localStorage.setItem('user', JSON.stringify(res.data))
       } catch (e) {
         console.error("Ошибка загрузки профиля", e)
       }
    }
  },

  persist: true
})