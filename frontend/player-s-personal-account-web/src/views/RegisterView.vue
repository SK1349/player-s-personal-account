<template>
  <div class="auth-wrapper">
    <div class="auth-card">
      <h2>Регистрация</h2>
      <form @submit.prevent="handleRegister">
        <input v-model="form.nickname" placeholder="Никнейм *" required minlength="3" maxlength="50" />
        <input v-model="form.email" type="email" placeholder="Email *" required />
        <input v-model="form.password" type="password" placeholder="Пароль *" required minlength="6" />

        <input v-model="form.fullName" placeholder="Полное имя" maxlength="100" />
        <input v-model="form.birthDate" type="date" />

        <select v-model="form.gender" required>
          <option value="" disabled>Пол *</option>
          <option value="M">Мужской</option>
          <option value="F">Женский</option>
          <option value="U">Не указан</option>
        </select>

        <input v-model="form.country" placeholder="Страна" maxlength="100" />
        <input v-model="form.city" placeholder="Город" maxlength="100" />
        <input v-model="form.phone" placeholder="Телефон" maxlength="20" />
        <textarea v-model="form.bio" placeholder="О себе" maxlength="500" rows="3"></textarea>

        <p v-if="authStore.error" class="error">{{ authStore.error }}</p>

        <button type="submit" :disabled="authStore.loading">
          {{ authStore.loading ? 'Регистрация...' : 'Создать аккаунт' }}
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
  fullName: '',
  birthDate: '',
  gender: '',
  country: '',
  city: '',
  phone: '',
  bio: ''
})

const handleRegister = async () => {
  const success = await authStore.register(form)
  if (success) {
    alert('Регистрация успешна! Теперь войдите в аккаунт.')
    router.push('/login')
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
  color: white;
}
input, select, textarea {
  width: 100%;
  padding: 12px;
  margin: 8px 0;
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