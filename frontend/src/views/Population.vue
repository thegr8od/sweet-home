<!-- YourComponent.vue -->
<template>
  <div class="max-w-7xl mx-auto px-4 py-8">
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <!-- 동 이름 입력 -->
      <div class="mb-6">
        <input
          v-model="dongName"
          type="text"
          placeholder="동 이름을 입력하세요 (예: 삼청동)"
          class="px-4 py-2 border rounded w-full"
          @keyup.enter="fetchData"
        />
      </div>

      <!-- 기간 선택 -->
      <div class="mb-6">
        <div class="flex gap-2 items-center">
          <div class="flex flex-col">
            <label class="text-sm text-gray-600 mb-1">시작 월</label>
            <input
              type="month"
              v-model="period.from"
              class="px-3 py-2 border rounded"
              min="2022-10"
              :max="maxFromDate"
              @change="adjustToDate"
            />
          </div>
          <span class="mt-6">~</span>
          <div class="flex flex-col">
            <label class="text-sm text-gray-600 mb-1">종료 월</label>
            <input
              type="month"
              v-model="period.to"
              class="px-3 py-2 border rounded"
              :min="period.from"
              :max="maxToDate"
            />
          </div>
          <button
            @click="fetchData"
            class="px-4 py-2 bg-blue-500 text-white rounded mt-6"
          >
            조회
          </button>
        </div>
        <p class="text-sm text-gray-500 mt-2">
          * 시작 월로부터 최대 3개월 이내로 선택 가능합니다.
        </p>
      </div>

      <!-- 로딩 상태 -->
      <div v-if="store.loading" class="text-center py-4">
        데이터를 불러오는 중...
      </div>

      <!-- 에러 메시지 -->
      <div v-else-if="store.error" class="text-red-500 text-center py-4">
        {{ store.error }}
      </div>

      <!-- 데이터 표시 -->
      <div v-else-if="store.populationData.length > 0">
        <!-- 요약 정보 -->
        <PopulationSummary :data="store.populationData" />

        <!-- 인구 차트 -->
        <div class="mb-8 h-[400px]">
          <Bar
            v-if="store.chartData.datasets.length"
            :data="store.chartData"
            :options="chartOptions"
          />
        </div>

        <!-- 인구 증감률 차트 -->
        <PopulationGrowthChart
          v-if="store.growthChartData.datasets.length"
          :chartData="store.growthChartData"
        />

        <!-- 데이터 테이블 -->
        <div class="overflow-x-auto">
          <PopulationTable :data="store.populationData" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { usePopulationStore } from '@/stores/populationStore'
import { Bar } from 'vue-chartjs'
import PopulationSummary from '@/components/population/PopulationSummary.vue'
import PopulationTable from '@/components/population/PopulationTable.vue'
import PopulationGrowthChart from '@/components/population/PopulationGrowthChart.vue'

const store = usePopulationStore()
const dongName = ref('')
const period = ref({
  from: '2022-10',
  to: '2022-12'
})

// 현재 날짜 계산
const now = new Date()
const currentYearMonth = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`

// 시작 월의 최대값 (현재 월)
const maxFromDate = computed(() => currentYearMonth)

// 종료 월의 최대값 (시작 월로부터 3개월)
const maxToDate = computed(() => {
  if (!period.value.from) return currentYearMonth

  const fromDate = new Date(period.value.from + '-01')
  const maxDate = new Date(fromDate)
  maxDate.setMonth(maxDate.getMonth() + 2) // 3개월 범위이므로 +2

  const maxYear = maxDate.getFullYear()
  const maxMonth = String(maxDate.getMonth() + 1).padStart(2, '0')

  const calculatedMax = `${maxYear}-${maxMonth}`

  // 현재 월을 넘지 않도록 제한
  return calculatedMax > currentYearMonth ? currentYearMonth : calculatedMax
})

// 시작 월이 변경될 때 종료 월 자동 설정
const adjustToDate = () => {
  if (!period.value.from) return

  const fromDate = new Date(period.value.from + '-01')
  const autoToDate = new Date(fromDate)
  autoToDate.setMonth(autoToDate.getMonth() + 2) // 3개월 범위이므로 +2

  const toYear = autoToDate.getFullYear()
  const toMonth = String(autoToDate.getMonth() + 1).padStart(2, '0')
  const calculatedTo = `${toYear}-${toMonth}`

  // 현재 월을 넘지 않도록 제한
  period.value.to = calculatedTo > currentYearMonth ? currentYearMonth : calculatedTo
}

// 초기값 설정
onMounted(() => {
  period.value.from = '2022-10'
  period.value.to = '2022-12'
  if (dongName.value) {
    fetchData()
  }
})

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
      callbacks: {
        label: (context) => {
          const value = context.raw;
          const formattedValue = new Intl.NumberFormat('ko-KR').format(value);
          return `${formattedValue}명`;
        }
      }
    }
  },
  scales: {
    y: {
      grace: '5%',    // 위아래 여백 5%
      ticks: {
        count: 6,     // 눈금 개수를 6개로 제한
        callback: (value) => new Intl.NumberFormat('ko-KR').format(value),
        font: {
          size: 11
        },
        color: '#6B7280'
      },
      grid: {
        color: 'rgba(0, 0, 0, 0.05)',
        drawBorder: false
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
  }
}

const fetchData = async () => {
  if (!dongName.value) {
    store.error = '동 이름을 입력해주세요.'
    return
  }

  if (!period.value.from || !period.value.to) {
    store.error = '기간을 선택해주세요.'
    return
  }

  try {
    await store.fetchPopulationData(dongName.value, period.value.from, period.value.to)
  } catch (error) {
    console.error('데이터 조회 실패:', error)
  }
}
</script>
