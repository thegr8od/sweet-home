<template>
  <div class="house-detail detail-list-div type1">
    <div v-if="!houseStore.showCommentList">
      <div class="apt-image">
        <img :src="randomImage" alt="아파트 이미지" />
        <div class="gradient-overlay"></div>
      </div>

      <div class="detail-header">
        <div class="title-area">
          <h2 class="apt-name box-sizing m-0">{{ selectedHouse?.aptName }}</h2>
          <button
            @click="toggleInterest"
            class="interest-button"
            :class="{ 'is-interested': isInterested }"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              :fill="isInterested ? 'currentColor' : 'none'"
              stroke="currentColor"
              class="heart-icon"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z"
              />
            </svg>
          </button>
        </div>

        <p class="apt-location">{{ selectedHouse?.legalDong }}</p>
      </div>

      <div class="detail-content" v-if="aptInfo">
        <div class="info-section">
          <div class="info-header">
            <h3>기본 정보</h3>
          </div>
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

        <PurchaseRecommendation :deal-history="aptDeals" :sgg-cd="selectedHouse?.sggCd" />

        <div class="chart-section">
          <div class="chart-header">
            <h3>실거래가 추이</h3>
            <select v-model="selectedArea" class="area-select" @change="updateChart">
              <option value="all">전체 면적</option>
              <option v-for="area in uniqueAreas" :key="area" :value="area">{{ area }}㎡</option>
            </select>
          </div>
          <canvas ref="priceChart"></canvas>
        </div>

        <div class="deal-section">
          <DealTable />
        </div>

        <div class="board-section">
          <div class="board-header">
            <h3>단지글</h3>
          </div>
          <HouseComment v-if="firstBoard" :board="firstBoard" :is-first="true" />
          <div v-else class="flex flex-col items-center p-4">
            <div class="text-gray-500 mt-5 mb-12">작성된 게시글이 없습니다.</div>
            <button
              @click="handleWriteClick"
              class="w-full py-3 text-sm text-blue-600 hover:text-blue-700 hover:bg-blue-50 transition-colors rounded-md border border-blue-200"
            >
              <span class="flex items-center justify-center">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4 mr-1"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M12 4v16m8-8H4"
                  />
                </svg>
                첫 게시글 작성하기
              </span>
            </button>
          </div>
          <div v-if="aptBoardList && aptBoardList.length > 0" class="view-all-button">
            <button
              @click="houseStore.showComments()"
              class="w-full py-3 text-sm text-gray-500 hover:text-gray-700 hover:bg-gray-50 transition-colors"
            >
              단지글 전체보기
            </button>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="comment-overlay">
      <HouseCommentList />
    </div>
  </div>
</template>

