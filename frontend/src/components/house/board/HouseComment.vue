<template>
  <div>
    <div class="bg-white p-4" :class="{ 'first-comment': isFirst }">
      <!-- User Profile Card -->
      <div class="bg-gray-50 rounded-lg p-4 mb-4">
        <div class="flex items-start gap-4">
          <img
            :src="profileImageUrl"
            :alt="`${board.userId}의 프로필`"
            class="w-12 h-12 rounded-full bg-gray-200 object-cover"
            @error="handleImageError"
          />
          <div class="flex-1">
            <div class="flex justify-between items-center">
              <div>
                <div class="font-medium">{{ board.userId }}</div>
                <div class="text-xs text-gray-500">관심등록 | 팔로워 0</div>
              </div>
              <button
                class="text-sm text-blue-500 border border-blue-500 rounded-full px-3 py-1 hover:bg-blue-50 transition-colors"
              >
                + 팔로우
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- Post Content -->
      <div class="mb-4">
        <h3 class="text-lg font-medium mb-2">{{ board.title }}</h3>
        <p class="text-sm text-gray-700 mb-2">
          {{ board.content }}
        </p>
        <div class="flex justify-between items-center text-xs text-gray-500">
          <span>단지글 · {{ board.aptName }} · 아파트</span>
          <span>{{ formatDate(board.createdAt) }}</span>
        </div>
      </div>

      <!-- Interaction Section -->
      <div class="flex justify-between items-center border-t pt-3">
        <div class="flex w-full">
          <button
            class="interaction-button"
            @click="handleLike"
            :class="{ liked: board.liked && userStore.isLoggedIn }"
          >
            <ThumbsUpIcon class="w-4 h-4" :class="{ liked: board.liked && userStore.isLoggedIn }" />
            좋아요 {{ board.likesCount || 0 }}
          </button>
          <button class="interaction-button" @click="showReComments">
            <MessageCircleIcon class="w-4 h-4" />
            댓글 {{ board.commentsCount || 0 }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ThumbsUpIcon, MessageCircleIcon } from 'lucide-vue-next'
import { useHouseStore } from '@/stores/houseStore'
import { useAptBoardStore } from '@/stores/aptBoardStore'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { ref, computed } from 'vue'

const router = useRouter()
const houseStore = useHouseStore()
const aptBoardStore = useAptBoardStore()
const userStore = useUserStore()

const props = defineProps({
  isFirst: {
    type: Boolean,
    default: false,
  },
  board: {
    type: Object,
    required: true,
    default: () => ({
      id: null,
      userId: '',
      userName: '',
      title: '',
      content: '',
      aptName: '',
      createdAt: '',
      likesCount: 0,
      commentsCount: 0,
      liked: false,
    }),
  },
})

