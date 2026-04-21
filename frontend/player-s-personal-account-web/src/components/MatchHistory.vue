<template>
  <div class="matches-list">
    <div
      v-for="match in matches"
      :key="match.id"
      class="match-item"
      :class="match.result"
    >
      <div class="match-info">
        <span class="opponent">vs {{ match.opponent }}</span>
        <span class="map">{{ match.map }}</span>
      </div>
      <div class="match-result">
        <span class="score">{{ match.score }}</span>
        <span class="result-badge">{{ match.result === 'win' ? '✓ Победа' : '✗ Поражение' }}</span>
      </div>
      <div class="match-date">
        {{ new Date(match.date).toLocaleDateString('ru-RU') }}
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  matches: { type: Array, default: () => [] }
})
</script>

<style scoped>
.matches-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.match-item {
    background: #16213e;
    border-radius: 10px;
    padding: 16px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-left: 4px solid transparent;
}

.match-item.win {
    border-left-color: #4caf50;
}

.match-item.loss {
    border-left-color: #f44336;
}

.match-info {
    display: flex;
    flex-direction: column;
    gap: 4px;
}

.opponent {
    font-weight: 600;
    font-size: 1.1rem;
}

.map {
    color: #888;
    font-size: 0.9rem;
}

.match-result {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 4px;
}

.score {
    font-size: 1.3rem;
    font-weight: bold;
}

.result-badge {
    padding: 4px 12px;
    border-radius: 20px;
    font-size: 0.85rem;
    font-weight: 500;
}

.win .result-badge {
    background: rgba(76, 175, 80, 0.2);
    color: #4caf50;
}

.loss .result-badge {
    background: rgba(244, 67, 54, 0.2);
    color: #f44336;
}

.match-date {
    color: #666;
    font-size: 0.9rem;
}
</style>