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
      />
      <button class="search-button" @click="handleSearch">검색</button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useAddressStore } from '@/stores/addressStore'
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'

export default {
  name: 'HouseSearchBar',
  setup() {
    const addressStore = useAddressStore()
    const houseStore = useHouseStore()
    const searchQuery = ref('')
    const selectedSido = ref('')
    const selectedGugun = ref('')
    const selectedDong = ref('')

    const { sidos, guguns, dongs } = storeToRefs(addressStore)

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
        console.log('houseStore.markerPositions.value:', houseStore.markerPositions.value)
        // 좌표 업데이트 해야함.
      }
    }

    onMounted(() => {
      addressStore.getSido()
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
</style>
