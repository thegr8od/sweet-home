<template>
  <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" v-if="isOpen">
    <div class="fixed inset-0 z-10 overflow-y-auto">
      <div class="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
        <div class="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg sm:p-6">
          <div class="absolute right-0 top-0 pr-4 pt-4">
            <button
              type="button"
              class="rounded-md bg-white text-gray-400 hover:text-gray-500"
              @click="close"
            >
              <span class="sr-only">닫기</span>
              <X class="h-6 w-6" />
            </button>
          </div>

          <div class="sm:flex sm:items-start">
            <div class="mt-3 text-center sm:mt-0 sm:text-left w-full">
              <h3 class="text-lg font-semibold leading-6 text-gray-900 mb-4">
                비밀번호 찾기
              </h3>

              <form @submit.prevent="handleSubmit">
                <div class="mb-4">
                  <label class="block text-sm font-medium text-gray-700 mb-1">
                    이메일
                  </label>
                  <input
                    type="email"
                    v-model="email"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                    placeholder="가입시 등록한 이메일을 입력해주세요"
                  />
                </div>

                <div class="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
                  <button
                    type="submit"
                    :disabled="isLoading"
                    class="w-full inline-flex justify-center rounded-md border border-transparent px-4 py-2 bg-blue-600 text-base font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:ml-3 sm:w-auto sm:text-sm"
                  >
                    <span v-if="isLoading">처리중...</span>
                    <span v-else>임시 비밀번호 받기</span>
                  </button>
                  <button
                    type="button"
                    class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 px-4 py-2 bg-white text-base font-medium text-gray-700 shadow-sm hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:w-auto sm:text-sm"
                    @click="close"
                  >
                    취소
                  </button>
                </div>
              </form>

              <div v-if="message" :class="['mt-4 p-4 rounded-md', messageClass]">
                <p class="text-sm">{{ message }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { X } from 'lucide-vue-next'
import { apiInstance } from '@/api'

const props = defineProps({
  isOpen: Boolean
})

const emit = defineEmits(['close'])

const api = apiInstance()
const email = ref('')
const isLoading = ref(false)
const message = ref('')
const messageType = ref('')

const messageClass = computed(() => {
  return {
    'bg-green-50 text-green-700': messageType.value === 'success',
    'bg-red-50 text-red-700': messageType.value === 'error'
  }
})

const close = () => {
  emit('close')
  email.value = ''
  message.value = ''
  messageType.value = ''
}

const handleSubmit = async () => {
  try {
    isLoading.value = true
    message.value = ''

    const response = await api.post('/user/reset-password', { email: email.value })

    messageType.value = 'success'
    message.value = response.data

    setTimeout(() => {
      close()
    }, 3000)
  } catch (error) {
    messageType.value = 'error'
    message.value = error.response?.data ||
      '비밀번호 재설정 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.'
    console.error('비밀번호 재설정 에러:', error)
  } finally {
    isLoading.value = false
  }
}
</script>
