<template>
  <div class="min-w-full min-h-full flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="w-full max-w-md space-y-8">
      <!-- Logo -->
      <div class="flex justify-center mb-6">
        <!-- <h1 class="text-3xl font-bold">SWEET HOME</h1> -->
      </div>

      <!-- Register Form -->
      <div class="bg-white p-8 rounded-lg shadow-sm">
        <h2 class="text-2xl font-medium text-gray-900 mb-8">회원가입</h2>

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

          <!-- Password Confirm Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.passwordConfirm"
                :type="showPassword ? 'text' : 'password'"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.passwordConfirm ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="비밀번호를 다시 입력해주세요"
                @blur="validatePasswordConfirm"
              />
            </div>
            <p v-if="errors.passwordConfirm" class="mt-1 text-sm text-red-500">
              {{ errors.passwordConfirm }}
            </p>
          </div>

          <!-- Name Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.name"
                type="text"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.name ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="이름을 입력해주세요"
                @blur="validateName"
              />
            </div>
            <p v-if="errors.name" class="mt-1 text-sm text-red-500">{{ errors.name }}</p>
          </div>

          <!-- Email Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.email"
                type="email"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.email ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="이메일을 입력해주세요"
                @blur="validateEmail"
              />
            </div>
            <p v-if="errors.email" class="mt-1 text-sm text-red-500">{{ errors.email }}</p>
          </div>

          <!-- Age Input -->
          <div>
            <div class="relative">
              <input
                v-model="form.age"
                type="number"
                :class="[
                  'w-full px-3 py-3 border rounded-md',
                  'placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500',
                  errors.age ? 'border-red-300' : 'border-gray-300',
                ]"
                :disabled="isLoading"
                placeholder="나이를 입력해주세요"
                @blur="validateAge"
              />
            </div>
            <p v-if="errors.age" class="mt-1 text-sm text-red-500">{{ errors.age }}</p>
          </div>

          <!-- Register Button -->
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
              가입 중...
            </span>
            <span v-else>회원가입</span>
          </button>

          <!-- Login Link -->
          <div class="text-center mt-4">
            <p class="text-sm text-gray-600">
              이미 스위트홈 회원이신가요?
              <router-link to="/login" class="text-blue-600 hover:text-blue-500 font-medium">
                로그인
              </router-link>
            </p>
          </div>
        </form>

        <!-- Error Alert -->
        <div v-if="registerError" class="mt-4 p-4 rounded-md bg-red-50 border border-red-200">
          <div class="flex">
            <AlertCircle class="h-5 w-5 text-red-400" />
            <div class="ml-3">
              <p class="text-sm text-red-700">
                {{ registerError }}
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
import { useUserStore } from '@/stores/user'

const api = apiInstance()
const router = useRouter()
const userStore = useUserStore()

const isLoading = ref(false)
const showPassword = ref(false)
const registerError = ref('')

const form = ref({
  id: '',
  password: '',
  passwordConfirm: '',
  name: '',
  email: '',
  age: '',
})

const errors = ref({
  id: '',
  password: '',
  passwordConfirm: '',
  name: '',
  email: '',
  age: '',
})

// Validation functions
const validateId = () => {
  if (!form.value.id) {
    errors.value.id = '아이디를 입력해주세요.'
  } else if (form.value.id.length < 4) {
    errors.value.id = '아이디는 4자 이상이어야 합니다.'
  } else {
    errors.value.id = ''
  }
}

const validatePassword = () => {
  if (!form.value.password) {
    errors.value.password = '비밀번호를 입력해주세요.'
  } else if (form.value.password.length < 6) {
    errors.value.password = '비밀번호는 6자 이상이어야 합니다.'
  } else {
    errors.value.password = ''
  }
}

const validatePasswordConfirm = () => {
  if (!form.value.passwordConfirm) {
    errors.value.passwordConfirm = '비밀번호를 다시 입력해주세요.'
  } else if (form.value.password !== form.value.passwordConfirm) {
    errors.value.passwordConfirm = '비밀번호가 일치하지 않습니다.'
  } else {
    errors.value.passwordConfirm = ''
  }
}

const validateName = () => {
  if (!form.value.name) {
    errors.value.name = '이름을 입력해주세요.'
  } else {
    errors.value.name = ''
  }
}

const validateEmail = () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!form.value.email) {
    errors.value.email = '이메일을 입력해주세요.'
  } else if (!emailRegex.test(form.value.email)) {
    errors.value.email = '올바른 이메일 형식이 아닙니다.'
  } else {
    errors.value.email = ''
  }
}

const validateAge = () => {
  if (!form.value.age) {
    errors.value.age = '나이를 입력해주세요.'
  } else if (form.value.age < 1 || form.value.age > 120) {
    errors.value.age = '올바른 나이를 입력해주세요.'
  } else {
    errors.value.age = ''
  }
}

const isFormValid = computed(() => {
  return (
    form.value.id &&
    form.value.password &&
    form.value.passwordConfirm &&
    form.value.name &&
    form.value.email &&
    form.value.age &&
    !errors.value.id &&
    !errors.value.password &&
    !errors.value.passwordConfirm &&
    !errors.value.name &&
    !errors.value.email &&
    !errors.value.age
  )
})

const togglePasswordVisibility = () => {
  showPassword.value = !showPassword.value
}

const handleSubmit = async () => {
  // 모든 필드 유효성 검사
  validateId()
  validatePassword()
  validatePasswordConfirm()
  validateName()
  validateEmail()
  validateAge()

  if (!isFormValid.value) return

  try {
    isLoading.value = true
    registerError.value = ''

    await api.post('/user/create', {
      id: form.value.id,
      password: form.value.password,
      name: form.value.name,
      email: form.value.email,
      age: parseInt(form.value.age),
    })

    alert('회원가입이 완료되었습니다.')
    userStore.setLogout() // 상태 초기화
    router.push('/login')
  } catch (error) {
    console.error('회원가입 실패:', error)
    registerError.value = '회원가입에 실패했습니다. 다시 시도해주세요.'
  } finally {
    isLoading.value = false
  }
}
</script>
