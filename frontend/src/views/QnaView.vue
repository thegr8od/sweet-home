<script setup>
import { ref, computed } from 'vue';
import QnaList from '@/components/board/qna/QnaList.vue';
import QnaWrite from '@/components/board/qna/QnaWrite.vue';
import QnaDetail from '@/components/board/qna/QnaDetail.vue';

const showWriteForm = ref(false);
const selectedQuestion = ref(null);
const listRef = ref(null);

// 현재는 임시로 admin 체크
const isAdmin = computed(() => {
  // 실제 구현에서는 사용자 정보에 따라 반환
  return true; // 테스트를 위해 임시로 true 설정
});

const isLoggedIn = ref(true); // 실제로는 로그인 정보에 따라 설정

const refreshList = () => {
  if (listRef.value) {
    listRef.value.fetchQnaList();
  }0
};
</script>

<template>
  <main class="w-full mt-20 min-h-[calc(100vh-5rem)] p-6 bg-gray-50">
    <div class="max-w-6xl mx-auto">
      <div class="mb-6 flex justify-between items-center">
        <h2 class="text-2xl font-bold text-gray-900">Q&A 게시판</h2>
        <button
          v-if="isLoggedIn"
          @click="showWriteForm = true"
          class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition-colors"
        >
          질문하기
        </button>
      </div>

      <QnaList
        ref="listRef"
        @select-question="selectedQuestion = $event"
        :is-admin="isAdmin"
      />

      <QnaWrite
        v-if="showWriteForm"
        @close="showWriteForm = false"
        @refresh="refreshList"
      />

      <QnaDetail
        v-if="selectedQuestion"
        :question="selectedQuestion"
        :is-admin="isAdmin"
        @close="selectedQuestion = null"
        @refresh="refreshList"
      />
    </div>
  </main>
</template>

<style scoped>
/* 필요한 경우 추가 스타일링 */
</style>
