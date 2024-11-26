<template>
  <nav class="bg-white shadow-sm fixed top-0 w-full z-50 border-b border-gray-200">
    <div class="max-w-7xl mx-auto px-4">
      <div class="flex justify-between h-16">
        <div class="flex-shrink-0 flex items-center">
          <router-link to="/" class="flex items-center gap-2">
            <img src="@/assets/img/home.png" alt="Home Logo" class="w-10 h-10" />
            <span class="font-dohyeon text-3xl font-bold mt-1" style="color: #2196f3"
              >스위트홈</span
            >
          </router-link>
        </div>

        <!-- Navigation Items -->
        <div class="flex space-x-8 items-center">
          <router-link
            v-for="item in menuItems"
            :key="item.id"
            :to="item.href"
            class="font-dohyeon inline-flex items-center px-1 pt-1 text-lg text-gray-700 hover:text-blue-500 transition-colors duration-200"
          >
            {{ item.name }}
          </router-link>
        </div>

        <!-- Right side buttons -->
        <div class="flex items-center space-x-4">
          <template v-if="userStore.isLoggedIn">
            <span class="font-dohyeon text-lg text-gray-700">
              {{ userStore.userInfo?.name }}님 안녕하세요
            </span>
            <router-link
              to="/mypage"
              class="font-dohyeon text-lg text-gray-700 hover:text-blue-500"
            >
              마이페이지
            </router-link>
            <button
              @click="handleLogout"
              class="font-dohyeon text-lg text-gray-700 hover:text-blue-500"
            >
              로그아웃
            </button>
          </template>
          <template v-else>
            <router-link to="/login" class="font-dohyeon text-lg text-gray-700 hover:text-blue-500">
              회원가입/로그인
            </router-link>
          </template>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { apiInstance } from '@/api/index'

const api = apiInstance()
const userStore = useUserStore()
const router = useRouter()

const menuItems = [
  { id: 1, name: '실거래가조회', href: '/house' },
  { id: 2, name: '인구변화', href: '/population' },
  { id: 3, name: '인사이트', href: '/insight' },
  { id: 4, name: '질문게시판', href: '/qna' },
]

// 로그아웃 처리
const handleLogout = async () => {
  try {
    const token = localStorage.getItem('token')
    if (!token) {
      userStore.setLogout()
      router.push('/login')
      return
    }

    await api.post(
      '/user/logout',
      {}, // empty body
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      },
    )

    userStore.setLogout()
    router.push('/login')
  } catch (error) {
    console.error('로그아웃 실패:', error)
    // 에러가 발생하더라도 로컬에서는 로그아웃 처리
    // userStore.setLogout()
    // router.push('/login')
  }
}

// 컴포넌트 마운트 시 로그인 상태 복구
onMounted(() => {
  userStore.initializeLoginState()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Do+Hyeon:400');

.font-dohyeon {
  font-family: 'Do Hyeon', sans-serif;
}
</style>
