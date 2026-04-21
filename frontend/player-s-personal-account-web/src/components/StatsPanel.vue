<template>
  <div class="stats-container">
    <div class="charts-grid">
      <div class="chart-card">
        <h3>Результаты матчей</h3>
        <Pie :data="winLossData" :options="chartOptions" />
      </div>

      <div class="chart-card kdr-card">
        <h3>K/D Ratio</h3>
        <div class="kdr-value">{{ stats.kdr?.toFixed(2) || '0.00' }}</div>
        <p class="kdr-label">Убийств на смерть</p>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-item">
        <span class="label">Всего матчей</span>
        <span class="value">{{ stats.totalMatches || 0 }}</span>
      </div>
      <div class="stat-item">
        <span class="label">Процент побед</span>
        <span class="value win">{{ stats.winRate?.toFixed(1) || 0 }}%</span>
      </div>
      <div class="stat-item">
        <span class="label">Победы</span>
        <span class="value win">{{ stats.wins || 0 }}</span>
      </div>
      <div class="stat-item">
        <span class="label">Поражения</span>
        <span class="value loss">{{ stats.losses || 0 }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Pie } from 'vue-chartjs'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'

ChartJS.register(ArcElement, Tooltip, Legend)

const props = defineProps({
  stats: { type: Object, default: () => ({}) }
})

const winLossData = computed(() => ({
  labels: ['Победы', 'Поражения', 'Ничьи'],
  datasets: [{
    data: [
      props.stats.wins || 0,
      props.stats.losses || 0,
      props.stats.draws || 0
    ],
    backgroundColor: ['#4caf50', '#f44336', '#ff9800'],
    borderWidth: 0
  }]
}))

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { position: 'bottom', labels: { color: '#fff' } }
  }
}
</script>

<style scoped>
.stats-container {
    display: flex;
    flex-direction: column;
    gap: 24px;
}

.charts-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
}

.chart-card {
    background: #16213e;
    border-radius: 12px;
    padding: 20px;
    min-height: 250px;
}

.kdr-card {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
}

.kdr-value {
    font-size: 3.5rem;
    font-weight: bold;
    color: #e94560;
    line-height: 1;
}

.kdr-label {
    color: #888;
    margin-top: 8px;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 16px;
}

.stat-item {
    background: #16213e;
    padding: 16px;
    border-radius: 8px;
    text-align: center;
}

.stat-item .label {
    display: block;
    color: #888;
    font-size: 0.9rem;
    margin-bottom: 8px;
}

.stat-item .value {
    font-size: 1.5rem;
    font-weight: 600;
}

.stat-item .value.win {
    color: #4caf50;
}

.stat-item .value.loss {
    color: #f44336;
}
</style>