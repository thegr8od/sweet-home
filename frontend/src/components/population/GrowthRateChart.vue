<!-- GrowthRateChart.vue -->
<template>
  <div class="mb-8 h-[400px]">
    <Bar v-if="data.datasets.length" :data="data" :options="chartOptions" />
  </div>
</template>

<script setup>
import { Bar } from 'vue-chartjs'

const props = defineProps({
  data: {
    type: Object,
    required: true,
  },
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  scales: {
    y: {
      ticks: {
        callback: function (value) {
          return value + '%'
        },
      },
      title: {
        display: true,
        text: '증가율 (%)',
      },
    },
    x: {
      title: {
        display: true,
        text: '월',
      },
    },
  },
}
</script>
