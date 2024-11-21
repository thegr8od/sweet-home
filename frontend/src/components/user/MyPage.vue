<template>
  <div class="min-h-screen bg-gray-50 py-12">
    <div class="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="bg-white shadow rounded-lg p-8">
        <h2 class="text-2xl font-bold mb-8 text-gray-900">마이페이지</h2>

        <form @submit.prevent="handleSubmit" class="space-y-6">
          <!-- 읽기 전용 정보 섹션 -->
          <div class="space-y-6 pb-6">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">아이디</label>
              <input
                type="text"
                :value="userInfo.id"
                disabled
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm bg-gray-50 text-gray-500"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">이메일</label>
              <input
                type="email"
                :value="userInfo.email"
                disabled
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm bg-gray-50 text-gray-500"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">가입일</label>
              <input
                type="text"
                :value="formatDate(userInfo.joinDate)"
                disabled
                class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm bg-gray-50 text-gray-500"
              />
            </div>
          </div>

          <!-- 수정 가능한 정보 섹션 -->
          <div class="space-y-6 pt-6 border-t border-gray-200">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">이름</label>
              <input
                v-model="form.name"
                type="text"
                @blur="validateName"
                :class="[
                  'w-full px-3 py-2 border rounded-md shadow-sm',
                  errors.name ? 'border-red-300' : 'border-gray-300',
                  'focus:ring-blue-500 focus:border-blue-500',
                ]"
              />
              <p v-if="errors.name" class="mt-1 text-sm text-red-600">{{ errors.name }}</p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">나이</label>
              <input
                v-model="form.age"
                type="number"
                @blur="validateAge"
                :class="[
                  'w-full px-3 py-2 border rounded-md shadow-sm',
                  errors.age ? 'border-red-300' : 'border-gray-300',
                  'focus:ring-blue-500 focus:border-blue-500',
                ]"
              />
              <p v-if="errors.age" class="mt-1 text-sm text-red-600">{{ errors.age }}</p>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">새 비밀번호</label>
              <input
                v-model="form.password"
                type="password"
                @blur="validatePassword"
                :class="[
                  'w-full px-3 py-2 border rounded-md shadow-sm',
                  errors.password ? 'border-red-300' : 'border-gray-300',
                  'focus:ring-blue-500 focus:border-blue-500',
                ]"
                placeholder="변경하지 않으려면 비워두세요"
              />
              <p v-if="errors.password" class="mt-1 text-sm text-red-600">{{ errors.password }}</p>
            </div>

            <!-- 정보 수정 버튼 -->
            <div class="flex justify-end pt-4">
              <button
                type="submit"
                :disabled="isLoading || !isFormValid"
                class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors duration-200"
              >
                {{ isLoading ? '수정 중...' : '정보 수정' }}
              </button>
            </div>
          </div>
        </form>

        <!-- 회원탈퇴 영역 -->
        <div class="mt-8">
          <div class="flex justify-end">
            <button
              type="button"
              @click="showDeleteConfirm = true"
              class="text-sm text-gray-500 hover:text-gray-700 hover:underline transition-colors duration-200"
            >
              회원탈퇴
            </button>
          </div>
        </div>

        <!-- 회원탈퇴 확인 모달 -->
        <div
          v-if="showDeleteConfirm"
          class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
        >
          <div class="bg-white p-6 rounded-lg shadow-xl max-w-sm w-full mx-4">
            <h3 class="text-lg font-medium text-gray-900 mb-4">회원 탈퇴</h3>
            <p class="text-sm text-gray-600 mb-6">
              탈퇴 시 모든 정보가 삭제되며 복구할 수 없습니다. 정말 탈퇴하시겠습니까?
            </p>
            <div class="flex justify-end space-x-3">
              <button
                @click="showDeleteConfirm = false"
                class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md hover:bg-gray-50 transition-colors duration-200"
              >
                취소
              </button>
              <button
                @click="handleDelete"
                :disabled="isDeleting"
                class="px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-md hover:bg-red-700 transition-colors duration-200"
              >
                {{ isDeleting ? '처리 중...' : '탈퇴하기' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { apiInstance } from '@/api/index'

const api = apiInstance()
const router = useRouter()
const userStore = useUserStore()

const userInfo = ref({})
const isLoading = ref(false)
const isDeleting = ref(false)
const showDeleteConfirm = ref(false)

const form = ref({
  name: '',
  age: '',
  password: '',
})

const errors = ref({
  name: '',
  age: '',
  password: '',
})

const isFormValid = ref(true)

const formatDate = (dateString) => {
  if (!dateString) return '-'
  try {
    const date = new Date(dateString)
    return `${date.getFullYear()}년 ${String(date.getMonth() + 1).padStart(2, '0')}월 ${String(
      date.getDate(),
    ).padStart(2, '0')}일`
  } catch (error) {
    console.error('날짜 변환 오류:', error)
    return '-'
  }
}

const getUserInfo = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      throw new Error('토큰이 없습니다.')
    }

    const response = await api.get('/user/info', {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    userInfo.value = response.data
    form.value.name = response.data.name
    form.value.age = response.data.age
  } catch (error) {
    console.error('사용자 정보 조회 실패:', error)
    if (error.response?.status === 401) {
      alert('인증이 만료되었습니다. 다시 로그인해주세요.')
      userStore.logout()
      router.push('/login')
    }
  }
}

const handleSubmit = async () => {
  // 유효성 검사 실행
  const isNameValid = validateName()
  const isAgeValid = validateAge()
  const isPasswordValid = validatePassword()

  if (!isNameValid || !isAgeValid || !isPasswordValid) {
    return
  }

  try {
    isLoading.value = true
    const token = localStorage.getItem('token')

    if (!token) {
      throw new Error('인증 토큰이 없습니다.')
    }

    const updateData = {
      id: userInfo.value.id,
      name: form.value.name.trim(),
      age: parseInt(form.value.age),
      email: userInfo.value.email,
    }

    // 비밀번호가 입력된 경우에만 updateData에 포함
    if (form.value.password && form.value.password.trim()) {
      updateData.password = form.value.password.trim()
    }

    const response = await api.post('/user/update', updateData, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })

    if (response.status === 200) {
      alert('회원정보��� 수정되었습니다.')
      // 사용자 정보 다시 불러오기
      const userResponse = await api.get('/user/info', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      userInfo.value = userResponse.data
      form.value.password = '' // 비밀번호 필드 초기화
    }
  } catch (error) {
    console.error('회원정보 수정 실패:', error)
    if (error.response?.status === 401) {
      alert('인증이 만료되었습니다. 다시 로그인해주세요.')
      userStore.logout()
      router.push('/login')
    } else {
      alert('회원정보 수정에 실패했습니다.')
    }
  } finally {
    isLoading.value = false
  }
}

const handleDelete = async () => {
  try {
    isDeleting.value = true
    const token = localStorage.getItem('token')

    if (!token) {
      throw new Error('인증 토큰이 없습니다.')
    }

    const cleanToken = token.trim()

    const response = await api.post('/user/delete', null, {
      headers: {
        Authorization: `Bearer ${cleanToken}`,
        'Content-Type': 'application/json',
      },
    })

    if (response.status === 200) {
      alert('회원탈퇴가 완료되었습니다.')
      userStore.logout()
      router.push('/')
    }
  } catch (error) {
    console.error('회원탈퇴 실패:', error)
    if (error.response?.status === 401) {
      alert('인증이 만료되었습니다. 다시 로그인해주세요.')
      userStore.logout()
      router.push('/login')
    } else {
      alert('회원탈퇴에 실패했습니다. 관리자에게 문의해주세요.')
    }
  } finally {
    isDeleting.value = false
    showDeleteConfirm.value = false
  }
}

// 유효성 검사 함수들
const validateName = () => {
  if (!form.value.name?.trim()) {
    errors.value.name = '이름을 입력해주세요.'
    return false
  }
  errors.value.name = ''
  return true
}

const validateAge = () => {
  const age = parseInt(form.value.age)
  if (!form.value.age) {
    errors.value.age = '나이를 입력해주세요.'
    return false
  }
  if (isNaN(age) || age < 1 || age > 150) {
    errors.value.age = '올바른 나이를 입력해주세요.'
    return false
  }
  errors.value.age = ''
  return true
}

const validatePassword = () => {
  if (form.value.password && form.value.password.length > 0 && form.value.password.length < 6) {
    errors.value.password = '비밀번호는 6글자 이상이어야 합니다.'
    return false
  }
  errors.value.password = ''
  return true
}

// 실시간 폼 유효성 검사
watch(
  [() => form.value.name, () => form.value.age, () => form.value.password, () => errors.value],
  () => {
    isFormValid.value =
      !errors.value.name &&
      !errors.value.age &&
      !errors.value.password &&
      form.value.name?.trim() &&
      form.value.age
  },
  { immediate: true },
)

onMounted(() => {
  getUserInfo()
})
</script>
