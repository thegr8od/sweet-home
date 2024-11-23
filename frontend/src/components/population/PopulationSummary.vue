<!-- PopulationSummary.vue -->
<template>
  <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-8">
    <!-- 전체 통계 카드 -->
    <div class="bg-blue-50 p-4 rounded-lg">
      <h3 class="text-lg font-semibold text-blue-800 mb-3">인구 현황</h3>
      <div class="space-y-3">
        <div class="flex justify-between items-center">
          <span>총 인구</span>
          <div class="text-right">
            <div>{{ formatNumber(currentPopulation) }}명</div>
            <div class="text-sm" :class="getChangeColor(populationChange)">
              {{ populationChange > 0 ? '+' : '' }}{{ formatNumber(populationChange) }}명
              <span class="ml-1">({{ changeRate }}%)</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 성별 통계 카드 -->
    <div class="bg-pink-50 p-4 rounded-lg">
      <h3 class="text-lg font-semibold text-pink-800 mb-3">성별 현황</h3>
      <div class="space-y-3">
        <div class="flex justify-between items-center">
          <span>남성</span>
          <div class="text-right">
            <div>{{ formatNumber(currentMale) }}명</div>
            <div class="text-sm" :class="getChangeColor(maleChange)">
              {{ maleChange > 0 ? '+' : '' }}{{ formatNumber(maleChange) }}명
              <span class="ml-1">({{ maleChangeRate }}%)</span>
            </div>
          </div>
        </div>
        <div class="flex justify-between items-center">
          <span>여성</span>
          <div class="text-right">
            <div>{{ formatNumber(currentFemale) }}명</div>
            <div class="text-sm" :class="getChangeColor(femaleChange)">
              {{ femaleChange > 0 ? '+' : '' }}{{ formatNumber(femaleChange) }}명
              <span class="ml-1">({{ femaleChangeRate }}%)</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 세대 통계 카드 -->
    <div class="bg-green-50 p-4 rounded-lg">
      <h3 class="text-lg font-semibold text-green-800 mb-3">세대 현황</h3>
      <div class="space-y-3">
        <div class="flex justify-between items-center">
          <span>세대수</span>
          <div class="text-right">
            <div>{{ formatNumber(currentHouseholds) }}세대</div>
            <div class="text-sm" :class="getChangeColor(householdChange)">
              {{ householdChange > 0 ? '+' : '' }}{{ formatNumber(householdChange) }}세대
              <span class="ml-1">({{ householdChangeRate }}%)</span>
            </div>
          </div>
        </div>
        <div class="flex justify-between items-center">
          <span>세대당 평균</span>
          <div class="text-right">
            <div>{{ averagePerHousehold }}명</div>
            <div class="text-sm text-gray-500">
              {{ prevAveragePerHousehold }}명 대비
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  data: {
    type: Array,
    required: true
  }
})

// 숫자 포맷팅 함수
const formatNumber = (num) => {
  return new Intl.NumberFormat('ko-KR').format(num)
}

// 월별 데이터 정리
const monthlyData = computed(() => {
  const months = [...new Set(props.data.map(item => item.statsYm))].sort()
  const firstMonth = months[0]
  const lastMonth = months[months.length - 1]

  const getMonthTotal = (month) => {
    const monthData = props.data.filter(item => item.statsYm === month)
    return {
      population: monthData.reduce((sum, item) => sum + item.totalPopulation, 0),
      households: monthData.reduce((sum, item) => sum + item.households, 0),
      male: monthData.reduce((sum, item) => sum + item.male, 0),
      female: monthData.reduce((sum, item) => sum + item.female, 0)
    }
  }

  return {
    first: getMonthTotal(firstMonth),
    current: getMonthTotal(lastMonth)
  }
})

// 현재 값
const currentPopulation = computed(() => monthlyData.value.current.population)
const currentHouseholds = computed(() => monthlyData.value.current.households)
const currentMale = computed(() => monthlyData.value.current.male)
const currentFemale = computed(() => monthlyData.value.current.female)

// 변동량
const populationChange = computed(() =>
  monthlyData.value.current.population - monthlyData.value.first.population
)
const householdChange = computed(() =>
  monthlyData.value.current.households - monthlyData.value.first.households
)
const maleChange = computed(() =>
  monthlyData.value.current.male - monthlyData.value.first.male
)
const femaleChange = computed(() =>
  monthlyData.value.current.female - monthlyData.value.first.female
)

// 변동률
const calculateChangeRate = (current, previous) => {
  return ((current - previous) / previous * 100).toFixed(1)
}

const changeRate = computed(() =>
  calculateChangeRate(monthlyData.value.current.population, monthlyData.value.first.population)
)
const householdChangeRate = computed(() =>
  calculateChangeRate(monthlyData.value.current.households, monthlyData.value.first.households)
)
const maleChangeRate = computed(() =>
  calculateChangeRate(monthlyData.value.current.male, monthlyData.value.first.male)
)
const femaleChangeRate = computed(() =>
  calculateChangeRate(monthlyData.value.current.female, monthlyData.value.first.female)
)

// 세대당 평균
const averagePerHousehold = computed(() =>
  (currentPopulation.value / currentHouseholds.value).toFixed(2)
)
const prevAveragePerHousehold = computed(() =>
  (monthlyData.value.first.population / monthlyData.value.first.households).toFixed(2)
)

// 증감에 따른 색상
const getChangeColor = (change) => {
  if (change > 0) return 'text-red-500'
  if (change < 0) return 'text-blue-500'
  return 'text-gray-500'
}
</script>
