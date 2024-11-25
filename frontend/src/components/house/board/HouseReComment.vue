<template>
  <div class="bg-white p-4 border-y border-gray-100 w-full">
    <!-- User Profile Line -->
    <div class="flex items-center justify-between mb-3">
      <div class="flex items-center gap-3">
        <img
          :src="profileImageUrl"
          :alt="`${comment.userId}의 프로필`"
          class="w-8 h-8 rounded-full bg-gray-200 object-cover"
          @error="handleImageError"
        />
        <div class="flex items-center gap-2">
          <span class="font-medium text-sm">{{ comment.userId }}</span>
          <span class="text-xs text-gray-500">팔로워 0</span>
        </div>
      </div>
      <button class="text-xs text-blue-500 hover:text-blue-600">+ 팔로우</button>
    </div>

    <!-- Comment Content -->
    <div class="mb-3">
      <p class="text-sm text-gray-700 mb-2">
        {{ comment.content }}
      </p>
      <div class="flex justify-between items-center text-xs text-gray-500">
        <span>{{ formatDate(comment.createdAt) }}</span>
        <div
          class="flex items-center gap-1 cursor-pointer hover:text-blue-500"
          @click="handleLike"
          :class="{ liked: comment.liked && userStore.isLoggedIn }"
        >
          <ThumbsUpIcon class="w-4 h-4" :class="{ liked: comment.liked && userStore.isLoggedIn }" />
          <span class="mt-0.5">좋아요 {{ comment.likesCount || 0 }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ThumbsUpIcon } from 'lucide-vue-next'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { likeComment, unlikeComment } from '@/api/aptBoard'
import { computed } from 'vue'

const router = useRouter()
const userStore = useUserStore()

const props = defineProps({
  comment: {
    type: Object,
    required: true,
  },
})

// 기본 프로필 이미지
const defaultProfileImage =
  'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAAVESURBVGiB7VlrTFNnGH7Oc3oKpZRLuQgsXBSwZWAgc2GbqGHGZWbJFn9sGv2h2R+zxGQ/jMm2ZMvMkm3JsvhjyZb9MktMNBp1iVk2o1PmZTjcUKggKgiCcr8Uek7POe93flhpOQWkLaUM6PNP+n7v+z3P+37nfOc7h0A0NDQ0NDQ0NDQ0NDQ0NBYPZKEKt+7Yl0bQwlcpRXYABQBSABgAuAE4AQwC6KKE/MAJ4qXKg/tGFtLnvAls3bEvF4TsA/AEgPj5xAEMAXiLUlypPHjANZ/4OROofGZ/JqXYD2BXpHEUuALgxcqD+3+JJCgiAVqtVkNXbMZBAM9FQ3wKvgTBvlBRUeGfKyBmLuHKysr4rpj0kwDKF0McAAgFtiQZtG9XVlbyc8WEJdDV1RXbFZtxCsDGxVFPITMzVn+qqqoqLpz/rARoIBBzSkXxZADbokgYQEKCTnM8nH9IAoFAYK8K4gAQE8OQF8L5L5gAz/OlKmoDADQaDWEWjVkJUErXqqwNAGAYMrfAVAcKIVZVtf8D0zQEYQlMd4AQYlFV+z+wLEFg0ZjhwBQopUmqak+BYQhYdoYDUx0ghCSpKj4FQgCWJYv3AAAopQmqak+B4wg4bpoDUw4QQpSPHjMghIBhph0z04GYRdd+EoQQMMyM4ZvhgKIoCcuiPgWGIeC46Q5MdUAURWVZEiAEYNmZDkxxgBCSuCzqU6CUIhCYtsbMcCAQUPyqqk6BUgqPR5o9jVJKxsfHf1NVfAqiKMPnm+WAoihjY2O/qio+BZ9PQiAQPgFFUYZHRn5WVXwKPp8En0+e7QAA9Pf3t6iqDkAUZXg8wWkOTA+ilF7r6elRdTPj9QYgSbMTkGV5sLu7+ydV1QF4vQH4/WETgM/na+vs7LyrqjoARVHg8QTDJiDL8kBHR4ea6wEAQJYVeL2BsP6yLPd3dHR8p6o6AEVRMDERPgFJkvra29u/VVUdACEELpcvrL8sy309PT1fq6oOgBAChyN8ArIsD/T19TVOTk6qts4SQjA+7g3rL8vy0MDAwBdut3tMNXEAhBCMjblh5zgurL8kSSODg4OfOxyOEdXEATAMQX//BBiGhE1AkiTH8PDwp3a7fVgVcQAcx6K31wWOY8P6S5I0Ojo6eqK3t3dQFXEAHMeit3ccHMfMTEBRFFdvb+8nPT09A4suDoBhGNjtE+A4FoQQhDu4SJLkHB4e/ri7u7t/0cUZhqCnZwwcx4JhmLAJKIri7u/vP97V1WVfVHGGIejqcoLjGDAMmT2EiqK4BwYGPuro6Li3aOIMQ9DZ6QDHMeA4FoQQzHYgMDg4eLStre3uoogzDEFHhwMsy4DjWBBCwDBk9iAqiuIZGho60tra2hN1cYYhaG93gGUZcBwDQggYhgHDkNlDqCiKZ2Rk5IOWlpbuqIozDEFbmx0sy4LjJsUJIWDZ2UOoKIrX4XAcaW5uvh0VcUII7twZAcsy4DgWLDslPtsBSZK8IyMj7zc1Nd2KWJwQglu3hsGyLFh2UpxlGbDsbAckSfKNjo6+19jYeDMicUIIbt4cAsMwYFkWLMuAYZgpcZad44AkST673f5uQ0PDjXmLE0Jw/foQGIYBwzBgGAYsy4JhmHuVmO2AJEl+h8PxTn19/W/zEieE4Nq1QRBCwDAMWJYFwzD3xKfEZ++FJEnyOZ3Ot+vq6q6HLU4IQUvLIAghU+Isy4JlWTDM/QQIIXMfaERR9LtcrrdqamquhhUnhKC5eRAMw9wTZxgGDMOAZSfFCSFz74VEUfS7XK4DNzr7ql0Bxf3/4RoaGhoaGhoaGhoaGhoaGhoa/zX+Bvz5MULhI7xJAAAAAElFTkSuQmCC'

