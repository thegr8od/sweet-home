<template>
  <nav class="bg-white shadow-sm fixed top-0 w-full z-50">
    <div class="max-w-7xl mx-auto px-4">
      <div class="flex justify-between h-14">
        <!-- Logo -->
        <div class="flex-shrink-0 flex items-center">
          <a href="/" class="flex items-center">
            <div class="w-10 h-10 bg-blue-500 rounded flex items-center justify-center">
              <HomeIcon class="h-5 w-5 text-white" />
            </div>
            <span class="font-spoqa ml-2 text-base font-normal text-gray-900">SWEET HOME</span>
          </a>
        </div>

        <!-- Navigation Items -->
        <div class="flex space-x-8">
          <a
            v-for="item in menuItems"
            :key="item.id"
            :href="item.href"
            class="inline-flex items-center px-1 pt-1 text-base font-normal text-gray-700 hover:text-blue-500 transition-colors duration-200"
          >
            {{ item.name }}
          </a>
        </div>

        <!-- Right side buttons -->
        <div class="flex items-center space-x-4">
          <router-link to="/login" class="text-base font-normal text-gray-700 hover:text-blue-500">
            회원가입/로그인
          </router-link>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { HomeIcon } from 'lucide-vue-next'

const menuItems = [
  { id: 1, name: '실거래가조회', href: '/house' },
  { id: 2, name: '인사이트', href: '#' },
  { id: 3, name: '질문게시판', href: '/qna' },
]
</script>

<style scoped>
/* Add any additional custom styles here if needed */
</style>
