<template>
  <div class="stats-container">
    <div class="charts-grid">
      <div class="chart-card">
        <h3>Результаты матчей</h3>
        <div class="chart-wrapper">
          <Pie v-if="chartData" :data="chartData" :options="chartOptions" />
          <div v-else class="chart-placeholder">
            <span>Нет данных для отображения</span>
          </div>
        </div>

        <div class="stats-row">
          <div class="stat-card highlight">
            <div class="stat-content">
              <span class="stat-label">Процент побед</span>
              <span class="stat-value">{{ safeStats.winRate?.toFixed(1) ?? 0 }}%</span>
            </div>
          </div>

          <div class="stat-card primary">
            <div class="stat-content">
              <span class="stat-label">Всего матчей</span>
              <span class="stat-value">{{ safeStats.matchesPlayed ?? 0 }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="chart-card kdr-card">
        <h3>K/D Ratio</h3>
        <div class="kdr-value">{{ safeStats.kdRatio?.toFixed(2) || '0.00' }}</div>
        <p class="kdr-label">Убийств на смерть</p>
        <div class="kdr-details">
          <div class="kdr-stat">
            <span class="label">Убийства</span>
            <span class="value kill">{{ safeStats.totalKills ?? 0 }}</span>
          </div>
          <div class="kdr-stat">
            <span class="label">Смерти</span>
            <span class="value death">{{ safeStats.totalDeaths ?? 0 }}</span>
          </div>
        </div>
      </div>
    </div>

    <RatingTrend
      :key="matches.length"
      :matches="matches"
      :current-rating="user?.rating ?? null"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Pie } from 'vue-chartjs'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import RatingTrend from './RatingTrend.vue'

ChartJS.register(ArcElement, Tooltip, Legend)

const props = defineProps({
  stats: { type: Object, default: () => null },
  matches: { type: Array, default: () => [] },
  user: { type: Object, default: () => ({}) }
})

const safeStats = computed(() => props.stats || {
  matchesPlayed: 0, wins: 0, losses: 0, draws: 0,
  totalKills: 0, totalDeaths: 0, winRate: 0, kdRatio: 0
})

const chartData = computed(() => {
  const { wins, losses, draws } = safeStats.value
  const total = wins + losses + draws
  if (total === 0) return null

  return {
    labels: ['Победы', 'Поражения', 'Ничьи'],
    datasets: [{
      data: [wins, losses, draws],
      backgroundColor: ['#4caf50', '#f44336', '#ff9800'],
      borderWidth: 0,
      hoverOffset: 8
    }]
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: true,
  plugins: {
    legend: {
      position: 'bottom',
      labels: {
        color: '#fff',
        padding: 20,
        font: { size: 13, family: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif" },
        usePointStyle: true,
        pointStyle: 'circle'
      }
    },
    tooltip: {
      backgroundColor: 'rgba(22, 33, 62, 0.95)',
      titleColor: '#fff',
      bodyColor: '#ccc',
      borderColor: '#3a507a',
      borderWidth: 1,
      padding: 12,
      displayColors: true,
      callbacks: {
        label: function(context) {
          const label = context.label || ''
          const value = context.parsed || 0
          const total = context.dataset.data.reduce((a, b) => a + b, 0)
          const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0
          return `${label}: ${value} (${percentage}%)`
        }
      }
    }
  }
}
</script>

<style scoped>
.stats-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 16px;
  max-width: 1400px;
  margin: 0 auto;
}

.charts-grid {
  display: grid;
  grid-template-columns: 1.2fr 0.8fr;
  gap: 24px;
}

@media (max-width: 1024px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
}

.chart-card {
  background: linear-gradient(145deg, #16213e 0%, #1a2744 100%);
  border-radius: 16px;
  padding: 24px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.chart-card h3 {
  margin: 0 0 20px 0;
  color: #fff;
  font-size: 1.2rem;
  font-weight: 600;
  text-align: center;
}

.chart-wrapper {
  flex: 1;
  min-height: 250px;
  max-height: 350px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
  font-style: italic;
}

.kdr-card {
  justify-content: center;
  align-items: center;
  text-align: center;
}

.kdr-value {
  font-size: 4rem;
  font-weight: bold;
  background: linear-gradient(135deg, #e94560 0%, #ff6b6b 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1;
  margin: 16px 0;
}

.kdr-label {
  color: #888;
  margin: 0;
  font-size: 1rem;
}

.kdr-details {
  margin-top: 32px;
  display: flex;
  gap: 32px;
  justify-content: center;
  width: 100%;
}

.kdr-stat {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  min-width: 120px;
}

.kdr-stat .label {
  color: #888;
  font-size: 0.9rem;
}

.kdr-stat .value {
  font-size: 1.8rem;
  font-weight: bold;
}

.kdr-stat .value.kill { color: #4caf50; }
.kdr-stat .value.death { color: #f44336; }

.stats-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

@media (max-width: 640px) {
  .stats-row {
    grid-template-columns: 1fr;
  }
}

.stat-card {
  background: linear-gradient(145deg, #16213e 0%, #1a2744 100%);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.stat-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}

.stat-label {
  color: #888;
  font-size: 0.9rem;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: bold;
  color: #fff;
}


.stat-card.highlight .stat-value { color: #e94560; }


.stat-card.primary .stat-value { color: #2196f3; }

@media (max-width: 480px) {
  .kdr-value { font-size: 3rem; }
  .kdr-details { flex-direction: column; gap: 12px; }
  .stat-card { padding: 16px; }
  .stat-value { font-size: 1.5rem; }
}
</style>