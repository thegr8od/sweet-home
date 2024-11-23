<template>
  <div class="house-view-container">
    <div class="left-panel">
      <div class="search-div">
        <house-search-bar />
        <div class="list-div type1">
          <house-list class="house-list" />
        </div>
      </div>
      <transition name="slide" @after-enter="afterEnter">
        <div v-if="showDetail" class="detail-panel">
          <house-detail v-if="!showCommentList && !showReCommentList && !showCommentWriting" />
          <house-comment-list v-else-if="showCommentList" />
          <house-comment-writing v-else-if="showCommentWriting" />
          <house-re-comment-list v-else />
          <button class="close-button" @click="closeDetail">
            <span>&times;</span>
          </button>
        </div>
      </transition>
    </div>
    <div class="map-container" :class="{ 'map-shifted': showDetail }">
      <DefaultMap class="kakao-map" ref="mapRef"></DefaultMap>
    </div>
  </div>
</template>

<script>
import HouseSearchBar from '@/components/house/HouseSearchBar.vue'
import HouseList from '@/components/house/HouseList.vue'
import HouseDetail from '@/components/house/HouseDetail.vue'
import HouseCommentList from '@/components/house/board/HouseCommentList.vue'
import HouseReCommentList from '@/components/house/board/HouseReCommentList.vue'
import HouseCommentWriting from '@/components/house/board/HouseCommentWriting.vue'
import DefaultMap from '@/components/map/DefaultMap.vue'
import { useHouseStore } from '@/stores/houseStore'
import { storeToRefs } from 'pinia'
import { ref } from 'vue'

export default {
  name: 'HouseView',
  components: {
    HouseSearchBar,
    HouseList,
    HouseDetail,
    HouseCommentList,
    HouseReCommentList,
    HouseCommentWriting,
    DefaultMap,
  },
  setup() {
    const houseStore = useHouseStore()
    const { showDetail, selectedPosition, showCommentList, showReCommentList, showCommentWriting } =
      storeToRefs(houseStore)
    const mapRef = ref(null)

    const closeDetail = () => {
      houseStore.closeDetail()
      if (mapRef.value && selectedPosition.value) {
        mapRef.value.setCenter(selectedPosition.value)
      }
    }

    // 상세 패널이 열린 후 맵 중심을 재설정
    const afterEnter = () => {
      if (mapRef.value && selectedPosition.value) {
        mapRef.value.setCenter(selectedPosition.value)
      }
    }

    return {
      showDetail,
      showCommentList,
      showReCommentList,
      showCommentWriting,
      closeDetail,
      mapRef,
      afterEnter,
    }
  },
}
</script>

<style>
/* Spoqa Han Sans Neo 폰트 import */
@import url(//spoqa.github.io/spoqa-han-sans/css/SpoqaHanSansNeo.css);

* {
  font-family: 'Spoqa Han Sans Neo', 'sans-serif';
}

.house-view-container {
  display: flex;
  width: 100%;
  height: 100%;
}

.left-panel {
  display: flex;
  height: 100%;
  width: auto;
  position: relative;
  z-index: 5;
}

.search-div {
  width: 420px;
  height: 100%;
  background-color: #ffffff;
  border-right: 1px solid rgba(0, 0, 0, 0.08);
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  position: relative;
  z-index: 5;
}

.map-container {
  flex: 1;
  height: 100%;
}

.kakao-map {
  width: 100%;
  height: 100%;
}

.list-div {
  height: calc(100% - 180px);
  overflow-y: auto;
  overflow-x: hidden;
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

.detail-panel {
  position: fixed;
  top: 60px;
  left: 420px;
  width: 400px;
  height: 100%;
  background: white;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  z-index: 2;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  padding: 5px 10px;
  color: #666;
  transition: color 0.3s ease;
}

.close-button:hover {
  color: #000;
}

/* 슬라이드 애니메이션 */
.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(-50%);
  opacity: 0;
}

/* 반응형 스타일 */
@media (max-width: 1200px) {
  .search-div {
    width: 360px;
  }

  .detail-panel {
    left: 360px;
    width: 360px;
  }
}

@media (max-width: 768px) {
  .house-view-container {
    flex-direction: column;
  }

  .left-panel {
    flex-direction: column;
    bottom: 0;
    width: 100%;
  }

  .search-div {
    width: 100%;
    height: 100%;
  }

  .map-container {
    height: 50%;
  }

  .detail-panel {
    position: fixed;
    top: 50%;
    left: 0;
    width: 100%;
    height: 50%;
  }

  .slide-enter-from,
  .slide-leave-to {
    transform: translateY(100%);
  }
}
</style>
