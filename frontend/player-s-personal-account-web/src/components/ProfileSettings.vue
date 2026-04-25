<template>
  <form @submit.prevent="handleSubmit" class="settings-form">
    <div class="form-section">
      <h3>Основная информация</h3>

      <div class="form-group">
        <label>Никнейм</label>
        <input v-model="form.nickname" :placeholder="user.nickname" />
      </div>

      <div class="form-group">
        <label>Сменить аватар</label>
        <input
          type="file"
          @change="onAvatarChange"
          accept="image/*"
          class="file-input"
        />
        <div v-if="previewAvatar || user.avatarUrl" class="avatar-preview">
          <img :src="previewAvatar || getFullImageUrl(user.avatarUrl)" alt="Avatar" />
        </div>
    </div>
    </div>

    <div class="form-section">
      <h3>Контакты</h3>

      <div class="form-group">
          <label>Email</label>
          <input :value="user.email" disabled class="disabled" />
      </div>

      <div class="form-group">
        <label>Страна</label>
        <input v-model="form.country" :placeholder="user.country" />
      </div>

      <div class="form-group">
        <label>Город</label>
        <input v-model="form.city" :placeholder="user.city" />
      </div>

      <div class="form-group">
        <label>Телефон</label>
        <input v-model="form.phone" :placeholder="user.phone" type="tel" />
      </div>
    </div>

    <div class="form-section">
      <h3>Личное</h3>

      <div class="form-group">
        <label>Полное имя</label>
        <input v-model="form.fullName" :placeholder="user.fullName" />
      </div>

      <div class="form-group">
        <label>Биография</label>
        <textarea v-model="form.bio" :placeholder="user.bio" rows="4" />
      </div>

      <div class="form-group">
        <label>Дата рождения</label>
        <input v-model="form.birthDate" type="date" :max="maxDate" />
      </div>

      <div class="form-group">
        <label>Пол</label>
        <select v-model="form.gender">
          <option value="M">Мужской</option>
          <option value="F">Женский</option>
        </select>
      </div>
    </div>

    <details class="password-section">
      <summary>Сменить пароль</summary>

      <div class="form-group">
        <label>Старый пароль</label>
        <input v-model="form.oldPassword" type="password" />
      </div>
      <div class="form-group">
        <label>Новый пароль</label>
        <input v-model="form.newPassword" type="password" />
      </div>
      <div class="form-group">
        <label>Подтвердите новый пароль</label>
        <input v-model="form.confirmNewPassword" type="password" />
      </div>
    </details>

    <p v-if="error" class="error">{{ error }}</p>
    <p v-if="success" class="success">Профиль обновлён!</p>

    <button type="submit" :disabled="loading">
      {{ loading ? 'Сохранение...' : 'Сохранить изменения' }}
    </button>
  </form>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useAuthStore } from '../stores/authStore'
import api from '../api/api'

const props = defineProps({
  user: { type: Object, default: () => ({}) }
})
const emit = defineEmits(['saved'])

const authStore = useAuthStore()
const form = ref({})
const loading = ref(false)
const error = ref(null)
const success = ref(false)
const avatarFile = ref(null)
const previewAvatar = ref(null)

const API_BASE = 'http://localhost:8084'

const maxDate = computed(() => {
  const today = new Date()
  return today.toISOString().split('T')[0]
})

const getFullImageUrl = (url) => {
  if (!url) return null
  if (url.startsWith('http')) return url
  return `${API_BASE}${url}`
}

watch(() => props.user, (newUser) => {
  form.value = {
    nickname: newUser.nickname || '',
    fullName: newUser.fullName || '',
    country: newUser.country || '',
    city: newUser.city || '',
    phone: newUser.phone || '',
    birthDate: newUser.birthDate ? newUser.birthDate.split('T')[0] : '',
    gender: newUser.gender || '',
    bio: newUser.bio || '',
    oldPassword: '',
    newPassword: '',
    confirmNewPassword: ''
  }
  previewAvatar.value = null
}, { immediate: true, deep: true })

