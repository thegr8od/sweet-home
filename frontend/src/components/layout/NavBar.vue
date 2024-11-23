<template>
  <nav class="bg-white shadow-sm fixed top-0 w-full z-50">
    <div class="max-w-7xl mx-auto px-4">
      <div class="flex justify-between h-14">
        <!-- Logo -->
        <div class="flex-shrink-0 flex items-center">
          <router-link to="/" class="flex items-center">
            <div class="w-10 h-10 bg-blue-500 rounded flex items-center justify-center">
              <HomeIcon class="h-5 w-5 text-white" />
            </div>
            <span class="font-spoqa ml-2 text-base font-normal text-gray-900">SWEET HOME</span>
          </router-link>
        </div>

        <!-- Navigation Items -->
        <div class="flex space-x-8">
          <router-link
            v-for="item in menuItems"
            :key="item.id"
            :to="item.href"
            class="inline-flex items-center px-1 pt-1 text-base font-normal text-gray-700 hover:text-blue-500 transition-colors duration-200"
          >
            {{ item.name }}
          </router-link>
        </div>

        <!-- Right side buttons -->
        <div class="flex items-center space-x-4">
          <template v-if="userStore.isLoggedIn">
            <span class="text-base font-normal text-gray-700">
              {{ userStore.userInfo?.name }}님 안녕하세요
            </span>
            <router-link
              to="/mypage"
              class="text-base font-normal text-gray-700 hover:text-blue-500"
            >
              마이페이지
            </router-link>
            <button
              @click="handleLogout"
              class="text-base font-normal text-gray-700 hover:text-blue-500"
            >
              로그아웃
            </button>
          </template>
          <template v-else>
            <router-link
              to="/login"
              class="text-base font-normal text-gray-700 hover:text-blue-500"
            >
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
import { HomeIcon } from 'lucide-vue-next'
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
/* Add any additional custom styles here if needed */
</style>
