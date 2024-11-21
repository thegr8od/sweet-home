<script setup>
import { ref, computed, onMounted } from 'vue'
import { apiInstance } from '@/api/index.js'
import { useUserStore } from '@/stores/user'

const api = apiInstance()
const userStore = useUserStore()

onMounted(() => {
  console.log('UserStore:', userStore)
  console.log('UserInfo:', userStore.userInfo)
  console.log('Question:', props.question)
})

const props = defineProps({
  question: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['close', 'refresh', 'edit'])
const newAnswer = ref('')

const isAdmin = computed(() => {
  console.log('Checking isAdmin:', userStore.userInfo?.id === 'admin')
  return userStore.userInfo?.id === 'admin'
})

const isAuthor = computed(() => {
  console.log('Current user ID:', userStore.userInfo?.id)
  console.log('Question author:', props.question.userid)
  console.log('Is author match:', userStore.userInfo?.id === props.question.userid)
  return userStore.userInfo?.id === props.question.userid
})

const submitAnswer = async () => {
  try {
    await api.post(
      '/qna/answer',
      {
        idx: props.question.idx,
        answer: newAnswer.value,
      },
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      },
    )
    emit('refresh')
    emit('close')
  } catch (error) {
    console.error('답변 등록 실패:', error)
    alert('답변 등록에 실패했습니다.')
  }
}

const deleteQuestion = async () => {
  if (!confirm('정말 삭제하시겠습니까?')) return
  try {
    await api.delete(`/qna/delete/${props.question.idx}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    })
    emit('refresh')
    emit('close')
  } catch (error) {
    console.error('질문 삭제 실패:', error)
    alert('질문 삭제에 실패했습니다.')
  }
}

const updateQuestion = () => {
  emit('edit', props.question)
}
</script>

<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl">
      <div class="flex justify-between items-center mb-4">
        <h3 class="text-xl font-bold">{{ question.title }}</h3>
        <div class="flex gap-2">
          <div class="text-sm text-gray-500">
            <p>isAuthor: {{ isAuthor }}</p>
            <p>isAdmin: {{ isAdmin }}</p>
          </div>
          <div v-if="isAuthor || isAdmin" class="flex gap-2">
            <button
              @click="updateQuestion"
              class="px-3 py-1 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
              수정하기
            </button>
            <button
              @click="deleteQuestion"
              class="px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
            >
              삭제하기
            </button>
          </div>
        </div>
      </div>

      <div class="mb-4">
        <p class="text-gray-600">작성자: {{ question.userid }}</p>
        <p class="text-gray-600">작성일: {{ question.date }}</p>
      </div>

      <div class="mb-6 p-4 bg-gray-50 rounded">
        <p>{{ question.content }}</p>
      </div>

      <div v-if="question.answer" class="mb-6 p-4 bg-blue-50 rounded">
        <h4 class="font-bold mb-2">답변</h4>
        <p>{{ question.answer }}</p>
        <p class="text-gray-600 mt-2">답변일: {{ question.answer_date }}</p>
      </div>

      <div v-if="isAdmin && !question.answer" class="mb-4">
        <textarea
          v-model="newAnswer"
          placeholder="답변을 입력하세요"
          class="w-full p-2 border rounded h-32"
        ></textarea>
        <button
          @click="submitAnswer"
          class="mt-2 px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
        >
          답변 등록
        </button>
      </div>

      <div class="flex justify-end">
        <button
          @click="emit('close')"
          class="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
        >
          닫기
        </button>
      </div>
    </div>
  </div>
</template>