const onAvatarChange = (event) => {
  const file = event.target.files[0]
  if (file) {
    avatarFile.value = file
    const reader = new FileReader()
    reader.onload = (e) => {
      previewAvatar.value = e.target.result
    }
    reader.readAsDataURL(file)
  }
}

const handleSubmit = async () => {
  loading.value = true
  error.value = null
  success.value = false

  try {
    const formData = new FormData()
    formData.append('nickname', form.value.nickname ?? '')
    formData.append('fullName', form.value.fullName ?? '')
    formData.append('country', form.value.country ?? '')
    formData.append('city', form.value.city ?? '')
    formData.append('phone', form.value.phone ?? '')
    formData.append('bio', form.value.bio ?? '')

    if (form.value.birthDate) {
      formData.append('birthDate', form.value.birthDate)
    }

    if (form.value.gender) {
      formData.append('gender', form.value.gender)
    }

    if (avatarFile.value) {
      formData.append('avatarFile', avatarFile.value)
    }

    if (form.value.oldPassword || form.value.newPassword || form.value.confirmNewPassword) {
      formData.append('oldPassword', form.value.oldPassword || '')
      formData.append('newPassword', form.value.newPassword || '')
      formData.append('confirmNewPassword', form.value.confirmNewPassword || '')
    }

    const response = await api.put(`/users/${authStore.userId}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    authStore.user = response.data
    localStorage.setItem('user', JSON.stringify(response.data))

    emit('saved', response.data)

    success.value = true
    avatarFile.value = null
    previewAvatar.value = null
    form.value.oldPassword = ''
    form.value.newPassword = ''
    form.value.confirmNewPassword = ''

    setTimeout(() => success.value = false, 3000)
  } catch (e) {
    console.error('Ошибка обновления:', e)
    const errors = e.response?.data
    if (errors && typeof errors === 'object' && !errors.error) {
      error.value = Object.values(errors).join(', ')
    } else {
      error.value = errors?.error || errors?.message || 'Ошибка сохранения'
    }
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
  max-width: 600px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-section {
  padding-bottom: 20px;
  border-bottom: 1px solid #0f3460;
}
.form-section:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.form-section h3 {
  margin: 0 0 16px 0;
  color: #e94560;
  font-size: 1.1rem;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  color: #ccc;
  font-size: 0.9rem;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  background: #0f3460;
  border: 1px solid #3a507a;
  border-radius: 8px;
  color: #fff;
  font-size: 1rem;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #e94560;
}

.form-group input.disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
  font-family: inherit;
}

.form-group select {
  cursor: pointer;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%23ccc' d='M6 8L1 3h10z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 12px center;
  padding-right: 30px;
}

.file-input {
  padding: 8px;
  background: #0f3460;
  border: 1px dashed #3a507a;
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
}
.file-input:hover {
  border-color: #e94560;
}

.avatar-preview {
  margin-top: 12px;
  text-align: center;
}
.avatar-preview img {
  max-width: 150px;
  max-height: 150px;
  object-fit: cover;
}

.password-section {
  background: #0f3460;
  border-radius: 8px;
  padding: 16px;
}
.password-section summary {
  cursor: pointer;
  color: #fff;
  font-weight: 500;
  margin-bottom: 12px;
  list-style: none;
}
.password-section summary::-webkit-details-marker {
  display: none;
}
.password-section summary::after {
  content: ' ▼';
  font-size: 0.8rem;
}
.password-section[open] summary::after {
  content: ' ▲';
}

.error {
  color: #ff6b6b;
  margin: 8px 0;
  font-size: 0.9rem;
  background: rgba(255, 107, 107, 0.1);
  padding: 8px 12px;
  border-radius: 6px;
}
.success {
  color: #4caf50;
  margin: 8px 0;
  font-size: 0.9rem;
  background: rgba(76, 175, 80, 0.1);
  padding: 8px 12px;
  border-radius: 6px;
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
button:hover:not(:disabled) {
  background: #d63850;
}
button:disabled {
  background: #8a3a4a;
  cursor: not-allowed;
  opacity: 0.7;
}

@media (max-width: 600px) {
  .settings-form {
    padding: 16px;
  }
  .form-section h3 {
    font-size: 1rem;
  }
}
</style>