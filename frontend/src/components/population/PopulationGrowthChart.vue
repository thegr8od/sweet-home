<template>
  <div class="bg-blue-50 p-4 rounded-lg mb-6">
    <h3 class="text-lg font-semibold text-blue-800 mb-3">월별 인구 증감률</h3>
    <div class="h-[300px] bg-white rounded-lg p-4 mb-3">
      <Bar
        v-if="chartData.datasets.length"
        :data="processedChartData"
        :options="chartOptions"
      />
    </div>
    <div class="bg-white rounded-lg p-4">
      <table class="min-w-full">
        <thead>
          <tr class="text-gray-600">
            <th class="text-left pb-3">기간</th>
            <th class="text-right pb-3">총 인구</th>
            <th class="text-right pb-3">증감률</th>
            <th class="text-right pb-3">증감 인원</th>
          </tr>
        </thead>
        <tbody class="divide-y divide-gray-100">
          <tr v-for="(rate, index) in growthRates" :key="rate.month">
            <td class="py-2">{{ formatMonth(rate.month) }}</td>
            <td class="text-right py-2">
              <div>{{ formatNumber(rate.population) }}명</div>
            </td>
            <td class="text-right py-2">
              <div :class="getGrowthRateColor(rate.growthRate)">
                {{ rate.growthRate > 0 ? '+' : '' }}{{ rate.growthRate }}%
              </div>
            </td>
            <td class="text-right py-2">
              <div :class="getGrowthRateColor(rate.growthRate)">
                {{ formatPopulationChange(rate, index) }}
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
} from 'chart.js'

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
)

const props = defineProps({
  chartData: {
    type: Object,
    required: true
  }
})

const processedChartData = computed(() => ({
  labels: props.chartData.labels,
  datasets: [{
    label: '인구 증감률',
    data: props.chartData.datasets[0].data,
    backgroundColor: props.chartData.datasets[0].data.map(value =>
      value > 0 ? 'rgba(239, 68, 68, 0.1)' : 'rgba(59, 130, 246, 0.1)'
    ),
    borderColor: props.chartData.datasets[0].data.map(value =>
      value > 0 ? 'rgb(239, 68, 68)' : 'rgb(59, 130, 246)'
    ),
    borderWidth: 1.5,
    borderRadius: 4,
    barPercentage: 0.15,
    categoryPercentage: 0.9,
  }]
}))

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      display: false
    },
    tooltip: {
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      titleColor: '#1F2937',
      bodyColor: '#4B5563',
      padding: 10,
      borderColor: 'rgba(0, 0, 0, 0.1)',
      borderWidth: 1,
      callbacks: {
        label: (context) => {
          const value = Number(context.raw).toFixed(2);
          return `${value}%`;
        }
      }
    }
  },
  scales: {
    y: {
      min: function(context) {
        const min = Math.min(...context.chart.data.datasets[0].data);
        return Math.floor(min * 10) / 10 - 0.1;
      },
      max: function(context) {
        const max = Math.max(...context.chart.data.datasets[0].data);
        return Math.ceil(max * 10) / 10 + 0.1;
      },
      grid: {
        color: (context) => {
          if (context.tick.value === 0) {
            return 'rgba(0, 0, 0, 0.2)';
          }
          return 'rgba(0, 0, 0, 0.05)';
        },
        lineWidth: (context) => {
          if (context.tick.value === 0) {
            return 2;
          }
          return 1;
        }
      },
      ticks: {
        callback: (value) => {
          return Number(value).toFixed(1) + '%';
        },
        font: {
          size: 11
        },
        color: (context) => {
          if (context.tick.value === 0) {
            return '#374151';
          }
          return '#6B7280';
        }
      }
    },
    x: {
      grid: {
        display: false
      },
      ticks: {
        font: {
          size: 12,
          weight: 'medium'
        },
        color: '#374151'
      }
    }
  },
  interaction: {
    intersect: false,
    mode: 'index'
  }
};

// 증감률 데이터 계산
const growthRates = computed(() => {
  const labels = props.chartData.labels
  const rates = props.chartData.datasets[0].data
  const populations = props.chartData.datasets[0].originalData || []

  return labels.map((label, index) => ({
    month: label.replace('-', ''),
    population: populations[index] || 0,
    growthRate: rates[index] || 0
  }))
})

// 숫자 포맷팅
const formatNumber = (num) => {
  return new Intl.NumberFormat('ko-KR').format(num)
}

// 월 포맷팅
const formatMonth = (month) => {
  return `${month.slice(0, 4)}년 ${month.slice(4)}월`
}

// 증감률에 따른 색상 클래스
const getGrowthRateColor = (rate) => {
  if (rate > 0) return 'text-red-500'
  if (rate < 0) return 'text-blue-500'
  return 'text-gray-500'
}

// 인구 변동 수 계산 및 포맷팅
const formatPopulationChange = (rate, index) => {
  if (index === 0) return '-'
  const prevPopulation = growthRates.value[index - 1].population
  const change = rate.population - prevPopulation
  const sign = change > 0 ? '+' : ''
  return `${sign}${formatNumber(change)}명`
}
</script>
