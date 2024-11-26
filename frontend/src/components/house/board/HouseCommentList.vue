<template>
  <div v-if="!houseStore.showCommentWriting" class="house-comment-list">
    <!-- 헤더 섹션 -->
    <div class="comment-header">
      <button class="back-button" @click="goBack">
        <ArrowLeftIcon class="w-5 h-5" />
      </button>
      <h2 class="header-title">단지글 목록</h2>
    </div>

    <!-- 로딩 상태 -->
    <div v-if="aptBoardStore.isLoading" class="flex justify-center items-center p-4">
      <span>로딩 중...</span>
    </div>

    <!-- 에러 메시지 -->
    <div v-else-if="aptBoardStore.error" class="text-red-500 p-4">
      {{ aptBoardStore.error }}
    </div>

    <!-- 게시글 없음 -->
    <div v-else-if="!aptBoardStore.boards.length" class="text-gray-500 p-4">
      작성된 게시글이 없습니다.
    </div>

    <!-- 컨텐츠 섹션 -->
    <div v-else class="comment-content type1">
      <!-- 댓글 목록 -->
      <div class="comment-list">
        <HouseComment
          v-for="(board, index) in aptBoardStore.boards"
          :key="board.id"
          :board="{
            ...board,
            profileImageUrl: getProfileImageUrl(board.userId, board.profileImage),
          }"
          :is-first="index === 0"
          @image-error="handleImageError"
        />
      </div>
    </div>

    <!-- 플로팅 아이콘 -->
    <button class="floating-button" @click="handleWritingClick">
      <PencilIcon class="w-5 h-5 text-white" />
    </button>
  </div>

  <HouseCommentWriting v-else />
</template>

<script setup>
import { onMounted, watch, computed, ref } from 'vue'
import { ArrowLeftIcon, PencilIcon } from 'lucide-vue-next'
import { useHouseStore } from '@/stores/houseStore'
import { useAptBoardStore } from '@/stores/aptBoardStore'
import { useUserStore } from '@/stores/user'
import HouseComment from './HouseComment.vue'
import HouseCommentWriting from './HouseCommentWriting.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const houseStore = useHouseStore()
const aptBoardStore = useAptBoardStore()
const userStore = useUserStore()

const comments = ref([])

// 기본 프로필 이미지 추가
const defaultProfileImage =
  'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAVESURBVGiB7VlrTFNnGH7Oc3oKpZRLuQgsXBSwZWAgc2GbqGHGZWbJFn9sGv2h2R+zxGQ/jMm2ZMvMkm3JsvhjyZb9MktMNBp1iVk2o1PmZTjcUKggKgiCcr8Uek7POe93flhpOQWkLaUM6PNP+n7v+z3P+37nfOc7h0A0NDQ0NDQ0NDQ0NDQ0NBYPZKEKt+7Yl0bQwlcpRXYABQBSABgAuAE4AQwC6KKE/MAJ4qXKg/tGFtLnvAls3bEvF4TsA/AEgPj5xAEMAXiLUlypPHjANZ/4OROofGZ/JqXYD2BXpHEUuALgxcqD+3+JJCgiAVqtVkNXbMZBAM9FQ3wKvgTBvlBRUeGfKyBmLuHKysr4rpj0kwDKF0McAAgFtiQZtG9XVlbyc8WEJdDV1RXbFZtxCsDGxVFPITMzVn+qqqoqLpz/rARoIBBzSkXxZADbokgYQEKCTnM8nH9IAoFAYK8K4gAQE8OQF8L5L5gAz/OlKmoDADQaDWEWjVkJUErXqqwNAGAYMrfAVAcKIVZVtf8D0zQEYQlMd4AQYlFV+z+wLEFg0ZjhwBQopUmqak+BYQhYdoYDUx0ghCSpKj4FQgCWJYv3AAAopQmqak+B4wg4bpoDUw4QQpSPHjMghIBhph0z04GYRdd+EoQQMMyM4ZvhgKIoCcuiPgWGIeC46Q5MdUAURWVZEiAEYNmZDkxxgBCSuCzqU6CUIhCYtsbMcCAQUPyqqk6BUgqPR5o9jVJKxsfHf1NVfAqiKMPnm+WAoihjY2O/qio+BZ9PQiAQPgFFUYZHRn5WVXwKPp8En0+e7QAA9Pf3t6iqDkAUZXg8wWkOTA+ilF7r6elRdTPj9QYgSbMTkGV5sLu7+ydV1QF4vQH4/WETgM/na+vs7LyrqjoARVHg8QTDJiDL8kBHR4ea6wEAQJYVeL2BsP6yLPd3dHR8p6o6AEVRMDERPgFJkvra29u/VVUdACEELpcvrL8sy309PT1fq6oOgBAChyN8ArIsD/T19TVOTk6qts4SQjA+7g3rL8vy0MDAwBdut3tMNXEAhBCMjblh5zgurL8kSSODg4OfOxyOEdXEATAMQX//BBiGhE1AkiTH8PDwp3a7fVgVcQAcx6K31wWOY8P6S5I0Ojo6eqK3t3dQFXEAHMeit3ccHMfMTEBRFFdvb+8nPT09A4suDoBhGNjtE+A4FoQQhDu4SJLkHB4e/ri7u7t/0cUZhqCnZwwcx4JhmLAJKIri7u/vP97V1WVfVHGGIejqcoLjGDAMmT2EiqK4BwYGPuro6Li3aOIMQ9DZ6QDHMeA4FoQQzHYgMDg4eLStre3uoogzDEFHhwMsy4DjWBBCwDBk9iAqiuIZGho60tra2hN1cYYhaG93gGUZcBwDQggYhgHDkNlDqCiKZ2Rk5IOWlpbuqIozDEFbmx0sy4LjJsUJIWDZ2UOoKIrX4XAcaW5uvh0VcUII7twZAcsy4DgWLDslPtsBSZK8IyMj7zc1Nd2KWJwQglu3hsGyLFh2UpxlGbDsbAckSfKNjo6+19jYeDMicUIIbt4cAsMwYFkWLMuAYZgpcZad44AkST673f5uQ0PDjXmLE0Jw/foQGIYBwzBgGAYsy4JhmHuVmO2AJEl+h8PxTn19/W/zEieE4Nq1QRBCwDAMWJYFwzD3xKfEZ++FJEnyOZ3Ot+vq6q6HLU4IQUvLIAghU+Isy4JlWTDM/QQIIXMfaERR9LtcrrdqamquhhUnhKC5eRAMw9wTZxgGDMOAZSfFCSFz74VEUfS7XK4DNzr7ql0Bxf3/4RoaGhoaGhoaGhoaGhoaGhoa/zX+Bvz5MULhI7xJAAAAAElFTkSuQmCC'

