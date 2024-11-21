<template>
  <div class="min-w-full min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="w-full max-w-md space-y-8">
      <!-- Logo -->
      <div class="flex justify-center mb-6">
        <h1 class="text-3xl font-bold">SWEET HOME</h1>
      </div>

      <!-- Login Form -->
      <div class="bg-white p-8 rounded-lg shadow-sm">
        <h2 class="text-2xl font-medium text-gray-900 mb-8">로그인</h2>

        <form class="space-y-6" @submit.prevent="handleSubmit">
          <!-- ID Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.id"
                type="text"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.id ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="아이디를 입력해주세요"
                @blur="validateId"
              />
              <div v-if="errors.id" class="absolute right-0 top-0 pr-3 flex items-center h-full">
                <AlertCircle class="h-5 w-5 text-red-500" />
              </div>
            </div>
            <p v-if="errors.id" class="mt-1 text-sm text-red-500">{{ errors.id }}</p>
          </div>

          <!-- Password Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.password ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="비밀번호를 입력해주세요"
                @blur="validatePassword"
              />
              <button
                type="button"
                class="absolute right-0 top-0 pr-3 flex items-center h-full"
                @click="togglePasswordVisibility"
              >
                <Eye v-if="!showPassword" class="h-5 w-5 text-gray-400" />
                <EyeOff v-else class="h-5 w-5 text-gray-400" />
              </button>
            </div>
            <p v-if="errors.password" class="mt-1 text-sm text-red-500">{{ errors.password }}</p>
          </div>

          <!-- Remember Me & Forgot Password -->
          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <input
                v-model="form.rememberMe"
                id="remember-me"
                type="checkbox"
                :disabled="isLoading"
                class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              />
              <label for="remember-me" class="ml-2 block text-sm text-gray-700">
                로그인 유지
              </label>
            </div>
            <div class="text-sm">
              <a href="#" class="text-blue-600 hover:text-blue-500"> 비밀번호 찾기 </a>
            </div>
          </div>

          <!-- Login Button -->
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
              로그인 중...
            </span>
            <span v-else>로그인</span>
          </button>

          <!-- Sign Up Link -->
          <div class="text-center mt-4">
            <p class="text-sm text-gray-600">
              아직 SWEET HOME 회원이 아니신가요?
              <router-link to="/register" class="text-blue-600 hover:text-blue-500 font-medium">
                회원가입
              </router-link>
            </p>
          </div>
        </form>

        <!-- Error Alert -->
        <div v-if="loginError" class="mt-4 p-4 rounded-md bg-red-50 border border-red-200">
          <div class="flex">
            <AlertCircle class="h-5 w-5 text-red-400" />
            <div class="ml-3">
              <p class="text-sm text-red-700">
                {{ loginError }}
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
import { useUserStore } from '@/stores/user'
import { apiInstance } from '@/api/index'
import { Eye, EyeOff, AlertCircle, Loader2 } from 'lucide-vue-next'

const api = apiInstance()
const router = useRouter()
const userStore = useUserStore()

const isLoading = ref(false)
const showPassword = ref(false)
const loginError = ref('')

const form = ref({
  id: '',
  password: '',
  rememberMe: false,
})

const errors = ref({
  id: '',
  password: '',
})

// Validation functions
const validateId = () => {
  if (!form.value.id) {
    errors.value.id = '아이디를 입력해주세요.'
  } else {
    errors.value.id = ''
  }
}

const validatePassword = () => {
  if (!form.value.password) {
    errors.value.password = '비밀번호를 입력해주세요.'
  } else {
    errors.value.password = ''
  }
}

const isFormValid = computed(() => {
  return form.value.id && form.value.password && !errors.value.id && !errors.value.password
})

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
}

const handleSubmit = async () => {
  validateId()
  validatePassword()

  if (!isFormValid.value) return

  try {
    isLoading.value = true
    loginError.value = ''

    const response = await api.post('/user/login', {
      id: form.value.id,
      password: form.value.password,
    })

    if (response.data) {
      localStorage.setItem('token', response.data)

      const userResponse = await api.get('/user/info', {
        headers: {
          Authorization: `Bearer ${response.data}`,
        },
      })

      userStore.setUserInfo(userResponse.data)

      router.push('/')
    }
  } catch (error) {
    console.error('로그인 실패:', error)
    loginError.value = '아이디 또는 비밀번호가 올바르지 않습니다.'
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
/* Add any additional custom styles here if needed */
</style>
