<template>
  <div class="house-view-container">
    <div class="left-panel">
      <div class="search-div">
        <house-search-bar />
        <div class="list-div type1">
          <house-list class="house-list" />
        </div>
      </div>
      <Transition name="slide">
        <div v-if="showDetail" class="detail-div">
          <button class="close-button" @click="closeDetail">×</button>
          <house-detail />
        </div>
      </Transition>
    </div>
    <div class="map-container">
      <DefaultMap class="kakao-map"></DefaultMap>
    </div>
  </div>
</template>

<script>
import HouseSearchBar from '@/components/house/HouseSearchBar.vue'
import HouseList from '@/components/house/HouseList.vue'
import DefaultMap from '@/components/map/DefaultMap.vue'
import HouseDetail from '@/components/house/HouseDetail.vue'
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'

export default {
  name: 'HouseView',
  components: {
    HouseSearchBar,
    HouseList,
    DefaultMap,
    HouseDetail,
  },
  setup() {
    const houseStore = useHouseStore()
    const { showDetail } = storeToRefs(houseStore)

    const closeDetail = () => {
      houseStore.closeDetail()
    }

    return {
      showDetail,
      closeDetail,
    }
  },
}
</script>

<style>
/* Spoqa Han Sans Neo 폰트 import */
@import url(//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css);

/* 기본 스타일 */
* {
  font-family: 'Spoqa Han Sans Neo', 'sans-serif';
  font-weight: normal;
}

.house-view-container {
  display: flex;
  width: 100%;
  height: 100vh;
}

.left-panel {
  display: flex;
  height: 94vh;
  margin-top: 54px;
  position: relative;
  z-index: 2;
}

.search-div {
  width: 420px;
  height: 100%;
  background-color: #ffffff;
  border-right: 1px solid rgba(0, 0, 0, 0.08);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
}

.map-container {
  flex: 1;
  height: 100vh;
}

.kakao-map {
  width: 100%;
  height: 100%;
}

.underline-orange {
  display: inline-block;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0) 70%, rgba(231, 149, 27, 0.3) 30%);
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
  height: calc(100% - 180px);
  overflow-y: auto;
  overflow-x: hidden;
}

.house-search-bar {
  z-index: 3;
  width: 100%;
  padding: 0 20px;
}

.house-list {
  width: 100%;
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

.detail-div {
  width: 400px;
  height: 100%;
  background-color: #ffffff;
  border-right: 1px solid rgba(0, 0, 0, 0.08);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  overflow-y: auto;
}

.close-button {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 32px;
  height: 32px;
  border: none;
  background: none;
  font-size: 24px;
  color: #666;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s ease;
}

.close-button:hover {
  background-color: #f5f5f5;
  color: #333;
}

/* 슬라이드 애니메이션 수정 */
.slide-enter-active,
.slide-leave-active {
  transition: width 0.3s ease;
  overflow: hidden;
}

.slide-enter-from,
.slide-leave-to {
  width: 0;
}

/* 반응형 스타일 수정 */
@media (max-width: 1200px) {
  .search-div {
    width: 360px;
  }
  .detail-div {
    width: 360px;
  }
}

@media (max-width: 768px) {
  .house-view-container {
    flex-direction: column;
  }

  .left-panel {
    flex-direction: column;
    height: 50vh;
    margin-top: 0;
    position: fixed;
    bottom: 0;
    width: 100%;
    z-index: 2;
  }

  .search-div {
    width: 100%;
    height: 100%;
  }

  .detail-div {
    position: absolute;
    bottom: 0;
    width: 100%;
    height: 50vh;
    border-top: 1px solid rgba(0, 0, 0, 0.08);
    border-right: none;
  }

  .map-container {
    height: 50vh;
  }

  .list-div {
    height: calc(100% - 180px);
  }

  /* 모바일에서는 아래에서 위로 슬라이드 */
  .slide-enter-active,
  .slide-leave-active {
    transition: height 0.3s ease;
  }

  .slide-enter-from,
  .slide-leave-to {
    height: 0;
  }
}
</style>
