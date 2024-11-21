<script setup>
import { ref, onMounted, computed } from 'vue';
import { apiInstance } from '@/api/index.js';

const api = apiInstance();
const qnaList = ref([]);
const currentPage = ref(1);
const itemsPerPage = 10;
const expandedIdx = ref(null); // 현재 펼쳐진 질문의 idx

const props = defineProps({
  isAdmin: {
    type: Boolean,
    default: false
  }
});

// 현재 로그인한 사용자 ID (실제로는 store에서 가져와야 함)
const currentUserId = ref('user1'); // 테스트용

// 수정 모드 상태 관리
const editMode = ref(null);
const editContent = ref('');

// 게시글 수정
const startEdit = (qna) => {
  editMode.value = qna.idx;
  editContent.value = qna.content;
};

const cancelEdit = () => {
  editMode.value = null;
  editContent.value = '';
};

const saveEdit = async (qna) => {
  try {
    await api.put(`/qna/update/${qna.idx}`, {
      content: editContent.value
    });
    await fetchQnaList();
    editMode.value = null;
  } catch (error) {
    console.error('질문 수정 실패:', error);
    alert('수정에 실패했습니다.');
  }
};

// 게시글 삭제
const deleteQuestion = async (idx) => {
  if (!confirm('정말 삭제하시겠습니까?')) return;
  try {
    await api.delete(`/qna/delete/${idx}`);
    await fetchQnaList();
  } catch (error) {
    console.error('질문 삭제 실패:', error);
    alert('삭제에 실패했습니다.');
  }
};

// 작성자 본인 확인
const isAuthor = (userid) => {
  return currentUserId.value === userid;
};

// 정렬된 리스트 계산
const sortedQnaList = computed(() => {
  return [...qnaList.value].sort((a, b) => b.idx - a.idx); // 번호 내림차순 정렬
});

// 현재 페이지 아이템
const currentItems = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return sortedQnaList.value.slice(start, end);
});

// 전체 페이지 수
const totalPages = computed(() => {
  return Math.ceil(sortedQnaList.value.length / itemsPerPage);
});

const fetchQnaList = async () => {
  try {
    const response = await api.get('/qna/list');
    qnaList.value = response.data;
  } catch (error) {
    console.error('QnA 목록 조회 실패:', error);
  }
};

const formatDate = (dateString) => {
  if (!dateString) return '-';
  try {
    const date = new Date(dateString);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
  } catch (error) {
    return '-';
  }
};

const changePage = (page) => {
  currentPage.value = page;
};

const toggleQuestion = (idx) => {
  expandedIdx.value = expandedIdx.value === idx ? null : idx;
};

const submitAnswer = async (questionIdx) => {
  if (!props.isAdmin) {
    alert('관리자만 답변을 등록할 수 있습니다.');
    return;
  }

  try {
    await api.post('/qna/answer', {
      idx: questionIdx,
      answer: newAnswer.value
    });
    newAnswer.value = ''; // 입력 필드 초기화
    await fetchQnaList(); // 목록 새로고침
  } catch (error) {
    console.error('답변 등록 실패:', error);
    alert('답변 등록에 실패했습니다.');
  }
};

const deleteAnswer = async (questionIdx) => {
  if (!props.isAdmin) {
    alert('관리자만 답변을 삭제할 수 있습니다.');
    return;
  }

  if (!confirm('답변을 삭제하시겠습니까?')) return;

  try {
    await api.delete(`/qna/delete-answer/${questionIdx}`);
    await fetchQnaList();
  } catch (error) {
    console.error('답변 삭제 실패:', error);
    alert('답변 삭제에 실패했습니다.');
  }
};

// admin 체크를 위한 computed 속성 수정
const isAdmin = computed(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}');
  return userInfo.userid === 'admin'; // userid로 수정
});

onMounted(() => {
  fetchQnaList();
});

defineExpose({
  fetchQnaList
});
</script>

