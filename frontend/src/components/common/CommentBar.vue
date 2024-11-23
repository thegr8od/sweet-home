<template>
  <div class="flex items-start gap-2">
    <button class="text-gray-400 hover:text-gray-600 mt-2">
      <ImageIcon class="w-5 h-5" />
    </button>
    <div class="flex-1 relative">
      <textarea
        ref="textareaRef"
        v-model="commentText"
        placeholder="댓글을 입력해주세요."
        class="w-full px-4 py-2 pr-16 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none overflow-hidden"
        rows="1"
        @input="adjustTextareaHeight"
      ></textarea>
      <div class="absolute right-2 top-2 flex items-center">
        <span class="text-xs text-gray-500 mr-2">{{ characterCount }}자</span>
        <button
          :class="['text-blue-500', { 'opacity-50 cursor-not-allowed': !commentText.trim() }]"
          :disabled="!commentText.trim()"
          @click="handleSubmit"
        >
          <SendIcon class="w-5 h-5" />
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, watch } from 'vue'
import { ImageIcon, SendIcon } from 'lucide-vue-next'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { useHouseStore } from '@/stores/houseStore'

const router = useRouter()
const userStore = useUserStore()
const houseStore = useHouseStore()

const commentText = ref('')
const textareaRef = ref(null)
const characterCount = computed(() => commentText.value.length)

const adjustTextareaHeight = async () => {
  await nextTick()
  const textarea = textareaRef.value
  if (!textarea) return

  textarea.style.height = 'auto'
  const newHeight = Math.min(textarea.scrollHeight, 150)
  textarea.style.height = `${newHeight}px`
}

const handleSubmit = async () => {
  if (!commentText.value.trim()) return

  if (!userStore.isLoggedIn) {
    alert('로그인이 필요한 서비스입니다.')
    router.push('/login')
    return
  }

  emit('comment-added', {
    content: commentText.value,
    boardId: houseStore.selectedBoardId
  })
  
  // 댓글 입력 후 초기화
  commentText.value = ''
  if (textareaRef.value) {
    textareaRef.value.style.height = 'auto'
  }
}

// 컴포넌트가 마운트될 때 초기 높이 설정
onMounted(() => {
  adjustTextareaHeight()
})

// 댓글 텍스트가 변경될 때마다 높이 조절
watch(commentText, () => {
  adjustTextareaHeight()
})

// emit 정의
const emit = defineEmits(['comment-added'])
</script>

<style scoped>
textarea {
  min-height: 40px;
  max-height: 150px;
  line-height: 1.5;
  transition: height 0.1s ease-in-out;
}
</style>