// 프로필 이미지 URL 계산 함수 추가
const getProfileImageUrl = (userId, profileImage) => {
  if (!userId) return defaultProfileImage

  try {
    // 카카오 계정인 경우
    if (userId.startsWith('KAKAO_')) {
      // 카카오 프로필 이미지가 있는 경우
      if (profileImage && profileImage.startsWith('http')) {
        return profileImage
      }
      return defaultProfileImage
    }

    // 일반 계정인 경우
    return `http://localhost:8080/user/profile-image/${userId}`
  } catch (error) {
    console.error('프로필 이미지 URL 생성 중 에러:', error)
    return defaultProfileImage
  }
}

// 이미지 로드 에러 처리 함수 추가
const handleImageError = (e) => {
  e.target.src = defaultProfileImage
}

// 선택된 아파트가 변경될 때마다 게시글 목록 업데이트
watch(
  () => houseStore.selectedHouse,
  async (newHouse) => {
    if (newHouse && newHouse.aptSeq) {
      await aptBoardStore.fetchBoardsByAptSeq(newHouse.aptSeq)
    } else {
      aptBoardStore.clearBoards()
    }
  },
)

// 컴포넌트 마운트 시 현재 선택된 아파트의 게시글 목록 조회
onMounted(async () => {
  if (houseStore.selectedHouse?.aptSeq) {
    await aptBoardStore.fetchBoardsByAptSeq(houseStore.selectedHouse.aptSeq)
  }
})

const goBack = () => {
  houseStore.hideComments()
}

const handleWritingClick = () => {
  if (!userStore.isLoggedIn) {
    alert('로그인이 필요한 서비스입니다.')
    router.push('/login')
    return
  }
  houseStore.showCommentWritingPanel()
}

const handleCommentUpdated = (updatedComment) => {
  // 댓글 목록 갱신 로직
  const index = comments.value.findIndex((c) => c.id === updatedComment.id)
  if (index !== -1) {
    comments.value[index] = updatedComment
  }
}

const handleCommentDeleted = (commentId) => {
  // 댓글 목록에서 삭제된 댓글 제거
  comments.value = comments.value.filter((c) => c.id !== commentId)

  // 게시글 목록 갱신
  if (aptBoardStore.currentBoard) {
    aptBoardStore.currentBoard.commentsCount--
  }
}
</script>

<style scoped>
.house-comment-list {
  height: 100%;
  background-color: #f5f5f5;
  position: relative;
  display: flex;
  flex-direction: column;
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
  padding-bottom: 80px;
}

.comment-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
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

/* 플로팅 버튼 스타일 수정 */
.floating-button {
  position: fixed;
  left: 650px;
  bottom: 20px;
  background-color: #3b82f6;
  border: none;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 10000;
  transition: all 0.2s ease;
}

.floating-button:hover {
  background-color: #2563eb;
  transform: scale(1.05);
}

/* 모바일 화면 */
@media screen and (max-width: 767px) {
  .floating-button {
    left: auto;
    right: 24px;
    bottom: 24px;
    width: 48px;
    height: 48px;
  }
}

/* 태블릿 화면 (중간 화면) */
@media screen and (min-width: 768px) {
  .floating-button {
    bottom: 24px;
    width: 48px;
    height: 48px;
  }
}

/* 데스크탑 화면 (큰 화면) */
@media screen and (min-width: 1200px) {
  .floating-button {
    left: 750px;
    bottom: 24px;
    width: 48px;
    height: 48px;
  }
}
</style>