<template>
  <div class="w-full bg-white rounded-lg shadow-sm overflow-hidden">
    <div class="px-6 py-4 bg-gray-50 border-b border-gray-200">
      <div class="grid grid-cols-12 gap-4 text-xs font-medium text-gray-500 uppercase">
        <div class="col-span-1 text-center">번호</div>
        <div class="col-span-6">제목</div>
        <div class="col-span-2 text-center">작성자</div>
        <div class="col-span-2 text-center">작성일</div>
        <div class="col-span-1 text-center">상태</div>
      </div>
    </div>

    <div class="divide-y divide-gray-200">
      <div v-for="qna in currentItems" :key="qna.idx" class="transition-colors">
        <div @click="toggleQuestion(qna.idx)" :class="[
          'px-6 py-4 grid grid-cols-12 gap-4 cursor-pointer items-center',
          expandedIdx === qna.idx ? 'bg-blue-50' : 'hover:bg-gray-50'
        ]">
          <div class="col-span-1 text-center text-gray-500">{{ qna.idx }}</div>
          <div class="col-span-6">
            <div class="text-gray-900 font-medium">{{ qna.title }}</div>
          </div>
          <div class="col-span-2 text-center text-gray-500">{{ qna.userid }}</div>
          <div class="col-span-2 text-center text-gray-500">{{ formatDate(qna.date) }}</div>
          <div class="col-span-1 text-center">
            <span :class="[
              'px-2 py-1 rounded-full text-xs font-medium',
              qna.answer
                ? 'bg-green-100 text-green-800'
                : 'bg-yellow-100 text-yellow-800'
            ]">
              {{ qna.answer ? '답변완료' : '답변대기' }}
            </span>
          </div>
        </div>

        <div v-show="expandedIdx === qna.idx" class="px-6 py-4 bg-blue-50">
          <div class="bg-white rounded-lg p-6 shadow-sm">
            <div class="mb-6">
              <div class="flex items-start">
                <div class="flex-shrink-0 w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center mr-3">
                  <span class="text-blue-600 font-medium">Q</span>
                </div>
                <div class="flex-grow">
                  <div class="text-gray-900 whitespace-pre-wrap">{{ qna.content }}</div>
                  <div class="text-sm text-gray-500 mt-2">
                    {{ qna.userid }} | {{ formatDate(qna.date) }}
                  </div>
                </div>
              </div>
            </div>

            <div class="mt-6 border-t border-gray-100 pt-6">
              <div v-if="qna.answer" class="flex items-start">
                <div class="flex-shrink-0 w-8 h-8 bg-green-100 rounded-full flex items-center justify-center mr-3">
                  <span class="text-green-600 font-medium">A</span>
                </div>
                <div class="flex-grow">
                  <div class="text-gray-900 whitespace-pre-wrap">{{ qna.answer }}</div>
                  <div class="flex justify-between items-center mt-2">
                    <div class="text-sm text-gray-500">
                      관리자 | {{ formatDate(qna.answer_date) }}
                    </div>
                    <button
                      v-if="isAdmin"
                      @click.stop="deleteAnswer(qna.idx)"
                      class="text-sm text-red-600 hover:text-red-800"
                    >
                      답변 삭제
                    </button>
                  </div>
                </div>
              </div>

              <div v-else-if="isAdmin" class="flex items-start">
                <div class="flex-shrink-0 w-8 h-8 bg-green-100 rounded-full flex items-center justify-center mr-3">
                  <span class="text-green-600 font-medium">A</span>
                </div>
                <div class="flex-grow">
                  <textarea
                    v-model="newAnswer"
                    placeholder="답변을 입력하세요"
                    class="w-full p-4 border rounded-lg h-32 mb-2 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  ></textarea>
                  <div class="flex justify-end">
                    <button
                      @click="submitAnswer(qna.idx)"
                      class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
                    >
                      답변 등록
                    </button>
                  </div>
                </div>
              </div>

              <div v-else class="text-center text-gray-500 py-4">
                답변 대기중입니다.
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="totalPages > 1" class="px-6 py-4 bg-gray-50 border-t border-gray-200 flex justify-center gap-2">
      <button v-if="currentPage > 1" @click="changePage(currentPage - 1)" class="px-3 py-1 rounded border border-gray-300 hover:bg-gray-100 text-sm text-gray-600 transition-colors duration-150">
        이전
      </button>
      <button v-for="page in totalPages" :key="page" @click="changePage(page)" :class="[
        'px-3 py-1 rounded border text-sm transition-colors duration-150',
        currentPage === page
          ? 'bg-blue-500 text-white border-blue-500 hover:bg-blue-600'
          : 'border-gray-300 text-gray-600 hover:bg-gray-100'
      ]">
        {{ page }}
      </button>
      <button v-if="currentPage < totalPages" @click="changePage(currentPage + 1)" class="px-3 py-1 rounded border border-gray-300 hover:bg-gray-100 text-sm text-gray-600 transition-colors duration-150">
        다음
      </button>
    </div>
  </div>
</template>

<style scoped>
.expanded-content {
  max-height: 1000px;
  transition: all 0.3s ease-in-out;
}

.collapsed-content {
  max-height: 0;
  overflow: hidden;
  transition: all 0.3s ease-in-out;
}

@media (max-width: 768px) {
  .grid-cols-12 {
    grid-template-columns: auto 1fr auto;
  }

  .col-span-1 {
    grid-column: span 1;
  }

  .col-span-6 {
    grid-column: span 2;
  }

  .col-span-2 {
    display: none;
  }
}
</style>
