<template>
  <div style="width: 100%; height: 100%">
    <div class="position-relative">
      <main class="mt-0 main-content main-content-bg">
        <section>
          <DefaultMap class="kakao-map"></DefaultMap>
          <div>
            <div class="search-div">
              <div class="search-container">
                <!-- 시/도/구/군 선택기 추가 -->
                <div class="location-selector">
                  <select v-model="selectedSido" class="location-select">
                    <option value="">시/도 선택</option>
                    <option v-for="sido in sidoList" :key="sido" :value="sido">
                      {{ sido }}
                    </option>
                  </select>
                  <select v-model="selectedGugun" class="location-select">
                    <option value="">구/군 선택</option>
                    <option v-for="gugun in gugunList" :key="gugun" :value="gugun">
                      {{ gugun }}
                    </option>
                  </select>
                  <select v-model="selectedDong" class="location-select">
                    <option value="">동 선택</option>
                    <option v-for="dong in dongList" :key="dong" :value="dong">
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
                  />
                  <button class="search-button" @click="handleSearch">검색</button>
                </div>
              </div>
              <div class="list-div type1">
                <house-list class="house-list" />
              </div>
            </div>
            <div v-if="!none" class="detail-div type1">
              <house-detail class="house-detail" />
            </div>
          </div>
        </section>
      </main>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useHouseStore } from '@/stores/houseStore'
import HouseSearchBar from '@/components/house/HouseSearchBar.vue'
import HouseList from '@/components/house/HouseList.vue'
import HouseDetail from '@/components/house/HouseDetail.vue'
import DefaultMap from '@/components/map/DefaultMap.vue'

export default {
  name: 'HouseView',
  components: {
    HouseSearchBar,
    HouseList,
    HouseDetail,
    DefaultMap,
  },
  setup() {
    const houseStore = useHouseStore()
    const searchQuery = ref('')
    const selectedSido = ref('')
    const selectedGugun = ref('')
    const selectedDong = ref('')

    // 임시 데이터 - 실제로는 API나 스토어에서 가져와야 합니다
    const sidoList = ref(['서울특별시', '경기도', '인천광역시'])
    const gugunList = ref(['강남구', '서초구', '송파구'])
    const dongList = ref(['역삼동', '삼성동', '청담동'])

    // Computed values
    const { none, houses, markerPositions, house, detail1, detail2 } = houseStore

    // Methods
    const fetchHouseListByAddress = (address) => houseStore.getHouseListByAddress(address)
    const fetchHouseList = (dongName) => houseStore.getHouseList(dongName)
    const fetchHouseDetails = (house) => houseStore.detailHouse(house)

    const handleSearch = () => {
      if (selectedDong.value) {
        fetchHouseList(selectedDong.value)
      } else if (searchQuery.value) {
        fetchHouseListByAddress(searchQuery.value)
      }
    }

    return {
      none,
      houses,
      markerPositions,
      house,
      detail1,
      detail2,
      fetchHouseListByAddress,
      fetchHouseList,
      fetchHouseDetails,
      // 새로 추가된 반응형 변수들
      searchQuery,
      selectedSido,
      selectedGugun,
      selectedDong,
      sidoList,
      gugunList,
      dongList,
      handleSearch,
    }
  },
  onBeforeUnmount() {
    const houseStore = useHouseStore()
    houseStore.setNoneFalse(true)
  },
}
</script>

<style scoped>
/* 기본 스타일 */
.underline-orange {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(231, 149, 27, 0.3) 30%);
}

.search-div {
  position: absolute;
  width: 420px;
  height: 94vh;
  z-index: 2;
  top: 54px;
  left: 0;
  background-color: #ffffff;
  border-right: 1px solid rgba(0, 0, 0, 0.08);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

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

.list-div {
  position: relative;
  width: 100%;
  height: calc(94vh - 180px);
  overflow-y: auto;
  overflow-x: hidden;
  padding: 16px;
  background-color: #ffffff;
}

.detail-div {
  position: absolute;
  width: 401px;
  height: 94vh;
  z-index: 2;
  top: 54px;
  left: 420px;
  background-color: #ffffff;
  border-left: 1px solid rgba(0, 0, 0, 0.08);
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.05);
  overflow-y: auto;
}

.house-search-bar {
  z-index: 3;
  width: 100%;
  padding: 0 20px;
}

.house-list {
  width: 100%;
}

.kakao-map {
  height: 100vh;
  position: relative;
}

/* 스크롤바 스타일 */
.type1::-webkit-scrollbar {
  width: 8px;
}

.type1::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 4px;
}

.type1::-webkit-scrollbar-track {
  background-color: #f5f5f5;
  border-radius: 4px;
}

/* 카드 스타일 */
.house-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  border: 1px solid #eeeeee;
  transition: all 0.3s ease;
  cursor: pointer;
}

.house-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border-color: #e0e0e0;
}

/* 반응형 스타일 */
@media (max-width: 1200px) {
  .search-div {
    width: 360px;
  }

  .detail-div {
    left: 360px;
    width: 360px;
  }
}

@media (max-width: 768px) {
  .search-div {
    width: 100%;
    height: 50vh;
    top: 50vh;
  }

  .detail-div {
    display: none;
  }

  .kakao-map {
    height: 50vh;
  }

  .list-div {
    height: calc(50vh - 180px);
  }
}
</style>
