<template>
  <div class="relative">
    <div class="flex gap-4">
      <div class="relative flex-1">
        <input
          type="text"
          v-model="searchKeyword"
          @input="handleInput"
          @focus="showResults = true"
          placeholder="동 이름을 입력하세요 (예: 삼청동)"
          class="w-full px-4 py-2 border rounded-lg"
        />

        <!-- 검색 결과 드롭다운 -->
        <div
          v-if="showResults && locationStore.searchResults.length > 0"
          class="absolute z-10 w-full mt-1 bg-white border rounded-lg shadow-lg"
        >
          <ul>
            <li
              v-for="location in locationStore.searchResults"
              :key="location.code"
              @click="selectLocation(location)"
              class="px-4 py-2 hover:bg-gray-100 cursor-pointer"
            >
              {{ location.fullName }}
            </li>
          </ul>
        </div>
      </div>

      <button
        @click="handleSearch"
        class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
      >
        검색
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useLocationStore } from '@/stores/locationStore'
import { usePopulationStore } from '@/stores/populationStore'

const locationStore = useLocationStore()
const populationStore = usePopulationStore()
const searchKeyword = ref('')
const showResults = ref(false)

// 컴포넌트 마운트 시 법정동 코드 데이터 로드
onMounted(async () => {
  if (locationStore.locations.length === 0) {
    await locationStore.fetchLocationCodes()
  }
})

const handleInput = () => {
  locationStore.searchLocation(searchKeyword.value)
}

const selectLocation = (location) => {
  searchKeyword.value = location.fullName
  showResults.value = false
  populationStore.selectedLocation = {
    code: location.code,
    name: location.fullName,
  }
  populationStore.fetchPopulationData(location.code)
}

const handleSearch = () => {
  if (searchKeyword.value) {
    const location = locationStore.searchResults[0]
    if (location) {
      selectLocation(location)
    }
  }
}

// 외부 클릭 시 결과창 닫기
const handleClickOutside = (event) => {
  if (!event.target.closest('.search-container')) {
    showResults.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>