// 프로필 이미지 URL 계산
const profileImageUrl = computed(() => {
  if (!props.comment.userId) return defaultProfileImage

  try {
    // 카카오 계정인 경우
    if (props.comment.userId.startsWith('KAKAO_')) {
      // 카카오 프로필 이미지가 있는 경우
      if (props.comment.profileImage && props.comment.profileImage.startsWith('http')) {
        return props.comment.profileImage
      }
      return defaultProfileImage
    }

    // 일반 계정인 경우
    return `http://localhost:8080/user/profile-image/${props.comment.userId}`
  } catch (error) {
    console.error('프로필 이미지 URL 생성 중 에러:', error)
    return defaultProfileImage
  }
})

// 이미지 에러 처리
const handleImageError = (e) => {
  e.target.src = defaultProfileImage
}

const handleLike = async () => {
  if (!userStore.isLoggedIn) {
    alert('로그인이 필요한 서비스입니다.')
    router.push('/login')
    return
  }

  try {
    if (props.comment.liked) {
      await unlikeComment(props.comment.id, userStore.userId)
      props.comment.liked = false
      props.comment.likesCount -= 1
    } else {
      await likeComment(props.comment.id, userStore.userId)
      props.comment.liked = true
      props.comment.likesCount += 1
    }
  } catch (error) {
    console.error('좋아요 처리 중 에러:', error)
    if (error.response?.status === 401) {
      alert('로그인이 필요한 서비스입니다.')
      router.push('/login')
    }
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''

  const now = new Date()
  const commentDate = new Date(dateString)
  const diffTime = Math.abs(now - commentDate)
  const diffHours = Math.floor(diffTime / (1000 * 60 * 60))

  // 24시간 이내인 경우
  if (diffHours < 24) {
    if (diffHours === 0) {
      // 1시간 미만인 경우 분 단위로 표시
      const diffMinutes = Math.floor(diffTime / (1000 * 60))
      if (diffMinutes === 0) {
        return '방금 전'
      }
      return `${diffMinutes}분 전`
    }
    return `${diffHours}시간 전`
  }

  // 24시간이 지난 경우 년월일 표시
  const year = commentDate.getFullYear()
  const month = String(commentDate.getMonth() + 1).padStart(2, '0')
  const day = String(commentDate.getDate()).padStart(2, '0')

  // 올해 댓글은 년도 생략
  if (year === now.getFullYear()) {
    return `${month}.${day}`
  }

  return `${year}.${month}.${day}`
}

// emit 정의
const emit = defineEmits(['like-updated'])
</script>

<style scoped>
.bg-white {
  padding: 12px 16px;
}

/* 좋아요 버튼 스타일 */
.liked {
  color: #3b82f6;
}
</style>
