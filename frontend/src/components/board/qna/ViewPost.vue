<template>
  <div class="max-w-2xl mx-auto">
    <div v-if="post" class="bg-white shadow-md rounded-lg p-6">
      <h2 class="text-2xl font-bold mb-4">{{ post.title }}</h2>
      <div class="flex justify-between text-sm text-gray-500 mb-4">
        <span>작성자: {{ post.author }}</span>
        <span>작성일: {{ post.date }}</span>
      </div>
      <div class="border-t border-b border-gray-200 py-4 mb-4">
        <p>{{ post.content }}</p>
      </div>
      <div class="flex justify-end">
        <router-link
          to="/"
          class="px-4 py-2 bg-gray-100 text-gray-700 rounded-md hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
        >
          목록으로
        </router-link>
      </div>
    </div>
    <div v-else class="text-center py-8">
      <p class="text-xl text-gray-600">게시글을 찾을 수 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const post = ref(null)

onMounted(() => {
  // In a real application, you would fetch the post data from your backend
  // For this example, we'll use mock data
  const mockPosts = [
    {
      id: 17,
      title: '2021년 입고정보게시판 신설',
      author: 'PLIPOP',
      date: '2021-01-26',
      content: '입고정보게시판이 신설되었습니다. 많은 이용 부탁드립니다.',
    },
    {
      id: 16,
      title: '신상입고 기대합니다!',
      author: 'PLIPOP',
      date: '2021-01-26',
      content: '새로운 상품 입고를 기대하고 있습니다. 언제쯤 들어올까요?',
    },
    // ... add more mock posts as needed
  ]

  const postId = parseInt(route.params.id)
  post.value = mockPosts.find((p) => p.id === postId)
})
</script>
