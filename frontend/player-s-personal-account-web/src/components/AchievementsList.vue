<template>
  <div class="achievements-grid">
    <div
      v-for="ach in achievements"
      :key="ach.id"
      class="achievement-card"
      :class="{ locked: !ach.unlockedAt }"
    >
      <div class="icon-wrapper">
        <span class="icon">{{ ach.unlockedAt ? '🏅' : '🔒' }}</span>
      </div>
      <div class="content">
        <h4>{{ ach.name }}</h4>
        <p class="desc">{{ ach.description }}</p>
        <span v-if="ach.unlockedAt" class="date">
          Получено: {{ new Date(ach.unlockedAt).toLocaleDateString('ru-RU') }}
        </span>
        <span v-else class="locked-label">Ещё не получено</span>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  achievements: { type: Array, default: () => [] }
})
</script>

<style scoped>
.achievements-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}
.achievement-card {
  background: #16213e;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  gap: 16px;
  transition: transform 0.2s;
}
.achievement-card:hover {
    transform: translateY(-2px);
}

.achievement-card.locked {
    opacity: 0.7;
}
.icon-wrapper {
  width: 50px;
  height: 50px;
  background: #0f3460;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  flex-shrink: 0;
}
.achievement-card.locked .icon-wrapper { background: #2a2a4a; }

.content {
    flex: 1;
}
.content h4 {
    margin: 0 0 8px 0;
    font-size: 1.1rem;
}

.desc {
    color: #aaa;
    font-size: 0.95rem;
    margin: 0 0 12px 0;
    line-height: 1.4;
}

.date {
    color: #4caf50;
    font-size: 0.85rem;
}

.locked-label {
    color: #666;
    font-size: 0.85rem;
    font-style: italic;
}
</style>