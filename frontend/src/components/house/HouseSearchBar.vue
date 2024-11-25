<template>
  <div class="search-container">
    <div class="location-selector">
      <select v-model="selectedSido" class="location-select" @change="getGugun">
        <option value="">시/도 선택</option>
        <option v-for="sido in sidos" :key="sido" :value="sido">
          {{ sido }}
        </option>
      </select>
      <select v-model="selectedGugun" class="location-select" @change="getDong">
        <option value="">구/군 선택</option>
        <option v-for="gugun in guguns" :key="gugun" :value="gugun">
          {{ gugun }}
        </option>
      </select>
      <select v-model="selectedDong" class="location-select" @change="getDeals">
        <option value="">동 선택</option>
        <option v-for="dong in dongs" :key="dong" :value="dong">
          {{ dong }}
        </option>
      </select>
    </div>
    <div class="search-box">
      <input
        type="text"
        class="search-input"
        placeholder="주소나 건물명을 입력하세요"
        v-model="searchQuery"
        @keyup.enter="handleSearch"
      />
      <button class="search-button" @click="handleSearch">검색</button>
    </div>
    <div class="popular-search" v-if="popularKeywords.length > 0">
      <div class="popular-title" @click="toggleExpand">
        실시간 인기 검색어
        <span class="live-dot"></span>
        <span class="toggle-icon">{{ isExpanded ? '▼' : '▶' }}</span>
      </div>
      <div v-if="!isExpanded" class="popular-single-row">
        <div class="popular-item-single" @click="handleKeywordClick(currentKeyword.keyword)">
          <span class="rank">{{ currentIndex + 1 }}</span>
          <div class="keyword-wrapper">
            <div class="keyword" :class="{ marquee: isLongText(currentKeyword.keyword) }">
              <span class="keyword-text">{{ currentKeyword.keyword }}</span>
            </div>
          </div>
          <span class="change-indicator" :class="getChangeClass(currentKeyword)">
            {{ getChangeStatus(currentKeyword) }}
          </span>
        </div>
      </div>
      <div v-else class="popular-columns">
        <div class="popular-column">
          <div
            v-for="(keyword, index) in leftKeywords"
            :key="keyword.keyword"
            class="popular-item"
            @click="handleKeywordClick(keyword.keyword)"
          >
            <span class="rank">{{ index + 1 }}</span>
            <div class="keyword-wrapper">
              <div class="keyword" :class="{ marquee: isLongText(keyword.keyword) }">
                <span class="keyword-text">{{ keyword.keyword }}</span>
                <span class="keyword-text" v-if="isLongText(keyword.keyword)">{{
                  keyword.keyword
                }}</span>
              </div>
            </div>
            <span class="change-indicator" :class="getChangeClass(keyword)">
              {{ getChangeStatus(keyword) }}
            </span>
          </div>
        </div>
        <div class="popular-column">
          <div
            v-for="(keyword, index) in rightKeywords"
            :key="keyword.keyword"
            class="popular-item"
            @click="handleKeywordClick(keyword.keyword)"
          >
            <span class="rank">{{ index + 4 }}</span>
            <div class="keyword-wrapper">
              <div class="keyword" :class="{ marquee: isLongText(keyword.keyword) }">
                <span class="keyword-text">{{ keyword.keyword }}</span>
                <span class="keyword-text" v-if="isLongText(keyword.keyword)">{{
                  keyword.keyword
                }}</span>
              </div>
            </div>
            <span class="change-indicator" :class="getChangeClass(keyword)">
              {{ getChangeStatus(keyword) }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useAddressStore } from '@/stores/addressStore'
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'
import { saveSearchLog, getPopularSearchKeywords } from '@/api/searchLog'

