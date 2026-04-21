<template>
  <div class="profile-card">
    <div class="header">
      <img :src="user.avatarUrl || 'https://via.placeholder.com/120'" class="avatar" />
      <div class="info">
        <h2>{{ user.nickname }}</h2>
        <p class="email">{{ user.email }}</p>
        <div class="stats-row">
          <span class="badge">🏆 Рейтинг: {{ user.rating || 1000 }}</span>
          <span class="badge">⭐ Уровень: {{ user.level || 1 }}</span>
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
defineProps({
  user: { type: Object, default: () => ({}) }
})
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
}

.badge {
    background: #0f3460;
    padding: 6px 12px;
    border-radius: 20px;
    font-size: 0.9rem;
}

.details {
    padding-top: 20px;
    color: #ccc;
}

.details p {
    margin: 8px 0;
}
</style>