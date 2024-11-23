<template>
  <div class="house-comment-writing detail-list-div type1">
    <!-- 헤더 섹션 -->
    <div class="comment-header">
      <button class="back-button" @click="houseStore.hideCommentWritingPanel">
        <ArrowLeftIcon class="w-5 h-5" />
      </button>
      <h2 class="header-title">글쓰기</h2>
    </div>

    <!-- 컨텐츠 섹션 -->
    <div class="comment-content">
      <div class="bg-white p-4">
        <!-- Header -->
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-lg font-bold">{{ houseStore.selectedHouse?.aptName }}</h2>
          <button 
            class="text-blue-500 disabled:text-gray-400" 
            @click="submitBoard"
            :disabled="!isValidForm || !userStore.isLoggedIn"
          >
            등록
          </button>
        </div>

        <!-- Title Input -->
        <div class="mb-4 relative">
          <input
            v-model="title"
            type="text"
            placeholder="제목을 입력해주세요"
            class="w-full px-4 py-2 pr-10 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            maxlength="100"
          />
          <button
            v-if="title"
            @click="clearTitle"
            class="absolute right-3 top-1/2 -translate-y-1/2 w-5 h-5 flex items-center justify-center rounded-full bg-gray-200 hover:bg-gray-300 transition-colors"
          >
            <XIcon class="w-3.5 h-3.5 text-gray-600 stroke-2" />
          </button>
        </div>

        <!-- Info Box -->
        <div class="bg-blue-50 p-4 rounded-lg mb-4">
          <h3 class="text-sm font-semibold text-blue-700 mb-2">잠깐! 이런 글은 작성할 수 없어요</h3>
          <ul class="text-xs text-blue-700 list-disc pl-5">
            <li>단어, 계정 및 사진 도용한 글</li>
            <li>단지와 무관한 광고성 글</li>
            <li>동일한 내용의 반복적인 글</li>
            <li>단시간에 지나치게 많은 도배성 글</li>
          </ul>
        </div>

        <!-- Text Input -->
        <div class="mb-4">
          <textarea
            v-model="content"
            placeholder="단지에 관련한 글을 여기에 작성해주세요"
            class="w-full px-4 py-2 border-b focus:outline-none focus:border-b-2 focus:border-b-blue-500 resize-none"
            rows="15"
            maxlength="3000"
          ></textarea>
        </div>

        <!-- Character Count -->
        <div class="text-right text-xs text-gray-500 mb-4">{{ characterCount }}/3000자</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useHouseStore } from '@/stores/houseStore'
import { useAptBoardStore } from '@/stores/aptBoardStore'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { createBoard } from '@/api/aptBoard'
import { ArrowLeftIcon, XIcon } from 'lucide-vue-next'

const router = useRouter()
const houseStore = useHouseStore()
const aptBoardStore = useAptBoardStore()
const userStore = useUserStore()

const content = ref('')
const title = ref('')

const characterCount = computed(() => content.value.length)
const isValidForm = computed(() => title.value.trim() && content.value.trim())

const clearTitle = () => {
  title.value = ''
}

const submitBoard = async () => {
  if (!isValidForm.value) return
  
  if (!userStore.isLoggedIn) {
    alert('로그인이 필요한 서비스입니다.')
    router.push('/login')
    return
  }

  const newBoard = {
    title: title.value,
    content: content.value,
    aptSeq: houseStore.selectedHouse.aptSeq,
    aptName: houseStore.selectedHouse.aptName
  }

  try {
    await aptBoardStore.createNewBoard(newBoard)
    houseStore.hideCommentWritingPanel()
  } catch (error) {
    console.error('게시글 작성 중 에러:', error)
    if (error.response?.status === 401) {
      alert('로그인이 필요한 서비스입니다.')
      router.push('/login')
    } else {
      alert('게시글 작성에 실패했습니다.')
    }
  }
}
</script>

<style scoped>
.house-comment-writing {
  width: 100%;
  max-width: 400px;
  height: 100%;
  background-color: white;
  box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
  z-index: 10000;
  overflow-y: auto;
}

.comment-header {
  position: sticky;
  top: 0;
  display: flex;
  align-items: center;
  padding: 16px;
  background-color: white;
  border-bottom: 1px solid #e5e7eb;
  z-index: 10;
}

.back-button {
  padding: 8px;
  margin-right: 8px;
  color: #64748b;
  transition: all 0.2s;
}

.back-button:hover {
  color: #3b82f6;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.comment-content {
  padding: 16px;
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

textarea {
  min-height: 150px;
  line-height: 1.5;
  font-size: 15px;
  border-top: none;
  border-left: none;
  border-right: none;
  border-bottom: 1px solid #e5e7eb;
  transition: border-color 0.2s ease;
}

textarea:focus {
  border-bottom: 2px solid #3b82f6;
}

/* 스크롤바 스타일 */
textarea::-webkit-scrollbar {
  width: 8px;
}

textarea::-webkit-scrollbar-thumb {
  background-color: #bdbdbd;
  border-radius: 4px;
}

textarea::-webkit-scrollbar-track {
  background-color: #f5f5f5;
  border-radius: 4px;
}
</style>
