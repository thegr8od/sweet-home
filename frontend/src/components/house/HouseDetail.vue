<template>
  <div class="house-detail detail-list-div type1">
    <div class="apt-image">
      <img :src="randomImage" alt="아파트 이미지" />
      <div class="gradient-overlay"></div>
    </div>

    <div class="detail-header">
      <h2 class="apt-name">{{ selectedHouse?.aptName }}</h2>
      <p class="apt-location">{{ selectedHouse?.legalDong }}</p>
    </div>

    <div class="detail-content" v-if="aptInfo">
      <div class="info-section">
        <h3>기본 정보</h3>
        <div class="info-grid">
          <div class="info-item">
            <span class="label">건축년도</span>
            <span class="value">{{ aptInfo.buildYear }}년</span>
          </div>
          <div class="info-item">
            <span class="label">도로명 주소</span>
            <span class="value">
              {{ aptInfo.roadNm }}
              {{ aptInfo.roadNmBonbun }}
              {{ aptInfo.roadNmBubun ? `-${aptInfo.roadNmBubun}` : '' }}
            </span>
          </div>
        </div>
      </div>

      <div class="deal-section">
        <DealTable />
      </div>
    </div>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'
import DealTable from './detail/DealTable.vue'

// 이미지 import
import apt1 from '@/assets/img/apt/apt1.jpg'
import apt2 from '@/assets/img/apt/apt2.jpg'
import apt3 from '@/assets/img/apt/apt3.jpg'
import apt4 from '@/assets/img/apt/apt4.jpg'
import apt5 from '@/assets/img/apt/apt5.jpg'
import apt6 from '@/assets/img/apt/apt6.jpg'
import apt7 from '@/assets/img/apt/apt7.jpg'
import apt8 from '@/assets/img/apt/apt8.jpg'
import apt9 from '@/assets/img/apt/apt9.jpg'
import apt10 from '@/assets/img/apt/apt10.jpg'
import apt11 from '@/assets/img/apt/apt11.jpg'
import apt12 from '@/assets/img/apt/apt12.jpg'
import apt13 from '@/assets/img/apt/apt13.jpg'
import apt14 from '@/assets/img/apt/apt14.jpg'

export default {
  name: 'HouseDetail',
  components: {
    DealTable,
  },
  setup() {
    const houseStore = useHouseStore()
    const { selectedHouse, aptInfo } = storeToRefs(houseStore)
    const randomImage = ref('')

    const images = [
      apt1,
      apt2,
      apt3,
      apt4,
      apt5,
      apt6,
      apt7,
      apt8,
      apt9,
      apt10,
      apt11,
      apt12,
      apt13,
      apt14,
    ]

    // selectedHouse가 변경될 때마다 새로운 랜덤 이미지 선택
    watch(
      selectedHouse,
      () => {
        const randomIndex = Math.floor(Math.random() * images.length)
        randomImage.value = images[randomIndex]
      },
      { immediate: true },
    ) // immediate: true로 설정하여 초기 로드시에도 실행

    return {
      selectedHouse,
      aptInfo,
      randomImage,
    }
  },
}
</script>

<style scoped>
.house-detail {
  padding: 0;
}

.detail-list-div {
  overflow-x: hidden;
  overflow-y: auto;
  /* height: 100%; */
  height: calc(100vh - 60px);
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

.apt-image {
  width: 100%;
  height: 300px;
  overflow: hidden;
  margin-bottom: 20px;
  position: relative;
}

.apt-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.gradient-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    to bottom,
    rgba(0, 0, 0, 0) 0%,
    rgba(0, 0, 0, 0.2) 50%,
    rgba(0, 0, 0, 0.5) 100%
  );
  pointer-events: none;
}

.detail-header {
  /* margin-bottom: 12px; */
  padding: 0 20px 16px 20px;
  border-bottom: 1px solid #eee;
  text-align: left;
}

.apt-name {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #333;
}

.apt-location {
  font-size: 16px;
  color: #666;
  margin: 0;
}

.info-section {
  background-color: white;
  padding: 20px;
  margin: 0;
}

.info-section h3 {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 16px 0;
  color: #333;
}

.info-grid {
  display: grid;
  gap: 16px;
}

.info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-start;
}

.label {
  font-size: 14px;
  color: #666;
}

.value {
  font-size: 16px;
  color: #333;
}

.deal-section {
  background-color: white;
}

.detail-content {
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
</style>