const defaultProfileImage =
  'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAVESURBVGiB7VlrTFNnGH7Oc3oKpZRLuQgsXBSwZWAgc2GbqGHGZWbJFn9sGv2h2R+zxGQ/jMm2ZMvMkm3JsvhjyZb9MktMNBp1iVk2o1PmZTjcUKggKgiCcr8Uek7POe93flhpOQWkLaUM6PNP+n7v+z3P+37nfOc7h0A0NDQ0NDQ0NDQ0NDQ0NBYPZKEKt+7Yl0bQwlcpRXYABQBSABgAuAE4AQwC6KKE/MAJ4qXKg/tGFtLnvAls3bEvF4TsA/AEgPj5xAEMAXiLUlypPHjANZ/4OROofGZ/JqXYD2BXpHEUuALgxcqD+3+JJCgiAVqtVkNXbMZBAM9FQ3wKvgTBvlBRUeGfKyBmLuHKysr4rpj0kwDKF0McAAgFtiQZtG9XVlbyc8WEJdDV1RXbFZtxCsDGxVFPITMzVn+qqqoqLpz/rARoIBBzSkXxZADbokgYQEKCTnM8nH9IAoFAYK8K4gAQE8OQF8L5L5gAz/OlKmoDADQaDWEWjVkJUErXqqwNAGAYMrfAVAcKIVZVtf8D0zQEYQlMd4AQYlFV+z+wLEFg0ZjhwBQopUmqak+BYQhYdoYDUx0ghCSpKj4FQgCWJYv3AAAopQmqak+B4wg4bpoDUw4QQpSPHjMghIBhph0z04GYRdd+EoQQMMyM4ZvhgKIoCcuiPgWGIeC46Q5MdUAURWVZEiAEYNmZDkxxgBCSuCzqU6CUIhCYtsbMcCAQUPyqqk6BUgqPR5o9jVJKxsfHf1NVfAqiKMPnm+WAoihjY2O/qio+BZ9PQiAQPgFFUYZHRn5WVXwKPp8En0+e7QAA9Pf3t6iqDkAUZXg8wWkOTA+ilF7r6elRdTPj9QYgSbMTkGV5sLu7+ydV1QF4vQH4/WETgM/na+vs7LyrqjoARVHg8QTDJiDL8kBHR4ea6wEAQJYVeL2BsP6yLPd3dHR8p6o6AEVRMDERPgFJkvra29u/VVUdACEELpcvrL8sy309PT1fq6oOgBAChyN8ArIsD/T19TVOTk6qts4SQjA+7g3rL8vy0MDAwBdut3tMNXEAhBCMjblh5zgurL8kSSODg4OfOxyOEdXEATAMQX//BBiGhE1AkiTH8PDwp3a7fVgVcQAcx6K31wWOY8P6S5I0Ojo6eqK3t3dQFXEAHMeit3ccHMfMTEBRFFdvb+8nPT09A4suDoBhGNjtE+A4FoQQhDu4SJLkHB4e/ri7u7t/0cUZhqCnZwwcx4JhmLAJKIri7u/vP97V1WVfVHGGIejqcoLjGDAMmT2EiqK4BwYGPuro6Li3aOIMQ9DZ6QDHMeA4FoQQzHYgMDg4eLStre3uoogzDEFHhwMsy4DjWBBCwDBk9iAqiuIZGho60tra2hN1cYYhaG93gGUZcBwDQggYhgHDkNlDqCiKZ2Rk5IOWlpbuqIozDEFbmx0sy4LjJsUJIWDZ2UOoKIrX4XAcaW5uvh0VcUII7twZAcsy4DgWLDslPtsBSZK8IyMj7zc1Nd2KWJwQglu3hsGyLFh2UpxlGbDsbAckSfKNjo6+19jYeDMicUIIbt4cAsMwYFkWLMuAYZgpcZad44AkST673f5uQ0PDjXmLE0Jw/foQGIYBwzBgGAYsy4JhmHuVmO2AJEl+h8PxTn19/W/zEieE4Nq1QRBCwDAMWJYFwzD3xKfEZ++FJEnyOZ3Ot+vq6q6HLU4IQUvLIAghU+Isy4JlWTDM/QQIIXMfaERR9LtcrrdqamquhhUnhKC5eRAMw9wTZxgGDMOAZSfFCSFz74VEUfS7XK4DNzr7ql0Bxf3/4RoaGhoaGhoaGhoaGhoaGhoa/zX+Bvz5MULhI7xJAAAAAElFTkSuQmCC'

const profileImageUrl = computed(() => {
  if (!props.board.userId) return defaultProfileImage

  try {
    // 카카오 계정인 경우
    if (props.board.userId.startsWith('KAKAO_')) {
      // 카카오 프로필 이미지가 있는 경우
      if (props.board.profileImage && props.board.profileImage.startsWith('http')) {
        return props.board.profileImage
      }
      return defaultProfileImage
    }

    // 일반 계정인 경우
    return `http://localhost:8080/user/profile-image/${props.board.userId}`
  } catch (error) {
    console.error('프로필 이미지 URL 생성 중 에러:', error)
    return defaultProfileImage
  }
})

const handleImageError = (e) => {
  e.target.src = defaultProfileImage
}

const formatDate = (dateString) => {
  if (!dateString) return ''

  const now = new Date()
  const postDate = new Date(dateString)
  const diffTime = Math.abs(now - postDate)
  const diffHours = Math.floor(diffTime / (1000 * 60 * 60))

  if (diffHours < 24) {
    if (diffHours === 0) {
      const diffMinutes = Math.floor(diffTime / (1000 * 60))
      if (diffMinutes === 0) {
        return '방금 전'
      }
      return `${diffMinutes}분 전`
    }
    return `${diffHours}시간 전`
  }

  const year = postDate.getFullYear()
  const month = String(postDate.getMonth() + 1).padStart(2, '0')
  const day = String(postDate.getDate()).padStart(2, '0')

  if (year === now.getFullYear()) {
    return `${month}.${day}`
  }

  return `${year}.${month}.${day}`
}

const handleLike = async () => {
  try {
    if (!userStore.isLoggedIn || !userStore.userId) {
      alert('로그인이 필요한 서비스입니다.')
      router.push('/login')
      return
    }

    await aptBoardStore.updateBoardLike(props.board.id, !props.board.liked)
  } catch (error) {
    console.error('좋아요 처리 중 에러:', error)
    if (error.response?.status === 401) {
      alert('로그인이 필요한 서비스입니다.')
      router.push('/login')
    } else {
      alert('좋아요 처리에 실패했습니다.')
    }
  }
}

const showReComments = () => {
  houseStore.showReComments(props.board.id)
}
</script>

<style scoped>
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

.interaction-button.liked {
  color: #3b82f6;
}

.interaction-button svg {
  transition: all 0.2s ease;
}

.interaction-button:hover svg {
  color: #3b82f6;
}

.interaction-button svg.liked {
  color: #3b82f6;
}

.bg-gray-50 {
  margin-bottom: 16px;
}

.first-comment .bg-gray-50 {
  margin-top: 0;
}
</style>
