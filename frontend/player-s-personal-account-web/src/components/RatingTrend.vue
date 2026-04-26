<template>
  <div class="chart-card">
    <h3>Динамика рейтинга</h3>
    <div class="chart-wrapper">
      <canvas ref="chartRef" style="border: 2px solid red; display: block;"></canvas>
      <div v-if="!chartData" class="placeholder">Нет данных</div>
    </div>
    <div class="current-rating" v-if="currentRating !== null">
      <span class="rating-value">{{ currentRating }}</span>
      <span class="rating-label">текущий рейтинг</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Tooltip, Filler } from 'chart.js'

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Tooltip, Filler)

const props = defineProps({
  matches: { type: Array, default: () => [] },
  currentRating: { type: Number, default: null }
})

const chartRef = ref(null)
let chartInstance = null

const sortedMatches = computed(() => {
  return [...props.matches]
    .filter(m => m?.playedAt)
    .sort((a, b) => new Date(a.playedAt) - new Date(b.playedAt))
})

const chartData = computed(() => {
  const matches = sortedMatches.value
  if (!matches.length) return null

  let rating = 1000
  const labels = [], ratings = []
  matches.forEach((m, i) => {
    const r = (m.result || '').toUpperCase()
    if (r === 'WIN') rating += 15
    else if (r === 'LOSS') rating = Math.max(0, rating - 10)
    else if (r === 'DRAW') rating += 2
    labels.push(`#${i + 1}`)
    ratings.push(rating)
  })
   return { labels, datasets: [{ label: 'Рейтинг', data: ratings, borderColor: '#e94560', backgroundColor: 'rgba(233,69,96,0.1)', borderWidth: 2, pointRadius: 4, tension: 0.3, fill: true }] }
})

const renderChart = async () => {
  await nextTick()

  const parentWidth = chartRef.value.parentElement.clientWidth || 600
  chartRef.value.width = parentWidth
  chartRef.value.height = 200

  const data = chartData.value
  if (!data) return

  const pureData = JSON.parse(JSON.stringify(data))
  try {
    if (chartInstance) {
      chartInstance.data = pureData
      chartInstance.update()
    } else {
      chartInstance = new ChartJS(chartRef.value.getContext('2d'), {
        type: 'line',
        data: pureData,
        options: {
          responsive: false,
          maintainAspectRatio: false,
          animation: false,
          scales: {
            x: { grid: { display: false }, ticks: { color: '#888', font: { size: 11 } } },
            y: {
              grid: { color: 'rgba(255,255,255,0.1)' },
              ticks: { color: '#888', font: { size: 11 }, stepSize: 20 },
              min: 900,
              max: 1100
            }
          },
          plugins: { legend: { display: false }, tooltip: { enabled: true } }
        }
      })
    }
  } catch (e) {
  }
}

watch(() => props.matches, renderChart, { deep: true })

onMounted(() => {
  renderChart()
})
</script>

<style scoped>
.chart-card {
  background: linear-gradient(145deg, #16213e, #1a2744);
  border-radius: 16px;
  padding: 24px;
  margin-top: 24px;
}
.chart-wrapper {
  position: relative;
  width: 100%;
  height: 200px;
}
.placeholder {
  position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);
  color: #666; font-style: italic;
}
.current-rating {
  margin-top: 16px; text-align: center; padding-top: 16px;
  border-top: 1px solid rgba(255,255,255,0.05);
  color: #ff4d4d; font-size: 1.5rem; font-weight: bold;
}
</style>