<template>
  <div class="bg-white rounded-lg shadow">
    <div class="p-5 flex justify-between items-baseline">
      <h3 class="deal-title">실거래가</h3>
      <button
        @click="resetPage"
        class="fold-button text-gray-500 text-sm flex items-center gap-1 hover:text-gray-700"
      >
        접기
        <ChevronUpIcon class="w-4 h-4" />
      </button>
    </div>

    <div class="overflow-x-auto">
      <table class="w-full">
        <thead class="bg-gray-50">
          <tr>
            <th colspan="2" class="px-4 py-3 text-center text-sm font-medium text-gray-500">
              계약일
            </th>
            <th class="px-4 py-3 text-center text-sm font-medium text-gray-500">전용면적</th>
            <th class="px-4 py-3 text-center text-sm font-medium text-gray-500">가격</th>
            <th class="px-4 py-3 text-center text-sm font-medium text-gray-500">층</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="year in displayedYears" :key="year">
            <tr class="bg-gray-50">
              <td
                :rowspan="displayedTransactions[year].length + 1"
                class="px-4 py-3 text-sm font-medium border-r text-center"
              >
                {{ year }}
              </td>
            </tr>
            <tr
              v-for="deal in displayedTransactions[year]"
              :key="`${deal.dealYear}${deal.dealMonth}${deal.dealDay}`"
              class="hover:bg-gray-100"
            >
              <td class="px-4 py-3 text-sm text-center">
                {{ formatDate(deal.dealMonth, deal.dealDay) }}
              </td>
              <td class="px-4 py-3 text-sm text-center">{{ deal.excluUseAr }}㎡</td>
              <td class="px-4 py-3 text-sm text-center">{{ formatPrice(deal.tradeAmount) }}</td>
              <td class="px-4 py-3 text-sm text-center">{{ deal.floor }}층</td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>

    <div v-if="hasMoreItems" class="p-4 text-center border-t">
      <button
        @click="loadMore"
        class="text-gray-500 text-sm flex items-center justify-center w-full gap-1 hover:text-gray-700"
      >
        더보기
        <ChevronDownIcon class="w-4 h-4" />
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ChevronDownIcon, ChevronUpIcon } from 'lucide-vue-next'
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'

const houseStore = useHouseStore()
const { aptDeals } = storeToRefs(houseStore)

const itemsPerPage = 10
const currentPage = ref(1)

const formatDate = (month, day) => {
  return `${month.toString().padStart(2, '0')}.${day.toString().padStart(2, '0')}`
}

const formatPrice = (price) => {
  if (!price) return ''
  const amount = price.replace(/,/g, '')
  if (parseInt(amount) >= 10000) {
    const uk = Math.floor(parseInt(amount) / 10000)
    const rest = parseInt(amount) % 10000
    return rest > 0 ? `${uk}억 ${rest}` : `${uk}억`
  }
  return price + '만'
}

// 거래 데이터를 연도별로 그룹화하고 페이징 처리
const displayedTransactions = computed(() => {
  const endIndex = currentPage.value * itemsPerPage
  const visibleDeals = aptDeals.value
    .slice()
    .sort((a, b) => {
      // 연도 내림차순
      if (b.dealYear !== a.dealYear) return b.dealYear - a.dealYear
      // 월 내림차순
      if (b.dealMonth !== a.dealMonth) return b.dealMonth - a.dealMonth
      // 일 내림차순
      return b.dealDay - a.dealDay
    })
    .slice(0, endIndex)

  const groupedData = {}
  visibleDeals.forEach((deal) => {
    const year = deal.dealYear.toString()
    if (!groupedData[year]) {
      groupedData[year] = []
    }
    // 각 연도 내에서도 최신순으로 정렬
    groupedData[year].push(deal)
    groupedData[year].sort((a, b) => {
      if (b.dealMonth !== a.dealMonth) return b.dealMonth - a.dealMonth
      return b.dealDay - a.dealDay
    })
  })

  return groupedData
})

// 표시할 연도 목록 (내림차순 정렬)
const displayedYears = computed(() => {
  return Object.keys(displayedTransactions.value).sort((a, b) => b - a)
})

const hasMoreItems = computed(() => {
  return aptDeals.value.length > currentPage.value * itemsPerPage
})

const loadMore = () => {
  currentPage.value++
}

watch(aptDeals, () => {
  currentPage.value = 1
})

// 접기 버튼 클릭 핸들러
const resetPage = () => {
  currentPage.value = 1
}
</script>

<style scoped>
.border-navy-600 {
  border-color: #1a365d;
}

.text-navy-600 {
  color: #1a365d;
}

table {
  border-collapse: separate;
  border-spacing: 0;
}

td {
  border-top-width: 1px;
  border-color: #e5e7eb;
}

tbody tr:first-child td {
  border-top: none;
}

.deal-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #333;
  line-height: 1;
}

.fold-button {
  line-height: 1;
  padding: 0;
}

button {
  transition: all 0.2s ease;
}

button:hover {
  transform: scale(1.05);
}

.flex {
  display: flex;
}

.justify-between {
  justify-content: space-between;
}

.items-center {
  align-items: center;
}

.gap-1 {
  gap: 0.25rem;
}
</style>
