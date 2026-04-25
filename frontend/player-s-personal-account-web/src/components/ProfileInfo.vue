<template>
  <div class="profile-card">
    <div class="header">
      <img
        :src="getFullAvatarUrl(user.avatarUrl)"
        :key="user.avatarUrl"
        :alt="user.nickname"
        class="avatar"
        @error="handleImageError"
      />
      <div class="info">
        <h2>{{ user.nickname }}</h2>
        <p class="email">{{ user.email }}</p>
        <div class="stats-row">
          <span class="badge">🏆 Рейтинг: {{ user.rating || 1000 }}</span>
          <span class="badge">⭐ Уровень: {{ user.level || 1 }}</span>
        </div>

        <div class="xp-progress" v-if="user.experience !== undefined">
          <div class="xp-info">
            <span>✨ Опыт: {{ user.experience || 0 }} / {{ xpForNextLevel }}</span>
            <span>{{ xpProgress }}%</span>
          </div>
          <div class="xp-track">
            <div class="xp-fill" :style="{ width: `${xpProgress}%` }"></div>
          </div>
        </div>

      </div>
    </div>

    <div class="details" v-if="user.fullName || user.city || user.bio">
      <p v-if="user.fullName"><strong>Имя:</strong> {{ user.fullName }}</p>
      <p v-if="user.city"><strong>Город:</strong> {{ user.city }}</p>
      <p v-if="user.country"><strong>Страна:</strong> {{ user.country }}</p>
      <p v-if="user.bio"><strong>О себе:</strong> {{ user.bio }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import userIcon from '../assets/user.png'

const API_BASE = 'http://localhost:8084'

const props = defineProps({
  user: { type: Object, default: () => ({}) }
})

const xpForCurrentLevel = computed(() => {
  const level = props.user.level || 1
  return Math.pow(level - 1, 2) * 50
})

const xpForNextLevel = computed(() => {
  const level = props.user.level || 1
  return Math.pow(level, 2) * 50
})

const xpProgress = computed(() => {
  const current = props.user.experience || 0
  const prev = xpForCurrentLevel.value
  const next = xpForNextLevel.value
  if (next <= prev) return 100
  const progress = ((current - prev) / (next - prev)) * 100
  return Math.min(100, Math.max(0, Math.round(progress)))
})

const getFullAvatarUrl = (avatarUrl) => {
  if (!avatarUrl) return userIcon
  if (avatarUrl.startsWith('http')) return avatarUrl
  return `${API_BASE}${avatarUrl}`
}

const handleImageError = (e) => {
  if (e.target.src.includes('user')) {
    return
  }
  e.target.src = userIcon
}
</script>

<style scoped>
.profile-card {
    background: #16213e;
    border-radius: 12px;
    padding: 24px;
    max-width: 600px;
}

.header {
    display: flex;
    gap: 20px;
    align-items: center;
    padding-bottom: 20px;
    border-bottom: 1px solid #0f3460;
}

.avatar {
    width: 120px;
    height: 120px;
    border-radius: 50%;
    object-fit: cover;
    border: 3px solid #e94560;
}

.info h2 {
    margin: 0 0 8px 0;
    font-size: 1.5rem;
}

.email {
    color: #888;
    margin: 0 0 12px 0;
}

.stats-row {
    display: flex;
    gap: 12px;
    margin-bottom: 12px;
}

.badge {
    background: #0f3460;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.9rem;
}

.xp-progress {
    margin-top: 8px;
}

.xp-info {
    display: flex;
    justify-content: space-between;
    color: #888;
    font-size: 0.85rem;
    margin-bottom: 6px;
}

.xp-track {
    height: 6px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 3px;
    overflow: hidden;
}

.xp-fill {
    height: 100%;
    background: linear-gradient(90deg, #4caf50, #8bc34a);
    border-radius: 3px;
    transition: width 0.3s ease;
}

.details {
    padding-top: 20px;
    color: #ccc;
}

.details p {
    margin: 8px 0;
}
</style>