<template>
  <div class="bg-white rounded-lg shadow">
    <div class="p-5 flex justify-between items-baseline">
      <h3 class="interest-title">관심 아파트 목록</h3>
      <button
        @click="toggleList"
        class="fold-button text-gray-500 text-sm flex items-center gap-1 hover:text-gray-700"
      >
        {{ isOpen ? '접기' : '펼치기' }}
        <component :is="isOpen ? ChevronUpIcon : ChevronDownIcon" class="w-4 h-4" />
      </button>
    </div>

    <div v-if="isOpen" class="interest-content">
      <div v-if="displayedInterests.length > 0">
        <interest-list-item 
          v-for="interest in displayedInterests" 
          :key="interest.aptSeq" 
          :interest="interest" 
        />
      </div>
      <div v-else class="no-interests">
        <p>등록된 관심 아파트가 없습니다.</p>
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
  </div>
</template>

<script>
import InterestListItem from './InterestListItem.vue'
import { useInterestStore } from '@/stores/interestStore'
import { storeToRefs } from 'pinia'
import { ref, computed } from 'vue'
import { ChevronDownIcon, ChevronUpIcon } from 'lucide-vue-next'

export default {
  name: 'InterestList',
  components: {
    InterestListItem,
    ChevronDownIcon,
    ChevronUpIcon
  },
  setup() {
    const interestStore = useInterestStore()
    const { interests } = storeToRefs(interestStore)
    const isOpen = ref(true)
    const itemsPerPage = 5
    const currentPage = ref(1)

    // 컴포넌트 마운트 시 관심 목록 가져오기
    interestStore.fetchInterestList()

    // 현재 페이지까지의 아이템만 표시
    const displayedInterests = computed(() => {
      return interests.value.slice(0, currentPage.value * itemsPerPage)
    })

    // 더 보여줄 아이템이 있는지 확인
    const hasMoreItems = computed(() => {
      return interests.value.length > currentPage.value * itemsPerPage
    })

    // 더보기 버튼 클릭 시 페이지 증가
    const loadMore = () => {
      currentPage.value++
    }

    // 목록 접기/펼치기 토글
    const toggleList = () => {
      isOpen.value = !isOpen.value
      if (isOpen.value) {
        currentPage.value = 1 // 펼칠 때 첫 페이지로 초기화
      }
    }

    return {
      interests,
      isOpen,
      displayedInterests,
      hasMoreItems,
      loadMore,
      toggleList,
      ChevronDownIcon,
      ChevronUpIcon
    }
  }
}
</script>

<style scoped>
.interest-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: #333;
  line-height: 1;
}

.interest-content {
  max-height: 400px;
  overflow-y: auto;
}

.no-interests {
  padding: 32px 16px;
  text-align: center;
  color: #666;
}

.fold-button {
  line-height: 1;
  padding: 0;
  transition: all 0.2s ease;
}

.fold-button:hover {
  transform: scale(1.05);
}

/* 스크롤바 스타일링 */
.interest-content::-webkit-scrollbar {
  width: 6px;
}

.interest-content::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.interest-content::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 3px;
}

.interest-content::-webkit-scrollbar-thumb:hover {
  background: #555;
}
</style> 