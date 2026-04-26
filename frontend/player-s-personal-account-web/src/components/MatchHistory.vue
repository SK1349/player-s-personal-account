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
      <div class="match-map-info">
        <span class="map-name">{{ match.map }}</span>
      </div>

      <div class="match-faceoff">
        <div class="player-card">
          <img
            :src="getAvatarUrl(match.playerAvatar)"
            alt="Вы"
            class="avatar-small"
            @error="handleAvatarError"
          />
          <span class="nickname">{{ currentPlayerNickname }}</span>
        </div>

        <span class="score-value">{{ match.score }}</span>

        <div class="player-card">
          <img
            :src="getAvatarUrl(match.opponentAvatar)"
            :alt="match.opponent"
            class="avatar-small"
            @error="handleAvatarError"
          />
          <span class="nickname">{{ match.opponent }}</span>
        </div>
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
import defaultAvatar from '../assets/user.png'

const API_BASE = 'http://localhost:8084'

const props = defineProps({
  matches: { type: Array, default: () => [] },
  currentPlayerNickname: { type: String, default: 'Вы' },
  currentPlayerAvatar: { type: String, default: '' }
})

const getResultText = (result) => {
  const texts = { win: 'Победа', loss: 'Поражение', draw: 'Ничья' }
  return texts[result] || 'Поражение'
}

const getAvatarUrl = (avatarUrl) => {
  if (!avatarUrl) return defaultAvatar
  if (avatarUrl.startsWith('http')) return avatarUrl
  return `${API_BASE}${avatarUrl}`
}

const handleAvatarError = (e) => {
  e.target.src = defaultAvatar
}
</script>

<style scoped>
.matches-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
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
    border: 2px solid #3a507a;
    padding: 12px 20px;
    display: grid;
    grid-template-columns: 100px 1fr 120px;
    align-items: center;
    gap: 20px;
    transition: transform 0.2s, box-shadow 0.2s;
}

.match-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.match-map-info {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 100%;
}

.map-name {
    color: #888;
    font-size: 0.85rem;
    font-family: 'Courier New', monospace;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.match-faceoff {
    display: grid;
    grid-template-columns: 80px 100px 80px;
    align-items: center;
    justify-content: center;
    gap: 0;
    min-width: 0;
}

.player-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;
    width: 80px;
    min-width: 0;
}

.avatar-small {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
    border: 2px solid #3a507a;
    flex-shrink: 0;
}

.nickname {
    color: #fff;
    font-size: 0.9rem;
    font-weight: 500;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    width: 100%;
    text-align: center;
    line-height: 1.2;
}

.score-value {
    font-size: 1.6rem;
    font-weight: bold;
    color: #fff;
    font-family: 'Courier New', monospace;
    letter-spacing: 1px;
    white-space: nowrap;
    width: 100px;
    text-align: center;
    justify-self: center;
}

.match-meta {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 4px;
    min-width: 0;
}

.result-badge {
    padding: 4px 10px;
    border-radius: 12px;
    font-size: 0.8rem;
    font-weight: 600;
    white-space: nowrap;
}

.win .result-badge {
    background: rgba(76, 175, 80, 0.2);
    color: #4caf50;
}

.loss .result-badge {
    background: rgba(244, 67, 54, 0.2);
    color: #ff4d4d;
}

.draw .result-badge {
    background: rgba(255, 152, 0, 0.2);
    color: #ff9800;
}

.match-date {
    color: #666;
    font-size: 0.8rem;
    white-space: nowrap;
}
</style>