<template>
  <form @submit.prevent="handleSubmit" class="settings-form">
    <div class="form-group">
      <label>Никнейм</label>
      <input v-model="form.nickname" :placeholder="user.nickname" />
    </div>

    <div class="form-group">
      <label>Полное имя</label>
      <input v-model="form.fullName" :placeholder="user.fullName" />
    </div>

    <div class="form-group">
      <label>Email (не изменяется)</label>
      <input :value="user.email" disabled class="disabled" />
    </div>

    <div class="form-group">
      <label>Город</label>
      <input v-model="form.city" :placeholder="user.city" />
    </div>

    <div class="form-group">
      <label>О себе</label>
      <textarea v-model="form.bio" :placeholder="user.bio" rows="3" />
    </div>

    <div class="form-group">
      <label>Новый пароль (оставьте пустым, чтобы не менять)</label>
      <input v-model="form.newPassword" type="password" placeholder="••••••" />
    </div>

    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="success" class="success">✅ Профиль обновлён!</p>

    <button type="submit" :disabled="loading">
      {{ loading ? 'Сохранение...' : 'Сохранить изменения' }}
    </button>
  </form>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useAuthStore } from '../stores/authStore'
import api from '../api/api'

const props = defineProps({ user: { type: Object, default: () => ({}) } })
const emit = defineEmits(['saved'])

const authStore = useAuthStore()
const form = ref({})
const loading = ref(false)
const error = ref(null)
const success = ref(false)

watch(() => props.user, (newUser) => {
  form.value = { ...newUser, newPassword: '' }
}, { immediate: true })

const handleSubmit = async () => {
  loading.value = true
  error.value = null
  success.value = false

  try {
    const payload = { ...form.value }
    if (!payload.newPassword) delete payload.newPassword

    const response = await api.put(`/users/${authStore.userId}`, payload)

    emit('saved', response.data)
    success.value = true

    setTimeout(() => success.value = false, 3000)
  } catch (e) {
    error.value = e.response?.data?.error || 'Ошибка сохранения'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.settings-form {
  background: #16213e;
  border-radius: 12px;
  padding: 24px;
  max-width: 500px;
}
.form-group { margin-bottom: 20px; }
.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #ccc;
  font-size: 0.95rem;
}
.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px;
  background: #0f3460;
  border: 1px solid #3a507a;
  border-radius: 8px;
  color: #fff;
  font-size: 1rem;
}
.form-group input.disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.form-group textarea {
    resize: vertical;
    min-height: 80px;
}

.error {
    color: #ff6b6b;
    margin: 12px 0;
    font-size: 0.9rem;
}

.success {
    color: #4caf50;
    margin: 12px 0;
    font-size: 0.9rem;
}

button {
    width: 100%;
    padding: 14px;
    background: #e94560;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    transition: background 0.2s;
}

button:hover {
    background: #d63850;
}

button:disabled {
    background: #8a3a4a;
    cursor: not-allowed;
}
</style>