<template>
  <div class="min-w-full min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="w-full max-w-md space-y-8">
      <!-- Logo -->
      <div class="flex justify-center mb-6">
        <h1 class="text-3xl font-bold">SWEET HOME</h1>
      </div>

      <!-- Change Password Form -->
      <div class="bg-white p-8 rounded-lg shadow-sm">
        <h2 class="text-2xl font-medium text-gray-900 mb-8">비밀번호 변경</h2>

        <form class="space-y-6" @submit.prevent="handleSubmit">
          <!-- Current Password Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.currentPassword"
                :type="showCurrentPassword ? 'text' : 'password'"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.currentPassword ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="현재 비밀번호를 입력해주세요"
                @blur="validateCurrentPassword"
              />
              <button
                type="button"
                class="absolute right-0 top-0 pr-3 flex items-center h-full"
                @click="toggleCurrentPasswordVisibility"
              >
                <Eye v-if="!showCurrentPassword" class="h-5 w-5 text-gray-400" />
                <EyeOff v-else class="h-5 w-5 text-gray-400" />
              </button>
            </div>
            <p v-if="errors.currentPassword" class="mt-1 text-sm text-red-500">
              {{ errors.currentPassword }}
            </p>
          </div>

          <!-- New Password Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.newPassword"
                :type="showNewPassword ? 'text' : 'password'"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.newPassword ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="새 비밀번호를 입력해주세요"
                @blur="validateNewPassword"
              />
              <button
                type="button"
                class="absolute right-0 top-0 pr-3 flex items-center h-full"
                @click="toggleNewPasswordVisibility"
              >
                <Eye v-if="!showNewPassword" class="h-5 w-5 text-gray-400" />
                <EyeOff v-else class="h-5 w-5 text-gray-400" />
              </button>
            </div>
            <p v-if="errors.newPassword" class="mt-1 text-sm text-red-500">{{ errors.newPassword }}</p>
          </div>

          <!-- Confirm New Password Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.confirmPassword"
                :type="showNewPassword ? 'text' : 'password'"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.confirmPassword ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="새 비밀번호를 다시 입력해주세요"
                @blur="validateConfirmPassword"
              />
            </div>
            <p v-if="errors.confirmPassword" class="mt-1 text-sm text-red-500">
              {{ errors.confirmPassword }}
            </p>
          </div>

          <!-- Submit Button -->
          <button
            type="submit"
            :disabled="isLoading || !isFormValid"
            :class="[
              'w-full flex justify-center py-3 px-4 border border-transparent rounded-md',
              'text-sm font-medium text-white bg-blue-600',
              'focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500',
              'transition-colors duration-200',
              isLoading || !isFormValid ? 'opacity-50 cursor-not-allowed' : 'hover:bg-blue-700',
            ]"
          >
            <span v-if="isLoading" class="flex items-center">
              <Loader2 class="animate-spin -ml-1 mr-2 h-4 w-4" />
              변경 중...
            </span>
            <span v-else>비밀번호 변경</span>
          </button>

          <!-- Back to MyPage Link -->
          <div class="text-center mt-4">
            <router-link
              to="/mypage"
              class="text-sm text-gray-600 hover:text-gray-900 font-medium"
            >
              마이페이지로 돌아가기
            </router-link>
          </div>
        </form>

        <!-- Error Alert -->
        <div v-if="changeError" class="mt-4 p-4 rounded-md bg-red-50 border border-red-200">
          <div class="flex">
            <AlertCircle class="h-5 w-5 text-red-400" />
            <div class="ml-3">
              <p class="text-sm text-red-700">
                {{ changeError }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { apiInstance } from '@/api/index'
import { Eye, EyeOff, AlertCircle, Loader2 } from 'lucide-vue-next'

const api = apiInstance()
const router = useRouter()

const isLoading = ref(false)
const showCurrentPassword = ref(false)
const showNewPassword = ref(false)
const changeError = ref('')

const form = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: '',
})

const errors = ref({
  currentPassword: '',
  newPassword: '',
  confirmPassword: '',
})

// Validation functions
const validateCurrentPassword = () => {
  if (!form.value.currentPassword) {
    errors.value.currentPassword = '현재 비밀번호를 입력해주세요.'
  } else if (form.value.currentPassword.length < 6) {
    errors.value.currentPassword = '비밀번호는 6자 이상이어야 합니다.'
  } else {
    errors.value.currentPassword = ''
  }
}

const validateNewPassword = () => {
  if (!form.value.newPassword) {
    errors.value.newPassword = '새 비밀번호를 입력해주세요.'
  } else if (form.value.newPassword.length < 6) {
    errors.value.newPassword = '비밀번호는 6자 이상이어야 합니다.'
  } else if (form.value.newPassword === form.value.currentPassword) {
    errors.value.newPassword = '현재 비밀번호와 다른 비밀번호를 입력해주세요.'
  } else {
    errors.value.newPassword = ''
  }
}

const validateConfirmPassword = () => {
  if (!form.value.confirmPassword) {
    errors.value.confirmPassword = '새 비밀번호를 다시 입력해주세요.'
  } else if (form.value.newPassword !== form.value.confirmPassword) {
    errors.value.confirmPassword = '비밀번호가 일치하지 않습니다.'
  } else {
    errors.value.confirmPassword = ''
  }
}

const isFormValid = computed(() => {
  return (
    form.value.currentPassword &&
    form.value.newPassword &&
    form.value.confirmPassword &&
    !errors.value.currentPassword &&
    !errors.value.newPassword &&
    !errors.value.confirmPassword
  )
})

const toggleCurrentPasswordVisibility = () => {
  showCurrentPassword.value = !showCurrentPassword.value
}

const toggleNewPasswordVisibility = () => {
  showNewPassword.value = !showNewPassword.value
}

const handleSubmit = async () => {
  // 모든 필드 유효성 검사
  validateCurrentPassword()
  validateNewPassword()
  validateConfirmPassword()

  if (!isFormValid.value) return

  try {
    isLoading.value = true
    changeError.value = ''

    const token = localStorage.getItem('token')
    if (!token) {
      throw new Error('인증 토큰이 없습니다.')
    }

    await api.post(
      '/user/change-password',
      {
        currentPassword: form.value.currentPassword,
        newPassword: form.value.newPassword,
        confirmPassword: form.value.confirmPassword,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    )

    alert('비밀번호가 성공적으로 변경되었습니다.')
    router.push('/mypage')
  } catch (error) {
    console.error('비밀번호 변경 실패:', error)
    if (error.response?.status === 401) {
      changeError.value = '현재 비밀번호가 일치하지 않습니다.'
    } else {
      changeError.value = '비밀번호 변경에 실패했습니다. 다시 시도해주세요.'
    }
  } finally {
    isLoading.value = false
  }
}
</script>
