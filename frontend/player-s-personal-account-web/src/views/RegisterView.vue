<template>
  <div class="auth-wrapper">
    <div class="auth-card">
      <h2>Регистрация</h2>
      <form @submit.prevent="handleRegister">
        <input v-model="form.nickname" placeholder="Никнейм *" required minlength="3" maxlength="50" />
        <input v-model="form.email" type="email" placeholder="Email *" required />
        <input v-model="form.password" type="password" placeholder="Пароль *" required minlength="6" />

        <select v-model="form.gender" required>
          <option value="" disabled>Пол *</option>
          <option value="M">Мужской</option>
          <option value="F">Женский</option>
        </select>

        <p v-if="authStore.error" class="error">{{ authStore.error }}</p>

        <button type="submit" :disabled="authStore.loading">
          {{ authStore.loading ? 'Создание аккаунта...' : 'Создать аккаунт' }}
        </button>
      </form>
      <router-link to="/login" class="link">Уже есть аккаунт? Войти</router-link>
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
  nickname: '',
  email: '',
  password: '',
  gender: ''
})

const handleRegister = async () => {
  authStore.error = null

  const success = await authStore.register(form)
  if (success) {
    router.push('/profile?tab=settings')
  }
}
</script>

<style scoped>
.auth-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #1a1a2e;
  padding: 20px;
}
.auth-card {
  background: #16213e;
  padding: 2rem;
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  text-align: center;
  color: #fff;
}
input, select, textarea {
  width: 100%;
  padding: 12px;
  margin: 8px 0;
  background: #0f3460;
  border: 1px solid #3a507a;
  border-radius: 6px;
  color: #fff;
  box-sizing: border-box;
}
button {
  width: 100%;
  padding: 12px;
  margin-top: 15px;
  background: #ff4d4d;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
}
button:disabled {
  background: #ff4d4d;
  cursor: not-allowed;
}
.error {
  color: #ff4d4d;
  font-size: 0.9rem;
  margin: 10px 0;
  background: rgba(255, 107, 107, 0.1);
  padding: 8px;
  border-radius: 6px;
}
.link {
  display: block;
  margin-top: 20px;
  color: #ff4d4d;
  text-decoration: none;
}
.link:hover {
  text-decoration: underline;
}
</style>