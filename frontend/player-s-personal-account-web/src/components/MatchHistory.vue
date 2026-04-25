<template>
  <div class="matches-list">
    <div v-if="matches.length === 0" class="empty-state">
      <p>Матчей пока нет</p>
    </div>

    <div
      v-for="match in matches"
      :key="match.id"
      class="match-item"
      :class="match.result"
    >
      <div class="match-info">
        <img
          :src="getOpponentAvatar(match.opponentAvatar)"
          :alt="match.opponent"
          class="opponent-avatar"
          @error="handleAvatarError"
        />
        <div class="opponent-text">
          <span class="opponent">vs {{ match.opponent }}</span>
          <span class="map">{{ match.map }}</span>
        </div>
      </div>

      <div class="match-score">
        <span class="score-value">{{ match.score }}</span>
      </div>

      <div class="match-meta">
        <span class="result-badge">
          {{ getResultText(match.result) }}
        </span>
        <span class="match-date">{{ match.date }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import defaultAvatar from '../assets/user.png'

const props = defineProps({
  matches: {
    type: Array,
    default: () => []
  }
})

const getResultText = (result) => {
  const texts = {
    'win': 'Победа',
    'loss': 'Поражение',
    'draw': 'Ничья'
  }
  return texts[result] || 'Поражение'
}

const getOpponentAvatar = (avatarUrl) => {
  if (!avatarUrl) return defaultAvatar
  if (avatarUrl.startsWith('http')) return avatarUrl
  return `http://localhost:8084${avatarUrl}`
}

const handleAvatarError = (e) => {
  e.target.src = defaultAvatar
}
</script>

<style scoped>
.matches-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.empty-state {
    text-align: center;
    padding: 40px;
    color: #888;
    background: #16213e;
    border-radius: 10px;
}

.match-item {
    background: #16213e;
    border-radius: 10px;
    padding: 16px 20px;
    display: grid;
    grid-template-columns: 1fr auto 1fr;
    align-items: center;
    gap: 20px;
    border-left: 4px solid transparent;
    transition: transform 0.2s, box-shadow 0.2s;
}

.match-item:hover {
    transform: translateX(4px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.match-item.win {
    border-left-color: #4caf50;
}

.match-item.loss {
    border-left-color: #f44336;
}

.match-item.draw {
    border-left-color: #ff9800;
}

.match-info {
    display: flex;
    align-items: center;
    gap: 12px;
}

.opponent-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
    border: 2px solid #3a507a;
    flex-shrink: 0;
}

.opponent-text {
    display: flex;
    flex-direction: column;
    gap: 4px;
    min-width: 0;
}

.opponent {
    font-weight: 600;
    font-size: 1.1rem;
    color: #fff;
}

.map {
    color: #888;
    font-size: 0.85rem;
    font-family: 'Courier New', monospace;
}

.match-score {
    text-align: center;
}

.score-value {
    font-size: 1.5rem;
    font-weight: bold;
    color: #fff;
    font-family: 'Courier New', monospace;
}

.match-meta {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 6px;
}

.result-badge {
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 0.85rem;
    font-weight: 500;
    white-space: nowrap;
}

.win .result-badge {
    background: rgba(76, 175, 80, 0.2);
    color: #4caf50;
}

.loss .result-badge {
    background: rgba(244, 67, 54, 0.2);
    color: #f44336;
}

.draw .result-badge {
    background: rgba(255, 152, 0, 0.2);
    color: #ff9800;
}

.match-date {
    color: #666;
    font-size: 0.85rem;
}
</style>