export default {
  name: 'HouseSearchBar',
  setup() {
    const addressStore = useAddressStore()
    const houseStore = useHouseStore()
    const searchQuery = ref('')
    const selectedSido = ref('')
    const selectedGugun = ref('')
    const selectedDong = ref('')
    const popularKeywords = ref([])
    const previousKeywords = ref([])
    const rankChanges = ref({})
    const isExpanded = ref(false)
    const currentIndex = ref(0)
    let rotateInterval = null

    const { sidos, guguns, dongs } = storeToRefs(addressStore)

    const leftKeywords = computed(() => popularKeywords.value.slice(0, 3))
    const rightKeywords = computed(() => popularKeywords.value.slice(3, 6))

    const currentKeyword = computed(() => {
      return popularKeywords.value[currentIndex.value] || { keyword: '' }
    })

    const rotateKeywords = () => {
      if (!isExpanded.value && popularKeywords.value.length > 0) {
        currentIndex.value = (currentIndex.value + 1) % popularKeywords.value.length
      }
    }

    const toggleExpand = () => {
      isExpanded.value = !isExpanded.value
      if (isExpanded.value) {
        clearInterval(rotateInterval)
      } else {
        startRotation()
      }
    }

    const startRotation = () => {
      rotateInterval = setInterval(rotateKeywords, 3000) // 3초마다 변경
    }

    // 순위 변동 상태 계산 및 저장
    const updateRankChanges = (newKeywords) => {
      if (previousKeywords.value.length === 0) {
        // 초기 로드시에는 변동 없음으로 설정
        newKeywords.forEach((keyword) => {
          rankChanges.value[keyword.keyword] = '-'
        })
        return
      }

      newKeywords.forEach((keyword, index) => {
        const currentRank = index + 1
        const prevKeyword = previousKeywords.value.find((k) => k.keyword === keyword.keyword)

        if (!prevKeyword) {
          rankChanges.value[keyword.keyword] = 'new'
        } else {
          const prevRank =
            previousKeywords.value.findIndex((k) => k.keyword === keyword.keyword) + 1
          if (currentRank < prevRank) {
            rankChanges.value[keyword.keyword] = '▲'
          } else if (currentRank > prevRank) {
            rankChanges.value[keyword.keyword] = '▼'
          }
          // 순위가 같으면 이전 상태 유지
        }
      })
    }

    const getChangeStatus = (keyword) => {
      return rankChanges.value[keyword.keyword] || '-'
    }

    const getChangeClass = (keyword) => {
      const status = rankChanges.value[keyword.keyword]
      if (status === '▲') return 'up'
      if (status === '▼') return 'down'
      if (status === 'new') return 'new'
      return ''
    }

    const getGugun = async () => {
      selectedGugun.value = ''
      selectedDong.value = ''
      if (selectedSido.value) {
        await addressStore.getGugun(selectedSido.value)
      }
    }

    const getDong = async () => {
      selectedDong.value = ''
      if (selectedGugun.value) {
        await addressStore.getDong(selectedSido.value, selectedGugun.value)
      }
    }

    const getDeals = async () => {
      if (selectedSido.value && selectedGugun.value && selectedDong.value) {
        const address = {
          sido: selectedSido.value,
          gugun: selectedGugun.value,
          dong: selectedDong.value,
        }
        console.log('address:', address)
        await houseStore.getHouseListByAddress(address)
      }
    }

    const fetchPopularKeywords = async () => {
      try {
        await getPopularSearchKeywords(
          6,
          (response) => {
            previousKeywords.value = [...popularKeywords.value]
            const newKeywords = response.data
            updateRankChanges(newKeywords)
            popularKeywords.value = newKeywords
          },
          (error) => {
            console.error('인기 검색어 조회 실패:', error)
          },
        )
      } catch (error) {
        console.error('인기 검색어 조회 실패:', error)
      }
    }

    const handleSearch = async () => {
      console.log('검색 시작:', searchQuery.value)
      if (!searchQuery.value.trim()) {
        return
      }

      try {
        await saveSearchLog(
          searchQuery.value,
          async () => {
            console.log('검색어 로그 저장 성공')
            await fetchPopularKeywords()
          },
          (error) => {
            console.error('검색어 로그 저장 실패:', error)
          },
        )

        await houseStore.getHouseListByAptName(searchQuery.value)
        console.log('검색 완료')
      } catch (error) {
        console.error('아파트 검색 실패:', error)
      }
    }

    const handleKeywordClick = async (keyword) => {
      searchQuery.value = keyword
      await handleSearch()
    }

    const isLongText = (text) => {
      if (isExpanded.value) {
        return text.length > 6 // 펼쳤을 때는 6자 이상이면 슬라이딩
      }
      return text.length > 12 // 접혔을 때는 12자 이상이면 슬라이딩
    }

    onMounted(() => {
      addressStore.getSido()
      fetchPopularKeywords()
      startRotation()
    })

    onUnmounted(() => {
      clearInterval(rotateInterval)
    })

    return {
      searchQuery,
      selectedSido,
      selectedGugun,
      selectedDong,
      sidos,
      guguns,
      dongs,
      getGugun,
      getDong,
      getDeals,
      handleSearch,
      popularKeywords,
      handleKeywordClick,
      leftKeywords,
      rightKeywords,
      getChangeStatus,
      getChangeClass,
      isLongText,
      isExpanded,
      currentKeyword,
      currentIndex,
      toggleExpand,
    }
  },
}
</script>

