<template>
  <div class="achievements-container">
    <div class="section">
      <div v-if="unlocked.length" class="achievements-grid">
        <div v-for="ach in unlocked" :key="ach.id" class="achievement-card unlocked">
          <div class="icon-wrapper">
            <img v-if="ach.iconUrl" :src="ach.iconUrl" :alt="ach.name" class="achievement-icon" />
          </div>
          <div class="content">
            <h4 class="title">{{ ach.name }}</h4>
            <p class="description">{{ ach.description }}</p>
            <div class="status">
              <span class="unlocked-badge">Получено {{ formatDate(ach.unlockedAt) }}</span>
            </div>
            <span v-if="showCode" class="code">{{ ach.code }}</span>
          </div>
        </div>
      </div>

      <div v-else class="empty-state">
        <p>Полученных достижений пока нет</p>
      </div>
    </div>

    <div v-if="locked.length" class="section">
      <div class="achievements-grid">
        <div v-for="ach in locked" :key="ach.id" class="achievement-card locked">
          <div class="icon-wrapper">
            <img v-if="ach.iconUrl" :src="ach.iconUrl" :alt="ach.name" class="achievement-icon grayscale" />
          </div>
          <div class="content">
            <h4 class="title">{{ ach.name }}</h4>
            <p class="description">{{ ach.description }}</p>
            <div class="status">
              <span class="locked-badge">Ещё не получено</span>
            </div>
            <span v-if="showCode" class="code">{{ ach.code }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  achievements: {
    type: Array,
    default: () => []
  },
  showCode: {
    type: Boolean,
    default: false
  }
})

const unlocked = computed(() => props.achievements.filter(a => a.unlockedAt))
const locked = computed(() => props.achievements.filter(a => !a.unlockedAt))

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  if (isNaN(date.getTime())) return ''

  return date.toLocaleDateString('ru-RU', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  })
}
</script>

<style scoped>
.achievements-container {
  padding: 8px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #888;
  background: #16213e;
  border-radius: 12px;
  width: 100%;
  box-sizing: border-box;
}

.section {
  margin-bottom: 16px;
}

.section-title {
  font-size: 1.15rem;
  font-weight: 600;
  color: #fff;
  margin: 0 0 16px 0;
  padding-left: 12px;
  border-left: 4px solid #4caf50;
}

.section-title.locked {
  color: #888;
  border-left-color: #555;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, #3a507a, transparent);
}

.divider-text {
  padding: 0 16px;
  white-space: nowrap;
}

.achievements-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.achievement-card {
  background: #16213e;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  gap: 16px;
  transition: all 0.2s ease;
  border: 2px solid transparent;
}

.achievement-card.unlocked {
  border-color: #4caf50;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.15);
}

.achievement-card.locked {
  opacity: 0.75;
  border-color: #3a507a;
  background: #131c2e;
}

.achievement-card:hover {
  transform: translateY(-2px);
  background: #1a2744;
}

.icon-wrapper {
  width: 60px;
  height: 60px;
  background: #0f3460;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  border: 3px solid #e94560;
}

.achievement-icon {
  width: 40px;
  height: 40px;
  object-fit: contain;
}

.achievement-icon.grayscale {
  filter: grayscale(100%) brightness(0.7);
}

.icon-placeholder {
  font-size: 1.8rem;
}

.content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 8px;
}

.title {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 600;
  color: #fff;
}

.description {
  margin: 0;
  color: #aaa;
  font-size: 0.95rem;
  line-height: 1.4;
}

.status {
  margin-top: 4px;
}

.unlocked-badge {
  color: #4caf50;
  font-size: 0.85rem;
  font-weight: 500;
}

.locked-badge {
  color: #666;
  font-size: 0.85rem;
  font-style: italic;
}

.code {
  font-family: monospace;
  font-size: 0.75rem;
  color: #555;
  margin-top: 4px;
}
</style>