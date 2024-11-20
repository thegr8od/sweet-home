<template>
  <div class="max-w-5xl w-full mx-auto px-4 py-8">
    <h1 class="text-2xl font-bold text-center mb-8">Q&A</h1>

    <!-- Categories -->
    <div class="flex justify-center space-x-8 mb-8">
      <a
        v-for="category in categories"
        :key="category.name"
        :href="category.href"
        :class="[
          'text-sm',
          category.active ? 'text-black border-b-2 border-black' : 'text-gray-500 hover:text-black',
        ]"
      >
        {{ category.name }}
      </a>
    </div>

    <!-- Posts Table -->
    <div class="overflow-x-auto">
      <table class="w-full">
        <thead>
          <tr class="border-y border-gray-200">
            <th class="py-4 text-left w-16">No</th>
            <th class="py-4 text-left">제목</th>
            <th class="py-4 text-left w-24">글쓴이</th>
            <th class="py-4 text-left w-24">작성시간</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="post in posts" :key="post.id" class="border-b border-gray-200">
            <td class="py-4">{{ post.id }}</td>
            <td class="py-4">
              <div class="flex items-center space-x-2">
                <LockIcon v-if="post.locked" class="w-4 h-4 text-gray-400" />
                <span :class="{ 'font-medium': post.notice }">
                  {{ post.title }}
                </span>
              </div>
            </td>
            <td class="py-4">{{ post.author }}</td>
            <td class="py-4 text-gray-500">{{ post.date }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Search and Write -->
    <div class="flex justify-between items-center mt-6">
      <div class="relative flex-1 max-w-sm">
        <SearchIcon
          class="absolute left-3 top-1/2 transform -translate-y-1/2 w-4 h-4 text-gray-400"
        />
        <input
          type="text"
          placeholder="Search"
          class="w-full pl-9 pr-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
      <router-link
        to="/write"
        class="ml-4 px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
      >
        글쓰기
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { LockIcon, SearchIcon } from 'lucide-vue-next'

const categories = ref([
  { name: 'NOTICE', href: '#' },
  { name: 'FAQ', href: '#' },
  { name: 'Q&A', href: '#', active: true },
  { name: '1:1 문의', href: '#' },
  { name: 'CLASS', href: '#' },
  { name: '입고정보', href: '#' },
])

const posts = ref([
  {
    id: 17,
    title: '2021년 입고정보게시판 신설',
    author: 'PLIPOP',
    date: '2021-01-26',
    notice: true,
  },
  { id: 16, title: '신상입고 기대합니다!', author: 'PLIPOP', date: '2021-01-26', locked: true },
  { id: 15, title: '무통장입금 가능한가요?', author: 'PLIPOP', date: '2021-01-26', locked: true },
  {
    id: 14,
    title: '신상입고 언제 되요? ㅠㅠㅠㅠ',
    author: 'PLIPOP',
    date: '2021-01-26',
    locked: true,
  },
  {
    id: 13,
    title: '친절한 응대 감사합니다 ^^',
    author: 'PLIPOP',
    date: '2021-01-26',
    locked: true,
  },
  {
    id: 12,
    title: '프로모션으로 상품을 구매했는데요.',
    author: 'PLIPOP',
    date: '2021-01-26',
    locked: true,
  },
  { id: 11, title: '배송 언제 되나요?', author: 'PLIPOP', date: '2021-01-26', locked: true },
  { id: 10, title: '새상품 입고 언제 되나요?', author: 'PLIPOP', date: '2021-01-26', locked: true },
  {
    id: 9,
    title: '스키니진 블루블랙 언제 입고되나요?',
    author: 'PLIPOP',
    date: '2021-01-26',
    locked: true,
  },
  { id: 8, title: '[문의] 문의드립니다.', author: '익명', date: '2019-09-24', locked: true },
  {
    id: 7,
    title: '이거 이름을 모르겠는데 재입고 언제 되나요?',
    author: '익명',
    date: '2019-09-24',
    locked: true,
  },
])
</script>

<style scoped>
/* 추가적인 스타일이 필요한 경우 여기에 작성하세요 */
</style>