<style scoped>
.search-container {
  padding: 20px;
  background: linear-gradient(180deg, #ffffff 0%, #fafafa 100%);
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
}

.location-selector {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.location-select {
  flex: 1;
  appearance: none;
  padding: 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background: #ffffff;
  font-size: 14px;
  color: #333;
  transition: all 0.3s ease;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='24' height='24' viewBox='0 0 24 24' fill='none' stroke='%23666' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'%3E%3C/polyline%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 8px center;
  background-size: 16px;
  cursor: pointer;
}

.location-select:hover {
  border-color: #2196f3;
}

.location-select:focus {
  border-color: #2196f3;
  box-shadow: 0 0 0 3px rgba(33, 150, 243, 0.1);
  outline: none;
}

.search-box {
  position: relative;
  margin-bottom: 16px;
}

.search-input {
  width: 100%;
  padding: 12px 48px 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s ease;
  background: #ffffff;
}

.search-input:focus {
  border-color: #2196f3;
  box-shadow: 0 0 0 3px rgba(33, 150, 243, 0.1);
  outline: none;
}

.search-button {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 8px 12px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-button:hover {
  background-color: #1976d2;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.popular-search {
  margin-top: 16px;
  padding: 12px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.popular-columns {
  display: flex;
  gap: 24px;
}

.popular-column {
  flex: 1;
  width: 50%;
}

.popular-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  margin-bottom: 8px;
}

.rank {
  flex: 0 0 24px;
  font-weight: 600;
  color: #1971c2;
}

.keyword-wrapper {
  flex: 1;
  overflow: hidden;
  margin: 0 8px;
  position: relative;
}

.keyword {
  display: inline-flex;
  white-space: nowrap;
}

.keyword.marquee {
  animation: marquee 12s linear infinite;
}

.keyword.marquee:hover {
  animation-play-state: paused;
}

.keyword-text {
  padding-right: 24px;
}

.change-indicator {
  flex: 0 0 20px;
  text-align: center;
  font-size: 12px;
}

@keyframes marquee {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-50%);
  }
}

.change-indicator.up {
  color: #f03e3e;
}
.change-indicator.down {
  color: #1c7ed6;
}
.change-indicator.new {
  color: #37b24d;
}

.live-dot {
  width: 6px;
  height: 6px;
  background-color: #ff4b4b;
  border-radius: 50%;
  margin-left: 8px;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(0.95);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.1);
    opacity: 1;
  }
  100% {
    transform: scale(0.95);
    opacity: 0.5;
  }
}

.popular-title {
  display: flex;
  align-items: center;
  padding: 12px;
  cursor: pointer;
  user-select: none;
}

.toggle-icon {
  margin-left: auto;
  font-size: 12px;
  color: #868e96;
}

.popular-single-row {
  padding: 8px 12px;
}

.popular-item-single {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
}

.popular-columns {
  display: flex;
  gap: 24px;
  transition: all 0.3s ease;
}

.toggle-icon {
  margin-left: auto;
  font-size: 12px;
  color: #868e96;
}
</style>
