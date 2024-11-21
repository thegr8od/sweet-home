<script setup>
import { ref } from 'vue'
import { apiInstance } from '@/api/index.js'

const api = apiInstance()
const question = ref({
  title: '',
  content: '',
})

const emit = defineEmits(['close', 'refresh'])

const submitQuestion = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      alert('로그인이 필요합니다.')
      return
    }

    const response = await api.post('/qna/create', question.value, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    if (response.status === 201) {
      alert('질문이 등록되었습니다.')
      emit('refresh')
      emit('close')
    }
  } catch (error) {
    console.error('질문 등록 실패:', error)
    if (error.response?.status === 401) {
      alert('로그인이 필요합니다.')
    } else {
      alert('질문 등록에 실패했습니다.')
    }
  }
}
</script>

<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
    <div class="bg-white rounded-lg p-6 w-full max-w-2xl">
      <h3 class="text-xl font-bold mb-4">질문 작성</h3>

      <div class="mb-4">
        <input
          v-model="question.title"
          type="text"
          placeholder="제목을 입력하세요"
          class="w-full p-2 border rounded"
        />
      </div>

      <div class="mb-4">
        <textarea
          v-model="question.content"
          placeholder="내용을 입력하세요"
          class="w-full p-2 border rounded h-40"
        ></textarea>
      </div>

      <div class="flex justify-end gap-2">
        <button
          @click="submitQuestion"
          class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          등록
        </button>
        <button
          @click="emit('close')"
          class="bg-gray-300 text-gray-700 px-4 py-2 rounded hover:bg-gray-400"
        >
          취소
        </button>
      </div>
    </div>
  </div>
</template>
