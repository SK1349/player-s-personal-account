<template>
  <div class="chart-box">
    <h3>K/D Ratio</h3>
    <div class="kdr-value">{{ kdr }}</div>
  </div>
</template>

<script setup>
defineProps(['kdr'])
</script>
<style scoped>
.chart-box {
    background: #16213e;
    padding: 20px;
    border-radius: 10px;
    text-align: center;
}

.kdr-value {
    font-size: 3rem;
    font-weight: bold;
    color: #ff4d4d;
}
</style>