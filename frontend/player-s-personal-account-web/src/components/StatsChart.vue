<template>
  <div class="chart-box">
    <Pie :data="chartData" :options="options" />
  </div>
</template>

<script setup>
import { Pie } from 'vue-chartjs'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { computed } from 'vue'

ChartJS.register(ArcElement, Tooltip, Legend)

const props = defineProps(['wins', 'losses'])

const chartData = computed(() => ({
  labels: ['Победы', 'Поражения'],
  datasets: [{
    data: [props.wins, props.losses],
    backgroundColor: ['#4caf50', '#f44336']
  }]
}))

const options = { responsive: true, plugins: { legend: { position: 'bottom' } } }
</script>

<style scoped>
.chart-box {
    background: #16213e;
    padding: 20px;
    border-radius: 10px;
}
</style>