<script>
import { useHouseStore } from '@/stores/houseStore'
import { useInterestStore } from '@/stores/interestStore'
import { useUserStore } from '@/stores/user'
import { storeToRefs } from 'pinia'
import { ref, watch, computed, nextTick } from 'vue'
import DealTable from './detail/DealTable.vue'
import HouseComment from './board/HouseComment.vue'
import HouseCommentList from './board/HouseCommentList.vue'
import Chart from 'chart.js/auto'
import { useAptBoardStore } from '@/stores/aptBoardStore'
import PurchaseRecommendation from './detail/PurchaseRecommendation.vue'
import { useRouter } from 'vue-router'

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
    HouseComment,
    HouseCommentList,
    PurchaseRecommendation,
  },
  setup() {
    const houseStore = useHouseStore()
    const aptBoardStore = useAptBoardStore()
    const userStore = useUserStore()
    const interestStore = useInterestStore()
    const router = useRouter()

    const { selectedHouse, aptInfo, aptDeals } = storeToRefs(houseStore)
    const randomImage = ref('')
    const priceChart = ref(null)
    let chart = null
    const selectedArea = ref('all')
    const isLoading = ref(false)

    // 관심 등록 여부 확인 computed 추가
    const isInterested = computed(() => {
      return selectedHouse.value && interestStore.isInterested(selectedHouse.value.aptSeq)
    })

    // 관심 등록/해제 토글 함수 추가
    const toggleInterest = async () => {
      if (!userStore.isLoggedIn) {
        alert('로그인이 필요한 서비스입니다.')
        return
      }

      if (!selectedHouse.value?.aptSeq) return

      isLoading.value = true
      try {
        if (isInterested.value) {
          await interestStore.removeFromInterest(selectedHouse.value.aptSeq)
        } else {
          await interestStore.addToInterest(selectedHouse.value.aptSeq)
        }
      } catch (error) {
        console.error('관심 아파트 처리 실패:', error)
        alert(error.message || '처리 중 오류가 발생했습니다.')
      } finally {
        isLoading.value = false
      }
    }

    // 기존의 이미지 관련 코드
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

    watch(
      selectedHouse,
      () => {
        const randomIndex = Math.floor(Math.random() * images.length)
        randomImage.value = images[randomIndex]
      },
      { immediate: true },
    )

    // 고유한 전용면적 목록 계산
    const uniqueAreas = computed(() => {
      if (!aptDeals.value || aptDeals.value.length === 0) return []
      const areas = [...new Set(aptDeals.value.map((deal) => deal.excluUseAr))]
      return areas.filter((area) => area).sort((a, b) => a - b)
    })

    // 첫 번째 게시글을 가져오는 computed 속성
    const firstBoard = computed(() => {
      return aptBoardStore.boards[0] || null
    })

    // 차트 관련 함수들 유지
    const prepareChartData = (deals) => {
      if (!deals || deals.length === 0) return null

      // 선택된 면적에 따라 데이터 필터링
      let filteredDeals =
        selectedArea.value === 'all'
          ? deals
          : deals.filter((deal) => deal.excluUseAr === selectedArea.value)

      if (filteredDeals.length === 0) {
        selectedArea.value = 'all' // 필터링된 결과가 없으면 전체로 변경
        filteredDeals = deals
      }

      const sortedDeals = [...filteredDeals].sort((a, b) => {
        const dateA = new Date(a.dealYear, a.dealMonth - 1, a.dealDay)
        const dateB = new Date(b.dealYear, b.dealMonth - 1, b.dealDay)
        return dateA - dateB
      })

      return {
        labels: sortedDeals.map(
          (deal) => `${deal.dealYear}.${String(deal.dealMonth).padStart(2, '0')}`,
        ),
        datasets: [
          {
            label:
              selectedArea.value === 'all' ? '전체 실거래가' : `${selectedArea.value}㎡ 실거래가`,
            data: sortedDeals.map((deal) => {
              const amount = deal.tradeAmount.replace(/,/g, '')
              return parseInt(amount)
            }),
            borderColor: '#3b82f6',
            backgroundColor: 'rgba(59, 130, 246, 0.1)',
            tension: 0.2,
            borderWidth: 1.5,
            fill: true,
          },
        ],
      }
    }

    const updateChart = () => {
      if (chart) {
        chart.destroy()
      }

      if (!priceChart.value || !aptDeals.value || aptDeals.value.length === 0) return

      const chartData = prepareChartData(aptDeals.value)
      if (!chartData) return

      const ctx = priceChart.value.getContext('2d')
      chart = new Chart(ctx, {
        type: 'line',
        data: chartData,
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              display: false,
            },
            tooltip: {
              titleFont: {
                family: 'Spoqa Han Sans Neo',
                size: 11,
              },
              bodyFont: {
                family: 'Spoqa Han Sans Neo',
                size: 11,
              },
              padding: 8,
              callbacks: {
                label: function (context) {
                  const value = context.raw
                  if (value >= 10000) {
                    const uk = Math.floor(value / 10000)
                    const rest = value % 10000
                    return rest > 0 ? `${uk}억 ${rest}만원` : `${uk}억원`
                  }
                  return `${value}만원`
                },
              },
            },
          },
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                font: {
                  family: 'Spoqa Han Sans Neo',
                  size: 10,
                },
                callback: function (value) {
                  if (value >= 10000) {
                    return `${Math.floor(value / 10000)}억`
                  }
                  return `${value}만`
                },
              },
            },
            x: {
              ticks: {
                font: {
                  family: 'Spoqa Han Sans Neo',
                  size: 10,
                },
              },
            },
          },
          elements: {
            line: {
              borderWidth: 1.5,
              tension: 0.2,
            },
            point: {
              radius: 2,
              hoverRadius: 4,
            },
          },
        },
      })
    }

    // 차트 관련 watch 유지
    watch(
      [aptDeals, selectedArea, () => houseStore.showCommentList],
      ([newDeals, newArea, showComments]) => {
        if (newDeals && newDeals.length > 0) {
          nextTick(() => {
            if (priceChart.value && !showComments) {
              if (newArea !== 'all' && !uniqueAreas.value.includes(newArea)) {
                selectedArea.value = 'all'
              }
              updateChart()
            }
          })
        }
      },
      { immediate: true, deep: true },
    )

    // selectedHouse가 변경될 때 게시글 목록을 가져오는 watch
    watch(
      () => selectedHouse.value?.aptSeq,
      async (newAptSeq) => {
        if (newAptSeq) {
          await aptBoardStore.fetchBoardsByAptSeq(newAptSeq)
        } else {
          aptBoardStore.clearBoards()
        }
      },
      { immediate: true },
    )

    const handleWriteClick = () => {
      if (!userStore.isLoggedIn) {
        // 로그인되지 않은 경우
        alert('로그인이 필요한 서비스입니다.')
        router.push({
          name: 'login',
          query: { redirect: router.currentRoute.value.fullPath }, // 현재 페이지 경로를 저장
        })
      } else {
        // 로그인된 경우
        houseStore.showCommentWritingPanel()
      }
    }

    return {
      selectedHouse,
      aptInfo,
      aptDeals,
      randomImage,
      priceChart,
      selectedArea,
      uniqueAreas,
      updateChart,
      houseStore,
      firstBoard,
      userStore,
      isInterested,
      toggleInterest,
      isLoading,
      handleWriteClick,
    }
  },
}
</script>

