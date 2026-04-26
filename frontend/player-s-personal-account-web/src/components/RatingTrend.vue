<template>
  <div class="chart-card">
    <h3>Динамика рейтинга</h3>
    <div class="chart-wrapper">
      <canvas ref="chartRef"></canvas>
      <div v-if="!ratingHistory?.length" class="placeholder">Нет данных</div>
    </div>
    <div class="current-rating" v-if="currentRating != null">
      <span class="rating-value">{{ currentRating }}</span>
      <span class="rating-label">текущий рейтинг</span>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Tooltip,
  Filler
} from 'chart.js'

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Tooltip, Filler)

const props = defineProps({
  ratingHistory: { type: Array, default: () => [] },
  currentRating: { type: Number, default: null }
})

const chartRef = ref(null)
let chartInstance = null

const render = () => {
  if (!chartRef.value) return

  if (!props.ratingHistory?.length) {
    if (chartInstance) { chartInstance.destroy(); chartInstance = null }
    return
  }

  const data = {
    labels: props.ratingHistory.map((_, i) => `#${i + 1}`),
    datasets: [{
      data: [...props.ratingHistory],
      borderColor: '#e94560',
      backgroundColor: 'rgba(233,69,96,0.1)',
      borderWidth: 2,
      pointRadius: 4,
      tension: 0,
      fill: true
    }]
  }

  if (chartInstance) {
    chartInstance.data = data
    chartInstance.update('none')
  } else {
    chartInstance = new ChartJS(chartRef.value, {
      type: 'line',
      data: data,
      options: {
        responsive: true,
        maintainAspectRatio: false,
        animation: false,
        scales: {
          x: { grid: { display: false }, ticks: { color: '#888' } },
          y: { grid: { color: 'rgba(255,255,255,0.1)' }, ticks: { color: '#888' } }
        },
        plugins: { legend: { display: false }, tooltip: { enabled: true } }
      }
    })
  }
}

watch(() => props.ratingHistory, render)
onMounted(render)
</script>

<style scoped>
.chart-card {
    background: #16213e;
    border-radius: 16px;
    padding: 24px;
    margin-top: 24px;
}

.chart-card h3 {
    margin: 0 auto 20px auto;
    text-align: center;
    width: 100%;
    color: #fff;
    font-size: 1.2rem;
    font-weight: 600;
}

.chart-wrapper {
    position: relative;
    height: 200px;
}
.placeholder {
    position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);
    color: #666;
}
.current-rating {
    margin-top: 16px;
    text-align: center;
    border-top: 1px solid rgba(255, 255, 255, 0.05);
    padding-top: 16px;
}
.rating-value {
    display: block;
    font-size: 2rem;
    font-weight: bold;
    color: #e94560;
}
.rating-label { color: #888; font-size: 0.9rem; }
</style>