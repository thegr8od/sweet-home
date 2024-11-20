<template>
  <div class="max-w-2xl mx-auto">
    <h2 class="text-xl font-semibold mb-4">새 글 작성</h2>
    <form @submit.prevent="submitPost">
      <div class="mb-4">
        <label for="title" class="block text-sm font-medium text-gray-700 mb-1">제목</label>
        <input
          id="title"
          v-model="newPost.title"
          type="text"
          required
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
      <div class="mb-4">
        <label for="content" class="block text-sm font-medium text-gray-700 mb-1">내용</label>
        <textarea
          id="content"
          v-model="newPost.content"
          rows="8"
          required
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
        ></textarea>
      </div>
      <div class="mb-4 flex items-center">
        <input
          id="isPrivate"
          v-model="newPost.isPrivate"
          type="checkbox"
          class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
        />
        <label for="isPrivate" class="ml-2 block text-sm text-gray-900">비밀글로 작성</label>
      </div>
      <div class="flex justify-end space-x-3">
        <router-link
          to="/"
          class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          취소
        </router-link>
        <button
          type="submit"
          class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
        >
          작성완료
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const newPost = ref({
  title: '',
  content: '',
  isPrivate: false,
})

const submitPost = () => {
  // Here you would typically send the post data to your backend
  // For now, we'll just simulate adding it and redirect to the main page
  console.log('New post:', newPost.value)
  router.push('/')
}
</script>