<style scoped>
/* 기존 스타일에 추가 */
.interest-button {
  background: none;
  border: none;
  padding: 8px;
  cursor: pointer;
  color: #94a3b8;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.interest-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.interest-button:not(:disabled):hover {
  color: #ef4444;
}

.interest-button.is-interested {
  color: #ef4444;
}

.heart-icon {
  width: 24px;
  height: 24px;
  stroke-width: 1.5;
}

.interest-button:not(.is-interested):hover .heart-icon {
  fill: rgba(239, 68, 68, 0.1);
}
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
  /* margin-bottom: 20px; */
}

.detail-content {
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-bottom: 20px;
}

.chart-section {
  background-color: white;
  padding: 20px;
  margin: 0;
}

.chart-section h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.title-area {
  display: flex;
  align-items: center;
  justify-content: space-between;
  /* gap: 12px; */
}

.area-select {
  padding: 4px 8px;
  font-size: 13px;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  color: #64748b;
  background-color: white;
  cursor: pointer;
  outline: none;
  transition: all 0.2s;
  height: 28px;
  min-width: 100px;
}

.area-select:hover {
  border-color: #94a3b8;
}

.area-select:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.1);
}

canvas {
  width: 100% !important;
  height: 280px !important;
}

.board-section {
  background-color: white;
  margin: 0;
}

.board-header {
  padding: 20px 20px 16px 20px;
}

.board-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.view-all-button {
  border-top: 1px solid #eee;
}

.interaction-button {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 14px;
  color: #64748b;
  padding: 6px;
  transition: all 0.2s ease;
}

.interaction-button:hover {
  color: #3b82f6;
}

.interaction-button svg {
  transition: all 0.2s ease;
}

.interaction-button:hover svg {
  color: #3b82f6;
}

.comment-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: white;
  z-index: 10;
  height: 100%;
}

/* 매수 추천 분석 섹션 스타일 추가 */
.purchase-recommendation {
  background-color: white;
  margin: 0;
}
</style>
