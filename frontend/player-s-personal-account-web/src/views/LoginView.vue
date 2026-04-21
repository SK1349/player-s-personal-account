<template>
  <div class="auth-wrapper">
    <div class="auth-card">
      <h2>Вход в аккаунт</h2>
      <form @submit.prevent="handleLogin">
        <input
          v-model="form.email"
          type="email"
          placeholder="Email"
          required
        />
        <input
          v-model="form.password"
          type="password"
          placeholder="Пароль"
          required
        />

        <p v-if="authStore.error" class="error">{{ authStore.error }}</p>

        <button type="submit" :disabled="authStore.loading">
          {{ authStore.loading ? 'Вход...' : 'Войти' }}
        </button>
      </form>
      <router-link to="/register" class="link">Нет аккаунта? Зарегистрироваться</router-link>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  email: '',
  password: ''
})

const handleLogin = async () => {
  const success = await authStore.login(form.email, form.password)
  if (success) {
    router.push('/profile')
  }
}
</script>

<style scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: #1a1a2e;
}
.auth-card {
  background: #16213e;
  padding: 2rem;
  border-radius: 12px;
  width: 100%;
  max-width: 400px;
  text-align: center;
  color: white;
}
input {
  width: 100%;
  padding: 12px;
  margin: 10px 0;
  background: #0f3460;
  border: 1px solid #3a507a;
  border-radius: 6px;
  color: white;
  box-sizing: border-box;
}
button {
  width: 100%;
  padding: 12px;
  margin-top: 15px;
  background: #e94560;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
}
button:disabled {
  background: #8a3a4a;
  cursor: not-allowed;
}
.error {
  color: #ff6b6b;
  font-size: 0.9rem;
  margin: 10px 0;
}
.link {
  display: block;
  margin-top: 20px;
  color: #e94560;
  text-decoration: none;
}
.link:hover {
  text-decoration: underline;
}
</style>