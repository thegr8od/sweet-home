<template>
  <div class="house-detail-container" v-if="selectedHouse">
    <div class="detail-header">
      <h2 class="apt-name">{{ selectedHouse.aptName }}</h2>
      <p class="location">[{{ selectedHouse.legalDong }}]</p>
    </div>

    <div class="detail-content">
      <div class="info-section">
        <h3>아파트 정보</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="label">법정동</span>
            <span class="value">{{ selectedHouse.legalDong }}</span>
          </div>
          <div class="info-item">
            <span class="label">지번</span>
            <span class="value">{{ selectedHouse.jibun }}</span>
          </div>
          <!-- 추가 정보가 있다면 여기에 더 추가 -->
        </div>
      </div>
    </div>
  </div>
  <div v-else class="no-selection">
    <p>선택된 아파트가 없습니다.</p>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'
import { watch } from 'vue'

export default {
  name: 'HouseDetail',
  setup() {
    const houseStore = useHouseStore()
    const { selectedHouse } = storeToRefs(houseStore)

    // selectedHouse가 변경될 때마다 실행될 watch
    watch(selectedHouse, (newHouse) => {
      if (newHouse) {
        // 필요한 경우 여기에 추가 데이터 로딩 로직 추가
        console.log('Selected house changed:', newHouse)
      }
    })

    return {
      selectedHouse,
    }
  },
}
</script>

<style scoped>
.house-detail-container {
  padding: 24px;
  animation: fadeIn 0.3s ease;
}

.detail-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

.apt-name {
  font-size: 24px;
  margin: 0 0 8px 0;
  color: #333;
}

.location {
  font-size: 16px;
  color: #666;
  margin: 0;
}

.detail-content {
  padding: 16px 0;
}

.info-section {
  margin-bottom: 24px;
}

.info-section h3 {
  font-size: 18px;
  margin: 0 0 16px 0;
  color: #333;
}

.info-grid {
  display: grid;
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.label {
  font-size: 14px;
  color: #666;
  width: 80px;
}

.value {
  font-size: 14px;
  color: #333;
  flex: 1;
}

.no-selection {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #666;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
