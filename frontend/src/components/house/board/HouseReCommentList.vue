<template>
  <div class="house-comment-list">
    <!-- 헤더 섹션 -->
    <div class="comment-header">
      <button class="back-button" @click="goBack">
        <ArrowLeftIcon class="w-5 h-5" />
      </button>
      <h2 class="header-title">게시글 상세</h2>
    </div>

    <!-- 컨텐츠 섹션 -->
    <div class="comment-content type1">
      <!-- 로딩 상태 -->
      <div v-if="isLoading" class="flex justify-center items-center p-4">
        <span>로딩 중...</span>
      </div>

      <!-- 에러 메시지 -->
      <div v-else-if="error" class="text-red-500 p-4">
        {{ error }}
      </div>

      <!-- 게시글 상세 -->
      <div v-else-if="currentBoard" class="comment-list">
        <!-- 원본 게시글 -->
        <HouseComment 
          :board="currentBoard" 
          :is-first="true" 
        />
        
        <!-- 댓글 목록 -->
        <div v-if="comments.length > 0">
          <HouseReComment
            v-for="comment in comments"
            :key="comment.id"
            :comment="comment"
            @like-updated="handleLikeUpdated"
          />
        </div>
        <div v-else class="p-4 text-center text-gray-500">
          작성된 댓글이 없습니다.
        </div>
      </div>

      <!-- 게시글이 없는 경우 -->
      <div v-else class="p-4 text-center text-gray-500">
        게시글을 찾을 수 없습니다.
      </div>
    </div>

    <!-- 댓글 입력바 -->
    <div class="comment-bar-wrapper" v-if="userStore.isLoggedIn">
      <CommentBar @comment-added="handleCommentAdded" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ArrowLeftIcon } from 'lucide-vue-next'
import { useHouseStore } from '@/stores/houseStore'
import { useAptBoardStore } from '@/stores/aptBoardStore'
import { useUserStore } from '@/stores/user'
import { getComments, createComment, likeComment, unlikeComment } from '@/api/aptBoard'
import HouseComment from './HouseComment.vue'
import HouseReComment from './HouseReComment.vue'
import CommentBar from '@/components/common/CommentBar.vue'

const houseStore = useHouseStore()
const aptBoardStore = useAptBoardStore()
const userStore = useUserStore()

// 로컬 상태 관리
const comments = ref([])
const isLoading = ref(false)
const error = ref(null)

// currentBoard를 computed로 변경
const currentBoard = computed(() => aptBoardStore.currentBoard)

const goBack = () => {
  houseStore.hideReComments()
}

// 댓글 목록 조회
const fetchComments = async () => {
  isLoading.value = true
  error.value = null
  
  try {
    const response = await getComments(
      houseStore.selectedBoardId,
      (response) => {
        comments.value = response.data
        console.log(comments.value)
      },
      (error) => {
        console.error('댓글 목록 조회 실패:', error)
        error.value = '댓글을 불러오는데 실패했습니다.'
      }
    )
  } catch (err) {
    console.error('댓글 조회 중 에러:', err)
    error.value = '댓글을 불러오는데 실패했습니다.'
  } finally {
    isLoading.value = false
  }
}

// 댓글 추가 시 처리
const handleCommentAdded = async (newComment) => {
  try {
    await aptBoardStore.createNewComment(houseStore.selectedBoardId, newComment)
    // 댓글 목록 새로고침
    fetchComments()
  } catch (error) {
    console.error('댓글 작성 중 에러:', error)
  }
}

// 좋아요 상태 업데이트 처리
const handleLikeUpdated = async (commentId, isLiked) => {
  try {
    if (isLiked) {
      await likeComment(commentId, userStore.userId)
    } else {
      await unlikeComment(commentId, userStore.userId)
    }
    // 댓글 목록 새로고침
    fetchComments()
  } catch (error) {
    console.error('좋아요 처리 중 에러:', error)
  }
}

// 컴포넌트 마운트 시 게시글 정보와 댓글 목록 조회
onMounted(async () => {
  const boardId = houseStore.selectedBoardId
  if (boardId) {
    await aptBoardStore.fetchBoardDetail(boardId)  // 게시글 상세 정보 조회
    await fetchComments()  // 댓글 목록 조회
  }
})
</script>

<style scoped>
.house-comment-list {
  position: relative;
  display: flex;
  flex-direction: column;
  height: 100%;
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
  flex: 1;
  overflow-y: auto;
  height: 100%;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 0px;
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

.comment-bar-wrapper {
  position: sticky;
  margin-top: 60px;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background-color: white;
  border-top: 1px solid #e5e7eb;
  z-index: 10;
}
</style>
